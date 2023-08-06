
@Composable
fun CategoryGrid(
    categoryList: List<CategoryModel>,
    checkedCategoryIdList: List<String>,
    categoryOnClick: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        itemsIndexed(categoryList) { index, category ->
            CategoryItem(
                category = category,
                checked = checkedCategoryIdList.contains(category.id),
                onCheckChange = { categoryOnClick(category.id) },
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview10() {
    val list = getCategoryListExample()
    val categoryList = remember {
        mutableStateListOf(
            list[0],
            list[1],
            list[2],
            list[3],
            list[4],
            list[5],
            list[6],
            list[7],
            list[8],
            list[9]
        )
    }
    val checkedCategoryIdList = remember { mutableStateListOf<String>() }
    CategoryGrid(
        categoryList = categoryList,
        checkedCategoryIdList = checkedCategoryIdList,
        categoryOnClick =
        { id ->
            if (checkedCategoryIdList.contains((id))) {
                checkedCategoryIdList.remove(id)

            } else {
                if (checkedCategoryIdList.size < 3) {
                    checkedCategoryIdList.add(id)
                }
            }
        }
    )
}



