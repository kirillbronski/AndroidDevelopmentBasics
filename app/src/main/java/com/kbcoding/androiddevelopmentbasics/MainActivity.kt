package com.kbcoding.androiddevelopmentbasics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityMainBinding
import com.kbcoding.androiddevelopmentbasics.presentation.users.list.UsersListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fc_main_container, UsersListFragment())
                .commit()

        }
    }

}