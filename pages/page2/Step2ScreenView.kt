
@Composable
fun Step2ScreenView(
    maxCategoryChooseCount: Int,
    categoryList: List<CategoryModel>,
    checkedCategoryIdList: List<String>,
    categoryOnClick: (categoryId: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.choose_category),
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = AppTheme.colors.dark
            )
            Text(
                text = stringResource(id = R.string.max) + " $maxCategoryChooseCount",
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                color = AppTheme.colors.dark.copy(0.3f)
            )
        }
        CategoryGrid(
            modifier = Modifier.fillMaxWidth(),
            categoryList = categoryList,
            checkedCategoryIdList = checkedCategoryIdList,
            categoryOnClick = categoryOnClick
        )

    }
}


@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    val categoryList = getCategoryListExample()
    val checkedCategoryIdList = remember { mutableStateListOf<String>() }
    Step2ScreenView(
        maxCategoryChooseCount = MAX_CATEGORY_CHOOSE_COUNT,
        categoryList = categoryList,
        checkedCategoryIdList = checkedCategoryIdList,
        categoryOnClick =
        { id ->
            if (checkedCategoryIdList.contains((id))) {
                checkedCategoryIdList.remove(id)
            } else {
                if (checkedCategoryIdList.size < MAX_CATEGORY_CHOOSE_COUNT) {
                    checkedCategoryIdList.add(id)
                }
            }
        }
    )
}

private const val MAX_CATEGORY_CHOOSE_COUNT = 3