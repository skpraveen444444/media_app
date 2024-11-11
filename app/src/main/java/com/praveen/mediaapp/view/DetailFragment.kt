package com.praveen.mediaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.praveen.mediaapp.R
import com.praveen.mediaapp.databinding.FragmentDetailBinding
import com.praveen.mediaapp.utils.RootDetectionUtil
import com.praveen.mediaapp.model.MediaItem as MediaItemData

class DetailFragment : Fragment() {

    private var binding: FragmentDetailBinding? = null
    private var exoPlayer: ExoPlayer? = null
    private lateinit var mediaItem: MediaItemData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Root detection logic
        if (RootDetectionUtil.isDeviceRooted(requireContext())) {
            Toast.makeText(
                requireContext(),
                "Rooted device detected. Some features may be disabled.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val toolbar = binding?.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        mediaItem = DetailFragmentArgs.fromBundle(requireArguments()).mediaItem

        displayMediaDetails(mediaItem)
        setupExoPlayer(mediaItem)
    }


    private fun displayMediaDetails(mediaItem: MediaItemData) {
        binding?.apply {
            title.text = mediaItem.trackName
            director.text = mediaItem.artistName
            genre.text = mediaItem.primaryGenreName
            Glide.with(requireContext())
                .load(mediaItem.artworkUrl100)
                .into(thumbnail)
            description.text = mediaItem.longDescription
        }
    }

    private fun setupExoPlayer(mediaItem: MediaItemData) {
        exoPlayer = ExoPlayer.Builder(requireContext()).build().apply {
            binding?.videoPlayer?.player = this
            val videoItem = MediaItem.Builder()
                .setUri(mediaItem.previewUrl)
                .build()
            setMediaItem(videoItem)
            prepare()
            playWhenReady = true // can be set false for manual play
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        exoPlayer = null
        binding = null

    }
}
