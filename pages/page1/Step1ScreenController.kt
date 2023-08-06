@Composable
fun Step1ScreenController(viewModel: PostCreationViewModel) {

    val multipleMediaPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uriList ->
            val sortedList = uriList.sortedByDescending { it.toString() }
            viewModel.addMediaElements(sortedList)
        }
    )

    Step1ScreenView(
        postText = viewModel.postTextState,
        maxTextLength = PostCreationViewModel.MAX_POST_TEXT_LENGTH,
        galleryElements = viewModel.mediaElementsStateList,
        onPostTextChanged = { viewModel.postTextState = it },
        addElement = {
            val mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
            val pickVisualMediaRequest = PickVisualMediaRequest(mediaType)
            multipleMediaPicker.launch(pickVisualMediaRequest)
        },
        onRetryUploadClick = { uuid -> viewModel.uploadMediaElement(uuid) },
        removeElement = { viewModel.removeMediaElement(it) },
        maxPhotosCount = PostCreationViewModel.MAX_PHOTOS_COUNT,
        maxVideosCount = PostCreationViewModel.MAX_VIDEOS_COUNT,
        moveItem = viewModel::moveMediaElement,
        canDragOver = viewModel::canDragOver
    )
}
