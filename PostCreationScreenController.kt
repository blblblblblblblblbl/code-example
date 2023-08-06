@Composable
fun PostCreationScreenController() {
    val viewModel = getViewModel<PostCreationViewModel>()

    val context = LocalContext.current
    val mainRouter = LocalMainRouter.current

    LaunchedEffect(Unit) {
        launch {
            viewModel.errorsFlow.collect {
                showDefaultErrorToast(context, it)
            }
        }
        launch {
            viewModel.postCreatedFlow.collect {
                Toast.makeText(context, "Post created", Toast.LENGTH_LONG).show()
                mainRouter.goBack()
            }
        }
    }

    if (viewModel.showInitialLoadRetry) {
        RetryStub(
            onClick = { viewModel.initPost() },
            background = AppTheme.colors.background
        )
        return
    }

    var currentPageIndex by remember { mutableStateOf(0) }

    PostCreationScreenView(
        step1Content = { Step1ScreenController(viewModel) },
        step2Content = { Step2ScreenController(viewModel) },
        step3Content = { Step3ScreenController(viewModel) },
        isNextButtonEnabled = isNextButtonEnabled(vm = viewModel, pageIndex = currentPageIndex),
        onPageChanged = { currentPageIndex = it },
        onBackClick = {mainRouter.goBack()},
        postCreateOnClick = { viewModel.createPost() }
    )
    if (viewModel.isLoadingState) {
        FullScreenLoading(background = AppTheme.colors.background)
    }
}

@Composable
private fun isNextButtonEnabled(vm: PostCreationViewModel, pageIndex: Int) =
    when (pageIndex) {
        0 -> isPage1NextButtonEnabled(vm.postTextState, vm.mediaElementsStateList)
        1 -> isPage2NextButtonEnabled(vm.checkedCategoryIdsStateList)
        2 -> isPage3NextButtonEnabled(vm.checkedTagsStateList)
        else -> error("Wrong page index $pageIndex")
    }


private fun isPage1NextButtonEnabled(
    postText: String,
    uploadElementsMap: List<UploadMediaModel>
): Boolean {
    val postSize = postText.length
    val postSizeWithoutSpaces = postText.replace(" ","").length
    val uploadElementsCount = uploadElementsMap.size
    val uploadPhotoCount = uploadElementsMap.count { it.mediaType == MediaType.PHOTO }
    val uploadVideoCount = uploadElementsMap.count { it.mediaType == MediaType.VIDEO }
    val allMediaSuccessUploaded = uploadElementsMap.all { it.state == UploadState.SUCCESS }

    return postSize <= PostCreationViewModel.MAX_POST_TEXT_LENGTH
            && (uploadElementsCount > 0 || postSizeWithoutSpaces > 0)
            && uploadPhotoCount <= PostCreationViewModel.MAX_PHOTOS_COUNT
            && uploadVideoCount <= PostCreationViewModel.MAX_VIDEOS_COUNT
            && allMediaSuccessUploaded
}

private fun isPage2NextButtonEnabled(checkedCategoryIdsList: List<String>): Boolean {
    return checkedCategoryIdsList.size in 1..PostCreationViewModel.MAX_CATEGORY_CHOOSE_COUNT
}

private fun isPage3NextButtonEnabled(
    checkedTagsList: List<TagModel>,
): Boolean =
    checkedTagsList.size <=PostCreationViewModel.MAX_TAG_CHOOSE_COUNT


