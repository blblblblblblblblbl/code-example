
@Composable
fun NewTagItem(
    tagTitle: String,
    createTagOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    isButtonEnabled: Boolean = true
) {
    ConstraintLayout(modifier) {
        val (title, icon, count, createButton) = createRefs()
        Text(
            text = tagTitle,
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            color = AppTheme.colors.dark.copy(0.5f),
            modifier = Modifier.constrainAs(title) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    bias = 0f
                )
                centerVerticallyTo(parent)
                width = Dimension.preferredWrapContent
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(title.end, 8.dp)
                centerVerticallyTo(parent)
            },
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = "users_count",
            tint = AppTheme.colors.dark.copy(0.3f)
        )
        Text(
            modifier = Modifier
                .constrainAs(count) {
                    start.linkTo(icon.end, 2.dp)
                    centerVerticallyTo(parent)
                },
            text = "0k",
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color = AppTheme.colors.dark.copy(0.5f)
        )
        CreateTagButton(
            onClick = createTagOnClick,
            modifier = Modifier.constrainAs(createButton) {
                linkTo(
                    start = count.end,
                    end = parent.end,
                    bias = 1f,
                    startMargin = 8.dp
                )
                centerVerticallyTo(parent)
            },
            isEnabled = isButtonEnabled
        )
    }
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    NewTagItem(
        tagTitle = "Misteryos",
        createTagOnClick = {},
        modifier = Modifier.requiredWidth(300.dp)
    )
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun PreviewBigText() {
    NewTagItem(
        tagTitle = "123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_",
        createTagOnClick = {}
    )
}