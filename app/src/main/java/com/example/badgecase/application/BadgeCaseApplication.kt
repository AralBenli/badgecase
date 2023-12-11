package com.example.badgecase.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by AralBenli on 8.12.2023.
 */

@HiltAndroidApp
class BadgeCaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}