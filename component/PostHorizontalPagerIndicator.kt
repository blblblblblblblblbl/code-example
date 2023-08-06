//modified code from com.google.accompanist:accompanist-pager-indicators:0.30.1
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostHorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = LocalContentColor.current,
    inactiveColor: Color = activeColor.copy(alpha = 0.38f),
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = indicatorWidth,
    indicatorShape: Shape = CircleShape,
) {
    val indicatorWidthPx = indicatorWidth.roundToPx
    val spacingPx = spacing.roundToPx

    Box(
        modifier = modifier, contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) {
                Box(
                    modifier = Modifier
                        .size(width = indicatorWidth, height = indicatorHeight)
                        .background(color = inactiveColor, shape = indicatorShape)
                        .conditional(it <= pagerState.currentPage) {
                            background(color = activeColor, shape = indicatorShape)
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffsetFraction
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceAtMost(pageCount - 1f)
                        .coerceAtLeast(0f)
                    IntOffset(
                        x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(), y = 0
                    )
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .conditional(pageCount > 0) {
                    background(
                        color = activeColor,
                        shape = indicatorShape,
                    )
                }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun Preview() {
    val pagerState = rememberPagerState()
    BoxWithConstraints(
        modifier = Modifier
            .requiredWidth(200.dp)
            .padding(16.dp)
    ) {
        val indicatorWidth = ((this.maxWidth - 8.dp) / 3)
        PostHorizontalPagerIndicator(
            modifier = Modifier.fillMaxWidth(),
            pageCount = 3,
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.primary,
            spacing = 4.dp,
            indicatorHeight = 4.dp,
            indicatorWidth = indicatorWidth
        )
    }
}

