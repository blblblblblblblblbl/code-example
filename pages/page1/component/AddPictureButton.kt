@Composable
fun AddPictureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .dashedBorder(
                width = 2.dp,
                off = 8.dp,
                on = 9.dp,
                color = AppTheme.colors.accent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        color = AppTheme.colors.dark.copy(0.05f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = "gallery",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(
    backgroundColor = 0xFF_F9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    AddPictureButton(
        modifier = Modifier.size(64.dp),
        onClick = {}
    )
}
