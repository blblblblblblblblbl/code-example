
private val categoryIconResolver = CategoryIconResolver()
private val titleResolver = CategoryTitleResolver()

@Composable
fun CategoryItem(
    category: CategoryModel,
    checked: Boolean,
    onCheckChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) AppTheme.colors.accent else AppTheme.colors.dark.copy(0.05f),
        animationSpec = tween(AnimationColorDuration, easing = LinearEasing)
    )
    val iconColor by animateColorAsState(
        targetValue = if (checked) AppTheme.colors.white else AppTheme.colors.dark.copy(0.5f),
        animationSpec = tween(AnimationColorDuration, easing = LinearEasing)
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = modifier
                .clip(shape = CircleShape)
                .background(color = backgroundColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = AppTheme.colors.accent),
                    onClick = onCheckChange
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(categoryIconResolver.resolve(category.iconId)),
                contentDescription = category.title,
                modifier = Modifier.fillMaxSize(0.5f),
                tint = iconColor
            )
        }
        Text(
            text = stringResource(id = titleResolver.resolve(category.title)),
            fontSize = 12.sp,
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            color = AppTheme.colors.dark,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

private const val AnimationColorDuration = 250

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview48() {
    var checked by remember { mutableStateOf(false) }
    val category = CategoryModel(
        iconId = "nature_icon",
        title = "Nature",
        id = "1",
        subscribersCount = 0,
        isSubscribed = true,
        pathwayCount = 3
    )
    CategoryItem(
        category = category,
        checked = checked,
        onCheckChange = { checked = !checked },
        modifier = Modifier.size(48.dp)
    )
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview96() {
    var checked by remember { mutableStateOf(false) }
    val category = CategoryModel(
        iconId = "nature_icon",
        title = "Nature",
        id = "1",
        subscribersCount = 0,
        isSubscribed = true,
        pathwayCount = 3
    )
    CategoryItem(
        category = category,
        checked = checked,
        onCheckChange = { checked = !checked },
        modifier = Modifier.size(96.dp)
    )
}
