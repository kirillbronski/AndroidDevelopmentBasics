package com.kbcoding.androiddevelopmentbasics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kbcoding.androiddevelopmentbasics.R

/**
 * Entry point of the app.
 *
 * Splash activity contains only window background, all other initialization logic is placed to
 * [SplashFragment] and [SplashViewModel].
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}