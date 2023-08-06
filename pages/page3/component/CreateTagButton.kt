
@Composable
fun CreateTagButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (isEnabled) AppTheme.colors.accent else AppTheme.colors.accent.copy(alpha = 0.5f)
            )
            .clickable(
                enabled = isEnabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = AppTheme.colors.dark),
                onClick = onClick
            )
    ) {
        Text(
            text = stringResource(id = R.string.create_tag),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 10.sp,
            color = AppTheme.colors.white
        )
    }
    InstructionsPlacementController(InstructionsDestinations.CREATE_TAG)
}


@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun PreviewEnabled() {
    CreateTagButton(
        onClick = {}
    )
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun PreviewDisabled() {
    CreateTagButton(
        onClick = {},
        isEnabled = false
    )
}