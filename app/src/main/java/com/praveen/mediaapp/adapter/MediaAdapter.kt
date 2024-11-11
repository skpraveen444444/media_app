package com.praveen.mediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.praveen.mediaapp.model.MediaListItem
import com.praveen.mediaapp.databinding.ItemHeaderBinding
import com.praveen.mediaapp.databinding.ItemMediaBinding
import com.praveen.mediaapp.view.ITunesFragmentDirections

class MediaAdapter(private var mediaList: List<MediaListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isGridMode = true

    fun changeOrientation(isGrid: Boolean) {
        if (isGridMode != isGrid) {
            isGridMode = isGrid
            notifyItemRangeChanged(0, itemCount)
        }
    }

    // ViewHolder for MediaItem
    inner class MediaViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: MediaListItem.Item) {
            val mediaData = mediaItem.mediaItem
            binding.cardLayout.orientation =
                if (isGridMode) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
            binding.trackNameTextView.text = mediaData.trackName

            Glide.with(binding.root.context)
                .load(mediaData.artworkUrl100)
                .into(binding.artworkImageView)

            binding.root.setOnClickListener {
                val action = ITunesFragmentDirections
                    .actionFragmentITunesToFragmentDetail(mediaData)
                it.findNavController().navigate(action)
            }
        }
    }

    // ViewHolder for Header
    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: MediaListItem.Header) {
            binding.headerTextView.text = header.kind
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mediaList[position]) {
            is MediaListItem.Header -> ITEM_TYPE_HEADER
            is MediaListItem.Item -> ITEM_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> {
                val binding =
                    ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }

            ITEM_TYPE_ITEM -> {
                val binding =
                    ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MediaViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = mediaList[position]) {
            is MediaListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is MediaListItem.Item -> (holder as MediaViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = mediaList.size

    companion object {
        const val ITEM_TYPE_HEADER = 0
        const val ITEM_TYPE_ITEM = 1
    }
}






