@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBar(
    pagerState: PagerState,
    pageCount: Int,
    backOnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "back",
                tint = AppTheme.colors.accent,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = { backOnClick() }
                )
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = AppTheme.typography.defaultFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = AppTheme.colors.dark
                        )
                    ) {
                        append(stringResource(id = R.string.new_post) + "\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = AppTheme.typography.defaultFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.W500,
                            color = AppTheme.colors.dark
                        )
                    ) {
                        append(stringResource(id = R.string.step) + " ${pagerState.currentPage + 1}")
                    }
                },
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.requiredWidth(24.dp))
        }
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            val indicatorWidth = (this.maxWidth - 8.dp) / 3
            PostHorizontalPagerIndicator(
                modifier = Modifier.fillMaxWidth(),
                pageCount = pageCount,
                pagerState = pagerState,
                activeColor = AppTheme.colors.accent,
                inactiveColor = AppTheme.colors.dark.copy(0.2f),
                spacing = 4.dp,
                indicatorHeight = 4.dp,
                indicatorWidth = indicatorWidth
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(
    backgroundColor = 0xFF_F9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    val pagerState = rememberPagerState()
    TopBar(
        pagerState = pagerState,
        pageCount = 3,
        backOnClick = {}
    )
}
