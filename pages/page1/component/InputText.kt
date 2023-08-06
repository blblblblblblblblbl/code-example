@Composable
fun InputText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { isFocused = it.isFocused },
        textStyle = TextStyle(
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            color = AppTheme.colors.dark
        ),
        label = {
            Text(
                text = label,
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = if (isFocused || value.isNotEmpty()) 12.sp else 16.sp,
                color = AppTheme.colors.dark.copy(0.3f)
            )
        },
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AppTheme.colors.dark.copy(0.05f),
            unfocusedContainerColor = AppTheme.colors.dark.copy(0.05f),
            disabledContainerColor = AppTheme.colors.dark.copy(0.05f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = AppTheme.colors.dark.copy(0.3f),
            cursorColor = AppTheme.colors.accent
        ),
        shape = RoundedCornerShape(8.dp),
    )
}

@Preview(
    backgroundColor = 0xFFF9FBFF, showBackground = true
)
@Composable
private fun Preview() {
    var value by remember { mutableStateOf("") }
    InputText(
        modifier = Modifier.requiredHeight(300.dp),
        value = value,
        onValueChange = { value = it },
        label = "Write what you want to tell everyone"
    )
}