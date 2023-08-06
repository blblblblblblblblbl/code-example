
@Composable
fun PhotoItem(
    imageData: UploadMediaModel,
    removeOnClick: () -> Unit,
    retryOnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        CoilImage(
            imageModel = { imageData.uri },
            modifier = Modifier
                .fillMaxSize()
                .conditional(
                    condition = (imageData.state == UploadState.FAILURE),
                    modifier = {
                        composed {
                            this.border(
                                shape = RoundedCornerShape(8.dp),
                                color = AppTheme.colors.error,
                                width = 2.dp
                            )
                        }
                    }
                ),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            imageLoader = {
                ImageLoader.Builder(LocalContext.current)
                    .components {
                        add(VideoFrameDecoder.Factory())
                    }
                    .build()
            },
            previewPlaceholder = R.drawable.ic_launcher
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f)))
        if (imageData.state == UploadState.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.Center),
                trackColor = AppTheme.colors.accent,
                color = AppTheme.colors.background
            )
        }
        if (imageData.state == UploadState.FAILURE) {
            IconButton(
                modifier = Modifier.fillMaxSize(0.4f),
                onClick = retryOnClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = "remove photo",
                    tint = AppTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .fillMaxSize(0.25f),
            onClick = removeOnClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close_circle),
                contentDescription = "remove photo",
                tint = AppTheme.colors.white,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSUCCESS() {
    PhotoItem(
        modifier = Modifier.size(64.dp),
        imageData = UploadMediaModel(
            uri = "",
            uuid = "",
            state = UploadState.SUCCESS,
            mediaType = MediaType.PHOTO
        ),
        retryOnClick = {},
        removeOnClick = {}
    )
}

@Preview
@Composable
private fun PreviewLOADING() {
    PhotoItem(
        modifier = Modifier.size(64.dp),
        imageData = UploadMediaModel(
            uri = "",
            uuid = "",
            state = UploadState.LOADING,
            mediaType = MediaType.PHOTO
        ),
        retryOnClick = {},
        removeOnClick = {}
    )
}

@Preview
@Composable
private fun PreviewFAILURE() {
    PhotoItem(
        modifier = Modifier.size(64.dp),
        imageData = UploadMediaModel(
            uri = "",
            uuid = "",
            state = UploadState.FAILURE,
            mediaType = MediaType.PHOTO
        ),
        retryOnClick = {},
        removeOnClick = {}
    )
}


