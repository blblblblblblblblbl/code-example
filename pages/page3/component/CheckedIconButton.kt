
@Composable
fun CheckedIconButton(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val unCheckedBorderColor = AppTheme.colors.dark.copy(0.3f)
    val borderColor by animateColorAsState(targetValue = if (checked) Color.Transparent else unCheckedBorderColor)
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val iconSize by animateDpAsState(targetValue = if (checked) this.maxWidth else 0.dp)
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = R.drawable.ic_checked),
                contentDescription = "check button",
                tint = AppTheme.colors.accent

            )
        }
    }
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    var checked by remember { mutableStateOf(false) }
    CheckedIconButton(
        modifier = Modifier.size(24.dp),
        checked = checked,
        onClick = { checked = !checked })
}