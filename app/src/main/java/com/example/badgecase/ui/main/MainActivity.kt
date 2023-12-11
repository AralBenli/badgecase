package com.example.badgecase.ui.main

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.badgecase.R
import com.example.badgecase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
/**
 * Created by AralBenli on 8.12.2023.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }


    private fun initViews() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun statusBarTitle(isVisible: Boolean, title: String) {
        with(binding) {
            if (isVisible) {
                pageTitle.visibility = View.VISIBLE
                pageTitle.text = title
            } else {
                pageTitle.visibility = View.GONE
            }
        }
    }
}

