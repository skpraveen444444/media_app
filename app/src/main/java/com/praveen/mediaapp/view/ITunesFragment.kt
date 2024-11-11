package com.praveen.mediaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.praveen.mediaapp.model.MediaListItem
import com.praveen.mediaapp.R
import com.praveen.mediaapp.adapter.MediaAdapter
import com.praveen.mediaapp.databinding.FragmentITunesBinding
import com.praveen.mediaapp.utils.RootDetectionUtil
import com.praveen.mediaapp.viewmodel.MediaViewModel

class ITunesFragment : Fragment() {

    private val viewModel: MediaViewModel by activityViewModels()
    private var _binding: FragmentITunesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaAdapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentITunesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        // Root detection logic
        if (RootDetectionUtil.isDeviceRooted(requireContext())) {
            Toast.makeText(
                requireContext(),
                "Rooted device detected. Some features may be disabled.",
                Toast.LENGTH_LONG
            ).show()
            return
        } else {
            initRecyclerView()
            initTabLayout()
            observeViewModel()
        }
    }

    private fun initRecyclerView() {
        mediaAdapter = MediaAdapter(emptyList())
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mediaAdapter.getItemViewType(position)) {
                    MediaAdapter.ITEM_TYPE_HEADER -> gridLayoutManager.spanCount
                    MediaAdapter.ITEM_TYPE_ITEM -> 1
                    else -> 1
                }
            }
        }

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = mediaAdapter
        }
    }


    private fun initTabLayout() {
        binding.tabLayout.apply {
            addTab(newTab().setText("Grid"))
            addTab(newTab().setText("List"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> setRecyclerViewLayoutManager(isGrid = true)
                        1 -> setRecyclerViewLayoutManager(isGrid = false)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }



    private fun setRecyclerViewLayoutManager(isGrid: Boolean) {
        if (isGrid) {
            val gridLayoutManager = GridLayoutManager(requireContext(), 3)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (mediaAdapter.getItemViewType(position)) {
                        MediaAdapter.ITEM_TYPE_HEADER -> gridLayoutManager.spanCount
                        MediaAdapter.ITEM_TYPE_ITEM -> 1
                        else -> 1
                    }
                }
            }
            binding.recyclerView.layoutManager = gridLayoutManager
        } else {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        mediaAdapter.changeOrientation(isGrid)
        binding.recyclerView.adapter = mediaAdapter
    }


    private fun observeViewModel() {
        viewModel.mediaResults.observe(viewLifecycleOwner) { results ->
            if (results != null) {
                val filteredResults = results.filter { it.kind != null }
                val groupedResults = filteredResults.groupBy { it.kind }
                val sectionedList = mutableListOf<MediaListItem>()
                groupedResults.forEach { (kind, items) ->
                    kind?.let { MediaListItem.Header(it) }
                        ?.let { sectionedList.add(it) }
                    items.forEach { mediaItem ->
                        sectionedList.add(MediaListItem.Item(mediaItem))
                    }

                    mediaAdapter = MediaAdapter(sectionedList)
                    binding.recyclerView.adapter = mediaAdapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }

}

