
@Composable
fun Step1ScreenView(
    postText: String,
    maxTextLength: Int,
    galleryElements: List<UploadMediaModel>,
    onPostTextChanged: (String) -> Unit,
    addElement: () -> Unit,
    onRetryUploadClick: (String) -> Unit,
    removeElement: (String) -> Unit,
    moveItem: (from:Int, to:Int) -> Unit,
    canDragOver:(Int, Int) -> Boolean,
    maxPhotosCount: Int,
    maxVideosCount: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.your_post),
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = AppTheme.colors.dark
            )
            Text(
                text = "${postText.length}/$maxTextLength",
                fontFamily = AppTheme.typography.defaultFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                color = if (postText.length > maxTextLength) AppTheme.colors.error else AppTheme.colors.dark.copy(0.3f)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        InputText(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            value = postText,
            onValueChange = onPostTextChanged,
            label = stringResource(id = R.string.text_field_placeholder)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        )
        Text(
            text = stringResource(id = R.string.add_image),
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            color = AppTheme.colors.dark
        )
        Text(
            text = stringResource(id = R.string.max_post_create_gallery_elements, maxPhotosCount, maxVideosCount),
            fontFamily = AppTheme.typography.defaultFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color =  getLimitTextColor(galleryElements, maxPhotosCount, maxVideosCount)
        )
        PhotosGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            galleryElements = galleryElements,
            addElement = addElement,
            removeElement = removeElement,
            onRetryUploadClick = onRetryUploadClick,
            moveItem = moveItem,
            canDragOver = canDragOver
        )
    }
}
@Composable
private fun getLimitTextColor(
    elements: List<UploadMediaModel>,
    maxPhotosCount: Int,
    maxVideosCount: Int
): Color {
    val photosCount = elements.count { it.mediaType == MediaType.PHOTO }
    val videosCount = elements.count { it.mediaType == MediaType.VIDEO }
    return if (photosCount > maxPhotosCount || videosCount > maxVideosCount) {
        AppTheme.colors.error
    } else {
        AppTheme.colors.dark.copy(0.3f)
    }
}

@Preview(
    backgroundColor = 0xFFF9FBFF,
    showBackground = true
)
@Composable
private fun Preview() {
    var postText by remember { mutableStateOf("") }
    val photos = remember { mutableStateMapOf<String, UploadMediaModel>() }
    val photo = UploadMediaModel(
        uri = "",
        uuid = UUID.randomUUID().toString(),
        state = UploadState.FAILURE,
        mediaType = MediaType.PHOTO
    )
    Step1ScreenView(
        postText = postText,
        onPostTextChanged = { postText = it },
        maxTextLength = 300,
        galleryElements = photos.values.toList(),
        addElement = { photos.put(photo.uuid, photo) },
        onRetryUploadClick = {},
        removeElement = { photos.remove(photo.uuid) },
        maxPhotosCount = PostCreationViewModel.MAX_PHOTOS_COUNT,
        maxVideosCount = PostCreationViewModel.MAX_VIDEOS_COUNT,
        moveItem = { _, _ -> },
        canDragOver = { _, _ -> false }
    )
}

