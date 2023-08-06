
@Composable
fun Step3ScreenView(
    tags: List<TagModel>,
    checkedTagsList: List<TagModel>,
    onTagCheckChanged: (TagModel) -> Unit,
    isSearchedTagExist: Boolean,
    tagSearchText: String,
    maxTagLength: Int,
    maxTagChooseCount: Int,
    onTagSearchTextChanged: (String) -> Unit,
    onCreateTagClick: () -> Unit,
    isShowSearchedTagRetry: Boolean,
    onRetryClick: () -> Unit,
    isSearchedTagsLoadingState: Boolean,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.create_or_choose_tags),
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = AppTheme.colors.dark
            )
            Text(
                text = stringResource(id = R.string.max) + " $maxTagChooseCount",
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                color = AppTheme.colors.dark.copy(0.3f)
            )
        }
        Text(
            text = stringResource(id = R.string.max_tag_length) + " $maxTagLength",
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color = if (tagSearchText.length > maxTagLength) AppTheme.colors.error else AppTheme.colors.dark.copy(0.3f)
        )

        InputText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = tagSearchText,
            onValueChange = onTagSearchTextChanged,
            label = stringResource(id = R.string.search_tag),
            singleLine = true
        )
        CheckedTagsColumn(
            checkedTagsList = checkedTagsList,
            onTagCheckChanged = onTagCheckChanged,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        )
        if (isSearchedTagsLoadingState) {
            FullScreenLoading()
            return
        }
        if (isShowSearchedTagRetry) {
            RetryStub(onClick = { onRetryClick() })
            return
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            if (!isSearchedTagExist) {
                item {
                    NewTagItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        tagTitle = tagSearchText,
                        createTagOnClick = onCreateTagClick,
                        isButtonEnabled = tagSearchText.length <= maxTagLength
                    )
                    ItemsDivider()
                }
            }
            itemsIndexed(tags) { index, tagModel ->
                if (!checkedTagsList.any { it.id == tagModel.id }){
                    TagItem(
                        tagModel = tagModel,
                        checked = false,
                        onCheckChange = { onTagCheckChanged(tagModel) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 36.dp)
                    )
                    if (index < tags.lastIndex) {
                        ItemsDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemsDivider() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = AppTheme.colors.dark.copy(0.1f)
    )
}

@Composable
private fun CheckedTagsColumn(
    checkedTagsList: List<TagModel>,
    onTagCheckChanged: (TagModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        checkedTagsList.forEachIndexed { index, tagModel ->
            TagItem(
                tagModel = tagModel,
                checked = checkedTagsList.any { it.id == tagModel.id },
                onCheckChange = { onTagCheckChanged(tagModel) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 36.dp)
            )
            if (index < checkedTagsList.lastIndex) {
                ItemsDivider()
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = AppTheme.colors.accent
        )
    }
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    var tagSearchText by remember { mutableStateOf("") }
    val initList = remember {
        List(10) { TagModel("popular tag$it", "${-it}", 100, 52, isSubscribed = false) }
    }
    val tagList = remember { mutableStateListOf<TagModel>() }
    LaunchedEffect(key1 = true) {
        tagList.addAll(initList)
    }
    val checkedTagsList = remember { mutableStateListOf<TagModel>() }
    Step3ScreenView(
        tags = tagList,
        checkedTagsList = checkedTagsList,
        onTagCheckChanged = { id ->
            if (checkedTagsList.contains(id)) {
                checkedTagsList.remove(id)
            } else {
                checkedTagsList.add(id)
            }
        },
        isSearchedTagExist = false,
        tagSearchText = tagSearchText,
        onTagSearchTextChanged = { tagSearchText = it },
        onCreateTagClick = {},
        isShowSearchedTagRetry = false,
        onRetryClick = { },
        isSearchedTagsLoadingState = false,
        maxTagLength = 30,
        maxTagChooseCount = 3
    )
}

