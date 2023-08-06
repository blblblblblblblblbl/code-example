@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCreationScreenView(
    step1Content: @Composable () -> Unit,
    step2Content: @Composable () -> Unit,
    step3Content: @Composable () -> Unit,
    isNextButtonEnabled: Boolean,
    onPageChanged: (newPageIndex: Int) -> Unit,
    onBackClick: () -> Unit,
    postCreateOnClick: () -> Unit
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { pageIndex -> onPageChanged(pageIndex) }
    }

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        topBar = {
            TopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                pagerState = pagerState,
                pageCount = PAGES_COUNT,
                backOnClick = {
                    if (pagerState.currentPage > 0) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    } else {
                        onBackClick()
                    }
                }
            )
        },
        bottomBar = {
            LoginButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
                enabled = isNextButtonEnabled,
                text = if (pagerState.currentPage < PAGES_COUNT - 1) {
                    stringResource(id = R.string.next)
                } else {
                    stringResource(id = R.string.publish)
                },
                onClick = {
                    if (pagerState.currentPage < PAGES_COUNT - 1) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        postCreateOnClick()
                    }
                }
            )
        }
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding() + 32.dp,
                    bottom = it.calculateBottomPadding()
                ),
            pageCount = PAGES_COUNT,
            state = pagerState,
            verticalAlignment = Alignment.Top,
            userScrollEnabled = false
        ) { position ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (position) {
                    0 -> step1Content()
                    1 -> step2Content()
                    2 -> step3Content()
                }
            }
        }
    }
    if (pagerState.currentPage > 0) {
        BackHandler {
            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    PostCreationScreenView(
        step1Content = { PreviewFullScreenStub("Step1Screen") },
        step2Content = { PreviewFullScreenStub("Step2Screen") },
        step3Content = { PreviewFullScreenStub("Step3Screen") },
        postCreateOnClick = {},
        isNextButtonEnabled = true,
        onBackClick = {},
        onPageChanged = {}
    )
}

private const val PAGES_COUNT = 3
