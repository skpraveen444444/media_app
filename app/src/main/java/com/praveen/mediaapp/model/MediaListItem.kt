package com.praveen.mediaapp.model

sealed class MediaListItem {
    data class Header(val kind: String) : MediaListItem()
    data class Item(val mediaItem: MediaItem) : MediaListItem()
}
