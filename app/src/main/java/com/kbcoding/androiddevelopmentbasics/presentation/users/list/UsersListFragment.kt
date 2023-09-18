package com.kbcoding.androiddevelopmentbasics.presentation.users.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbcoding.androiddevelopmentbasics.core.extensions.factory
import com.kbcoding.androiddevelopmentbasics.core.extensions.navigator
import com.kbcoding.androiddevelopmentbasics.core.presentation.BaseFragment
import com.kbcoding.androiddevelopmentbasics.core.result.Result
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentUsersListBinding
import com.kbcoding.androiddevelopmentbasics.model.User

class UsersListFragment : BaseFragment<FragmentUsersListBinding>() {

    private lateinit var usersAdapter: UsersAdapter

    private val viewModel: UsersListViewModel by viewModels { factory() }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUsersListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUsersAdapter()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.users.observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is Result.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    usersAdapter.submitList(it.data)
                }

                is Result.Error -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }

                is Result.Pending -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Empty -> {
                    binding.noUsersTextView.visibility = View.VISIBLE
                }
            }
        }
        viewModel.actionShowDetails.observe(viewLifecycleOwner) {
            it.getValue()?.let { user -> navigator().showDetails(user) }
        }
        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageRes -> navigator().toast(messageRes) }
        }
    }

    private fun initUsersAdapter() {
        usersAdapter = UsersAdapter(viewModel)
//            UsersAdapter(object : UsersAdapter.UserActionListener {
//            override fun onUserMove(user: User, moveBy: Int) {
//                viewModel.moveUser(user, moveBy)
//            }
//
//            override fun onUserDelete(user: User) {
//                viewModel.deleteUser(user)
//            }
//
//            override fun onUserDetails(user: User) {
//                navigator().showDetails(user)
//            }
//
//            override fun onUserFire(user: User) {
//                viewModel.fireUser(user)
//            }
//        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
            val itemAnimator = itemAnimator
            if (itemAnimator is DefaultItemAnimator) {
                itemAnimator.supportsChangeAnimations = false
            }
        }
    }

    private fun hideAll() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
        binding.noUsersTextView.visibility = View.GONE
    }

}