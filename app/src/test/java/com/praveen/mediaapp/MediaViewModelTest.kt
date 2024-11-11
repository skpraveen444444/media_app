package com.praveen.mediaapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.praveen.mediaapp.repository.MediaRepository
import com.praveen.mediaapp.model.MediaItem
import com.praveen.mediaapp.viewmodel.MediaViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After

@ExperimentalCoroutinesApi
class MediaViewModelTest {

    private lateinit var viewModel: MediaViewModel

    @Mock
    private lateinit var mediaRepository: MediaRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Ensures LiveData works in unit tests

    @Before
    fun setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this)

        // Set the Main dispatcher to the TestCoroutineDispatcher
        Dispatchers.setMain(testDispatcher)

        viewModel = MediaViewModel(mediaRepository)
    }

    @Test
    fun testSearchMediaSuccess() = runTest {
        // Mocking the repository to return a list of media items
        val mockResults = listOf(
            MediaItem(trackName = "Song 1"),
            MediaItem(trackName = "Song 2")
        )

        Mockito.`when`(mediaRepository.searchMedia("pop", "")).thenReturn(mockResults)

        // Simple observer to check LiveData results
        val observer = Observer<List<MediaItem>> { results ->
            assertNotNull(results)
            assertEquals(2, results.size)
            assertEquals("Song 1", results[0].trackName)
            assertEquals("Song 2", results[1].trackName)
        }

        // Observe the LiveData
        viewModel.mediaResults.observeForever(observer)

        viewModel.searchMedia("pop", "")

        // Let the coroutine run
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the repository call
        Mockito.verify(mediaRepository).searchMedia("pop", "")

        // Assert that the LiveData value is correct
        assertEquals(mockResults, viewModel.mediaResults.value)

        // Clean up: remove observer
        viewModel.mediaResults.removeObserver(observer)
    }

    @Test
    fun testSearchMediaError() = runTest {
        // Mock repository to throw an error
        val errorMessage = "Network error"
        Mockito.`when`(mediaRepository.searchMedia("pop", ""))
            .thenThrow(RuntimeException(errorMessage))

        // Simple observer to check LiveData for error messages
        val observer = Observer<String> { error ->
            assertNotNull(error)
            assertEquals(errorMessage, error)
        }

        // Observe error message LiveData
        viewModel.errorMessage.observeForever(observer)

        // Trigger search
        viewModel.searchMedia("pop", "")

        // Let the coroutine run
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify repository call and check error message
        Mockito.verify(mediaRepository).searchMedia("pop", "")
        assertEquals(errorMessage, viewModel.errorMessage.value)

        // Clean up: remove observer
        viewModel.errorMessage.removeObserver(observer)
    }

    @After
    fun tearDown() {
        // Reset dispatcher after tests
        Dispatchers.resetMain()
        testDispatcher.scheduler.advanceUntilIdle()
    }
}
