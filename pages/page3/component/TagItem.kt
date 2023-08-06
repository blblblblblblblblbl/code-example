
@Composable
fun TagItem(
    tagModel: TagModel,
    checked: Boolean,
    onCheckChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier) {
        val (title, icon, count, checkButton) = createRefs()
        Text(
            text = tagModel.title,
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            color = AppTheme.colors.dark,
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
                }
                .padding(start = 2.dp),
            text = shortThousands(tagModel.subscribersCount),
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color = AppTheme.colors.dark
        )
        CheckedIconButton(
            modifier = Modifier
                .size(24.dp)
                .constrainAs(checkButton) {
                    linkTo(
                        start = count.end,
                        end = parent.end,
                        bias = 1f,
                        startMargin = 8.dp
                    )
                    centerVerticallyTo(parent)
                },
            checked = checked,
            onClick = onCheckChange
        )
    }
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    var checked by remember { mutableStateOf(false) }
    TagItem(
        modifier = Modifier.fillMaxWidth(),
        tagModel = TagModel(
            title = "SaveTheEarth",
            subscribersCount = 12600,
            id = "1",
            pathwayCount = 52,
            isSubscribed = true
        ),
        checked = checked,
        onCheckChange = { checked = !checked })
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun PreviewBigText() {
    var checked by remember { mutableStateOf(false) }
    TagItem(
        modifier = Modifier.fillMaxWidth(),
        tagModel = TagModel(
            title = "123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_123456789_",
            subscribersCount = 12600,
            id = "1",
            pathwayCount = 52,
            isSubscribed = true
        ),
        checked = checked,
        onCheckChange = { checked = !checked })
}