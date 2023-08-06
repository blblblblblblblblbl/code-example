
@Composable
fun Step3ScreenController(viewModel: PostCreationViewModel) {

    val searchTextIsEmpty = viewModel.tagSearchTextState.isEmpty()

    Step3ScreenView(
        tags = if (searchTextIsEmpty) viewModel.popularTagsStateList else viewModel.searchedTagsStateList,
        checkedTagsList = viewModel.checkedTagsStateList.toList(),
        onTagCheckChanged = { viewModel.toggleTagCheckedState(it) },
        isSearchedTagExist = viewModel.isSearchedTagExistState ?: true,
        tagSearchText = viewModel.tagSearchTextState,
        onTagSearchTextChanged = { viewModel.setTagSearchText(it) },
        onCreateTagClick = { viewModel.createTag() },
        isShowSearchedTagRetry = viewModel.showSearchedTagRetry,
        onRetryClick = { viewModel.searchTags() },
        isSearchedTagsLoadingState = viewModel.isSearchedTagsLoadingState,
        maxTagLength = PostCreationViewModel.MAX_TAG_LENGTH,
        maxTagChooseCount = PostCreationViewModel.MAX_TAG_CHOOSE_COUNT
    )
}