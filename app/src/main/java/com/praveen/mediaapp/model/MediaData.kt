package com.praveen.mediaapp.model

import java.io.Serializable

data class MediaData(
    val resultCount: Long? = null,
    val results: List<MediaItem>? = null
) : Serializable

data class MediaItem(
    val wrapperType: String? = null,
    var kind: String? = null,

    val collectionId: Long? = null,
    val trackId: Long? = null,
    val artistName: String? = null,

    val collectionName: String? = null,
    val trackName: String? = null,
    val collectionCensoredName: String? = null,
    val trackCensoredName: String? = null,

    val collectionArtistId: Long? = null,
    val collectionArtistViewUrl: String? = null,
    val collectionViewUrl: String? = null,
    val trackViewUrl: String? = null,
    val previewUrl: String? = null,
    val artworkUrl30: String? = null,
    val artworkUrl60: String? = null,
    val artworkUrl100: String? = null,

    val collectionPrice: Double? = null,
    val trackPrice: Double? = null,
    val trackRentalPrice: Double? = null,
    val collectionHdPrice: Double? = null,
    val trackHdPrice: Double? = null,
    val trackHdRentalPrice: Double? = null,

    val releaseDate: String? = null,
    val collectionExplicitness: String? = null,
    val trackExplicitness: String? = null,

    val discCount: Long? = null,
    val discNumber: Long? = null,
    val trackCount: Long? = null,
    val trackNumber: Long? = null,

    val trackTimeMillis: Long? = null,
    val country: String? = null,
    val currency: String? = null,
    val primaryGenreName: String? = null,

    val contentAdvisoryRating: String? = null,
    val shortDescription: String? = null,
    val longDescription: String? = null,

    val hasItunesExtras: Boolean? = null,
    val artistId: Long? = null,
    val artistViewUrl: String? = null,
    val isStreamable: Boolean? = null,
    val collectionArtistName: String? = null
) : Serializable
