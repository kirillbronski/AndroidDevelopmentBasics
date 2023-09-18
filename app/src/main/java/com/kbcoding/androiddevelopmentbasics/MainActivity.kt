package com.kbcoding.androiddevelopmentbasics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.kbcoding.androiddevelopmentbasics.core.base.BaseFragment
import com.kbcoding.androiddevelopmentbasics.core.navigator.MainNavigator
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityMainBinding
import com.kbcoding.androiddevelopmentbasics.screens.hello.HelloFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navigator by viewModels<MainNavigator>() {
        ViewModelProvider.AndroidViewModelFactory(
            application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        if (savedInstanceState == null) {
            navigator.launchFragment(this, HelloFragment.Screen(), addToBackStack = false)
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        navigator.whenActivityActive.mainActivity = this
    }

    override fun onPause() {
        super.onPause()
        navigator.whenActivityActive.mainActivity = null
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                // more than 1 screen -> show back button in the toolbar
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }

            val result = navigator.result.value?.getValue() ?: return
            if (f is BaseFragment<*>) {
                // has result that can be delivered to the screen's view-model
                f.viewModel.onResult(result)
            }
        }
    }
}