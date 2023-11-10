package com.kbcoding.androiddevelopmentbasics

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.kbcoding.androiddevelopmentbasics.contract.CustomAction
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomAction
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomTitle
import com.kbcoding.androiddevelopmentbasics.contract.Navigator
import com.kbcoding.androiddevelopmentbasics.contract.ResultListener
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityMainBinding
import com.kbcoding.androiddevelopmentbasics.fragments.AboutFragment
import com.kbcoding.androiddevelopmentbasics.fragments.BoxFragment
import com.kbcoding.androiddevelopmentbasics.fragments.BoxSelectionFragment
import com.kbcoding.androiddevelopmentbasics.fragments.MenuFragment
import com.kbcoding.androiddevelopmentbasics.fragments.OptionsFragment
import java.io.Serializable

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var currentFragment: Fragment? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.fc_container) as NavHostFragment
        navController = navHost.navController


        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateUi()
        return true
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()

    override fun showBoxSelectionScreen(options: Options) {
        launchDestination(R.id.boxSelectionFragment, BoxSelectionFragment.createArgs(options))
    }

    override fun showOptionsScreen(options: Options) {
        launchDestination(R.id.optionsFragment, OptionsFragment.createArgs(options))
    }

    override fun showAboutScreen() {
        launchDestination(R.id.aboutFragment)
    }

    override fun showCongratulationsScreen() {
        launchDestination(R.id.boxFragment)
    }

    override fun goBack() {
        navController.popBackStack()
    }

    override fun goToMenu() {
        navController.popBackStack(R.id.menuFragment, false)
    }

    override fun <T : Serializable> publishResult(result: T) {
        navController.previousBackStackEntry?.savedStateHandle?.set(KEY_RESULT, result)
    }


    override fun <T : Serializable> listenResult(
        owner: LifecycleOwner, listener: ResultListener<T>
    ) {
        val liveData =
            navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(KEY_RESULT)
        liveData?.observe(owner) { result ->
            if (result != null) {
                listener(result)
                liveData.value = null
            }
        }
    }

    private fun launchDestination(destinationId: Int, args: Bundle? = null) {
        navController.navigate(destinationId, args, navOptions {
            anim {
                enter = R.anim.slide_in
                exit = R.anim.fade_out
                popEnter = R.anim.fade_in
                popExit = R.anim.slide_out
            }
        })
    }

    private fun updateUi() {
        val fragment = currentFragment

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = getString(R.string.fragment_navigation_example)
        }

        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        binding.toolbar.menu.clear() // clearing old action if it exists before assigning a new one

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
        iconDrawable.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }
    }

    companion object {
        @JvmStatic
        private val KEY_RESULT = "RESULT"
    }
}