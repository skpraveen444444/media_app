package com.praveen.mediaapp.repository

import com.praveen.mediaapp.model.MediaItem
import com.praveen.mediaapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MediaRepository {

    private val apiService = RetrofitClient.instance

    suspend fun searchMedia(term: String, mediaType: String): List<MediaItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchMedia(term, media = mediaType)
                if (response.isSuccessful) {
                    response.body()?.results ?: throw Exception("No data found")
                } else {
                    throw Exception("API Error: ${response.code()}")
                }

            } catch (e: Exception) {
                throw e  // Propagate the exception to be handled by the ViewModel or higher level
            }
        }
    }
}

