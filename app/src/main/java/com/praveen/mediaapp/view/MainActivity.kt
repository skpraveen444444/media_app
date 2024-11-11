package com.praveen.mediaapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.praveen.mediaapp.R
import com.praveen.mediaapp.databinding.ActivityMainBinding
import com.praveen.mediaapp.repository.MediaRepository
import com.praveen.mediaapp.viewmodel.MediaViewModel
import com.praveen.mediaapp.viewmodel.MediaViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MediaViewModel
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mediaRepository = MediaRepository()
        val viewModelFactory = MediaViewModelFactory(mediaRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MediaViewModel::class.java]


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
}