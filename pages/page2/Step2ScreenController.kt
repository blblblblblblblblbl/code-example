
@Composable
fun Step2ScreenController(viewModel: PostCreationViewModel) {
    Step2ScreenView(
        maxCategoryChooseCount = PostCreationViewModel.MAX_CATEGORY_CHOOSE_COUNT,
        categoryList = viewModel.categoriesStateList,
        checkedCategoryIdList = viewModel.checkedCategoryIdsStateList.toList(),
        categoryOnClick = { viewModel.toggleCategoryCheckedState(it) }
    )
    InstructionsPlacementController(InstructionsDestinations.POST_CREATION)
}