
@Composable
fun PhotosGrid(
    galleryElements: List<UploadMediaModel>,
    addElement: () -> Unit,
    removeElement: (String) -> Unit,
    moveItem: (from:Int, to:Int) -> Unit,
    canDragOver:(Int, Int) -> Boolean,
    onRetryUploadClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val hapticFeedback = LocalHapticFeedback.current
    val state = rememberReorderableLazyGridState(
        onMove = { from, to -> moveItem(from.index, to.index) },
        onDragStart = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        canDragOver = { from, to ->
            canDragOver(from.index, to.index)
        }
    )
    LazyVerticalGrid(
        modifier = modifier.reorderable(state),
        columns = GridCells.Adaptive(minSize = 64.dp),
        state = state.gridState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = galleryElements, key = { it.uuid }) { photo ->
            ReorderableItem(reorderableState = state, key = photo.uuid) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                PhotoItem(
                    modifier = Modifier
                        .requiredSize(64.dp)
                        .shadow(elevation.value)
                        .aspectRatio(1f)
                        .detectReorderAfterLongPress(state),
                    imageData = photo,
                    retryOnClick = { onRetryUploadClick(photo.uuid) },
                    removeOnClick = { removeElement(photo.uuid) }
                )
            }
        }
        item {
            AddPictureButton(
                modifier = Modifier.requiredSize(64.dp),
                onClick = addElement
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
    val photos = remember { mutableStateMapOf<String, UploadMediaModel>() }
    val photo = UploadMediaModel(
        uri = "",
        uuid = UUID.randomUUID().toString(),
        state = UploadState.FAILURE,
        mediaType = MediaType.PHOTO
    )

    PhotosGrid(
        modifier = Modifier.requiredSize(width = 300.dp, height = 200.dp),
        galleryElements = photos.values.toList(),
        addElement = { photos.put(photo.uuid, photo) },
        onRetryUploadClick = {},
        removeElement = { photos.remove(photo.uuid) },
        moveItem = { _, _ -> },
        canDragOver = { _, _ -> false }
    )
}
