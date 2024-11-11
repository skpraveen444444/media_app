package com.praveen.mediaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praveen.mediaapp.model.MediaItem
import com.praveen.mediaapp.repository.MediaRepository
import kotlinx.coroutines.launch

class MediaViewModel(private val repository: MediaRepository) : ViewModel() {

    private val _mediaResults = MutableLiveData<List<MediaItem>>()
    val mediaResults: LiveData<List<MediaItem>> = _mediaResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun searchMedia(term: String, mediaType: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchMedia(term, mediaType)
                _mediaResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}
