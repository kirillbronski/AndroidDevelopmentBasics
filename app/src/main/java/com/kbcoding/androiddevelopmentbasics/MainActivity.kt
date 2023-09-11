package com.kbcoding.androiddevelopmentbasics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityMainBinding
import com.kbcoding.androiddevelopmentbasics.model.UsersListener
import com.kbcoding.androiddevelopmentbasics.model.UsersService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usersAdapter: UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        initUsersAdapter()
        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private fun initUsersAdapter() {
        usersAdapter = UsersAdapter()
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = usersAdapter
        }
    }

    private val usersListener: UsersListener = {
        usersAdapter.usersList = it
    }
}