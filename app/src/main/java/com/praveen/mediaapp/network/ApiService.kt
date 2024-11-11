package com.praveen.mediaapp.network

import com.praveen.mediaapp.model.MediaData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    suspend fun searchMedia(
        @Query("term") term: String,
        @Query("media") media: String? = null,
        @Query("country") country: String? = null,
        @Query("limit") limit: Int? = null,
    ): Response<MediaData>
}