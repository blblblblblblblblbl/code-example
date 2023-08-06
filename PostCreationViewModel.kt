class PostCreationViewModel(
    private val postCreationRepository: PostCreateRepository,
    private val categoryRepository: CategoryRepository,
    private val tagRepository: TagRepository,
    private val uriUtils: MediaFileUtils
) : BaseViewModel() {



    //uuid -> UploadMediaModel
    val mediaElementsStateList = mutableStateListOf<UploadMediaModel>()

    val checkedTagsStateList = mutableStateListOf<TagModel>()
    val checkedCategoryIdsStateList = mutableStateListOf<String>()
    val categoriesStateList = categoryRepository.categoriesFlow.value.toMutableStateList()
    val popularTagsStateList = mutableStateListOf<TagModel>()
    val searchedTagsStateList = mutableStateListOf<TagModel>()
    var isSearchedTagExistState by mutableStateOf<Boolean?>(null)


    var postTextState by mutableStateOf("")
    var tagSearchTextState by mutableStateOf(""); private set
    var isLoadingState by mutableStateOf(true); private set
    var isSearchedTagsLoadingState by mutableStateOf(false); private set

    var showSearchedTagRetry by mutableStateOf(false); private set
    var showInitialLoadRetry by mutableStateOf(false); private set
    val errorsFlow = MutableSharedFlow<Throwable>()
    val postCreatedFlow = MutableSharedFlow<Unit>()

    private var isPostInitialized: Boolean = false
    private var lastLoadingJob: Job? = null

    private val uploadingMediaJobs = mutableMapOf<String, Job>() //uuid -> Job

    init {
        initPost()
    }

    fun initPost(){
        viewModelScope.launch(errorsFlow.asExceptionHandler()) {
            isLoadingState = true
            showInitialLoadRetry = false
            try {
                require(!isPostInitialized){ "post already initialized" }
                val initPostJob = viewModelScope.async { postCreationRepository.initPost() }
                val loadPopularTagsAsync = viewModelScope.async { tagRepository.getPopularTags() }

                popularTagsStateList.clear()
                popularTagsStateList += loadPopularTagsAsync.await()
                initPostJob.await()
                isPostInitialized = true
            } catch (e:Exception){
                showInitialLoadRetry = true
                e.rethrowCancellationException()
                errorsFlow.emit(e)
            } finally {
                isLoadingState = false
            }
        }
    }

    fun addMediaElements(uris: List<Uri>) {
        viewModelScope.launch(errorsFlow.asExceptionHandler()) {
            val urisFilteredBySize = uris.filter { uriUtils.fileSizeInBytes(it) < MAX_MEDIA_SIZE_BYTES}
            if (urisFilteredBySize.size < uris.size) {
                errorsFlow.emit(Exception("file size is bigger than 100 MB"))
            }
            val mediaModels = urisFilteredBySize.map {uri->
                val uuid = UUID.randomUUID().toString()
                UploadMediaModel(uri.toString(), uuid, uriUtils.parseMediaType(uri), UploadState.LOADING)
            }
            mediaModels.forEach { mediaModel->
                viewModelScope.launch(errorsFlow.asExceptionHandler()){
                    if (mediaElementsStateList.any { it.uuid == mediaModel.uuid }){
                        throw Exception("element with this uuid already exist")
                    }
                    mediaElementsStateList.add(mediaModel)
                    uploadMediaElement(mediaModel.uuid)
                }
            }
        }
    }

    fun uploadMediaElement(uuid: String) {
        val uploadingMediaModel = mediaElementsStateList.first { it.uuid == uuid }
        mediaElementsStateList.updateIf(predicate = { it.uuid == uuid}) {
            it.copy(state = UploadState.LOADING)
        }
        uploadingMediaJobs[uuid] = viewModelScope.launch {
            try {
                postCreationRepository.uploadMedia(
                    uploadingMediaModel.uri,
                    uploadingMediaModel.uuid
                )
                mediaElementsStateList.updateIf(predicate = { it.uuid == uuid }) {
                    it.copy(state = UploadState.SUCCESS)
                }
            } catch (e: Exception) {
                e.rethrowCancellationException()
                mediaElementsStateList.updateIf(predicate = { it.uuid == uuid }) {
                    it.copy(state = UploadState.FAILURE)
                }
            }
        }
    }

    fun removeMediaElement(uuid: String) {
        val uploadingJob = requireNotNull(uploadingMediaJobs[uuid]) {
            "uploading job for $uuid not found"
        }
        uploadingJob.cancel()
        postCreationRepository.removeMedia(uuid)
        mediaElementsStateList.removeAll { it.uuid == uuid }
    }

    fun moveMediaElement(from: Int, to: Int) {
        mediaElementsStateList.add(to, mediaElementsStateList.removeAt(from))
    }


    fun canDragOver(from: Int, to: Int): Boolean {
        return to in 0 until mediaElementsStateList.size && from in 0 until mediaElementsStateList.size
    }


    fun toggleCategoryCheckedState(categoryId: String) {
        if (checkedCategoryIdsStateList.contains((categoryId))) {
            checkedCategoryIdsStateList.remove(categoryId)
        } else if (checkedCategoryIdsStateList.size < MAX_CATEGORY_CHOOSE_COUNT) {
            checkedCategoryIdsStateList.add(categoryId)
        }
    }

    fun toggleTagCheckedState(tag: TagModel) {
        if (checkedTagsStateList.any { it.id == tag.id }) {
            checkedTagsStateList.removeAll { it.id == tag.id }
        } else if (checkedTagsStateList.size < MAX_TAG_CHOOSE_COUNT) {
            checkedTagsStateList.add(tag)
        }
    }


    private fun emitError(message:String){
        viewModelScope.launch {
            errorsFlow.emit(Exception(message))
        }
    }
    fun setTagSearchText(text: String) {
        if (text.length > MAX_TAG_INPUT_LENGTH) {
            emitError("Text length is too big")
            return
        }
        val processedText = text.replace(" ", "")
        if (processedText == tagSearchTextState) return
        tagSearchTextState = processedText

        if (tagSearchTextState.isNotEmpty()) {
            searchTags()
        } else {
            lastLoadingJob?.cancel()
            searchedTagsStateList.clear()
            showSearchedTagRetry = false
            isSearchedTagsLoadingState = false
            isSearchedTagExistState = null
        }
    }

    fun searchTags() {
        lastLoadingJob?.cancel()
        lastLoadingJob = viewModelScope.launch {
            isSearchedTagsLoadingState = true
            showSearchedTagRetry = false
            searchedTagsStateList.clear()
            try {
                val searchResult =
                    tagRepository.getTagPageByName(
                        tagSearchTextState,
                        INITIAL_PAGE,
                        PAGE_SIZE
                    )
                searchedTagsStateList += searchResult
                isSearchedTagExistState = tagRepository.tagIsExist(tagSearchTextState)
                showSearchedTagRetry = false
            } catch (e: Exception) {
                showSearchedTagRetry = true
                e.rethrowCancellationException()
                errorsFlow.emit(e)
            } finally {
                isSearchedTagsLoadingState = false
            }
        }
    }

    fun createTag() {
        viewModelScope.launch(errorsFlow.asExceptionHandler()) {
            tagRepository.createTag(tagSearchTextState)
            searchTags()
        }
    }

    fun createPost(parentPostId: Long? = null) {
        check(mediaElementsStateList.all { it.state == UploadState.SUCCESS }) {
            "Some media file not uploaded yet!"
        }

        val postCreateModel = PostCreateModel(
            textContent = postTextState,
            tagIds = checkedTagsStateList.map { it.id },
            categoryIds = checkedCategoryIdsStateList,
            parentPostId = parentPostId,
            mediaUUIDs = mediaElementsStateList.map { it.uuid }
        )

        isLoadingState = true

        viewModelScope.launch(errorsFlow.asExceptionHandler()) {
            try {
                postCreationRepository.createPost(postCreateModel)
                postCreatedFlow.emit(Unit)
            } finally {
                isLoadingState = false
            }
        }
    }

    companion object {
        const val MAX_CATEGORY_CHOOSE_COUNT = 3
        const val MAX_TAG_CHOOSE_COUNT = 3
        const val MAX_POST_TEXT_LENGTH = 300
        const val MAX_TAG_LENGTH = 30
        const val MAX_PHOTOS_COUNT = 10
        const val MAX_VIDEOS_COUNT = 3
        const val MAX_MEDIA_SIZE_BYTES = 104857600//100MB in bytes
        const val MAX_TAG_INPUT_LENGTH = 200
        private const val INITIAL_PAGE = 0
        private const val PAGE_SIZE = 50
    }
}
