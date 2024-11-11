package com.praveen.mediaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.praveen.mediaapp.R
import com.praveen.mediaapp.databinding.FragmentHomeBinding
import com.praveen.mediaapp.utils.RootDetectionUtil
import com.praveen.mediaapp.viewmodel.MediaViewModel

class HomeFragment : Fragment() {

    private val viewModel: MediaViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        clearSearchFields()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isDeviceRooted()) {
            showRootedDeviceWarning()
            return
        }

        setupSubmitButtonClickListener()
    }

    private fun isDeviceRooted(): Boolean {
        return RootDetectionUtil.isDeviceRooted(requireContext())
    }

    private fun showRootedDeviceWarning() {
        Toast.makeText(
            requireContext(),
            "Rooted device detected. Some features may be disabled.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun clearSearchFields() {
        binding.apply {
            searchTermInput.text.clear()
            mediaTypeGroup.clearCheck()
        }
    }

    private fun setupSubmitButtonClickListener() {
        binding.submitButton.setOnClickListener {
            changeButtonAppearance()
            val searchTerm = binding.searchTermInput.text.toString()
            val selectedMediaType = getSelectedMediaType()

            if (searchTerm.isNotEmpty()) {
                viewModel.searchMedia(searchTerm, selectedMediaType)
                navigateToITunesFragment()
            } else {
                showEmptySearchTermWarning()
            }
        }
    }

    private fun changeButtonAppearance() {
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)

        binding.submitButton.apply {
            setBackgroundColor(whiteColor)
            setTextColor(blackColor)
        }
    }

    private fun getSelectedMediaType(): String {
        val selectedRadioButtonId = binding.mediaTypeGroup.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.podcast -> "podcast"
            R.id.radioMovie -> "movie"
            R.id.radioMusicVideo -> "musicVideo"
            R.id.radioSong -> "music"
            else -> ""
        }
    }

    private fun navigateToITunesFragment() {
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentITunes)
    }

    private fun showEmptySearchTermWarning() {
        Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
    }
}
