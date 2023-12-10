package com.kbcoding.androiddevelopmentbasics.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbcoding.androiddevelopmentbasics.Repositories
import com.kbcoding.androiddevelopmentbasics.databinding.ActivityMainBinding
import com.kbcoding.androiddevelopmentbasics.adapters.DefaultLoadStateAdapter
import com.kbcoding.androiddevelopmentbasics.adapters.TryAgainAction
import com.kbcoding.androiddevelopmentbasics.adapters.UsersAdapter
import com.kbcoding.androiddevelopmentbasics.simpleScan
import com.kbcoding.androiddevelopmentbasics.viewModelCreator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder

    private val viewModel by viewModelCreator { MainViewModel(Repositories.usersRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Repositories.init(applicationContext)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUsersList()
        setupSearchInput()
        setupSwipeToRefresh()
        setupEnableErrorsCheckBox()
    }

    private fun setupUsersList() {
        val adapter = UsersAdapter()

        // in case of loading errors this callback is called when you tap the 'Try Again' button
        val tryAgainAction: TryAgainAction = { adapter.retry() }

        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)

        // combined adapter which shows both the list of users + footer indicator when loading pages
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usersRecyclerView.adapter = adapterWithLoadState
        (binding.usersRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )

        observeUsers(adapter)
        observeLoadState(adapter)

        handleScrollingToTopWhenSearching(adapter)
        handleListVisibility(adapter)
    }

    private fun setupSearchInput() {
        binding.searchEditText.addTextChangedListener {
            viewModel.setSearchBy(it.toString())
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeUsers(adapter: UsersAdapter) {
        lifecycleScope.launch {
            viewModel.usersFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeLoadState(adapter: UsersAdapter) {
        // you can also use adapter.addLoadStateListener
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                // main indicator in the center of the screen
                mainLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun handleScrollingToTopWhenSearching(adapter: UsersAdapter) = lifecycleScope.launch {
        // list should be scrolled to the 1st item (index = 0) if data has been reloaded:
        // (prev state = Loading, current state = NotLoading)
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    binding.usersRecyclerView.scrollToPosition(0)
                }
            }
    }

    private fun handleListVisibility(adapter: UsersAdapter) = lifecycleScope.launch {
        // list should be hidden if an error is displayed OR if items are being loaded after the error:
        // (current state = Error) OR (prev state = Error)
        //   OR
        // (before prev state = Error, prev state = NotLoading, current state = Loading)
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.usersRecyclerView.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun getRefreshLoadStateFlow(adapter: UsersAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    // ----

    private fun setupEnableErrorsCheckBox() {
        lifecycleScope.launch {
            viewModel.isErrorsEnabled.collectLatest {
                binding.errorCheckBox.isChecked = it
            }
        }
        binding.errorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEnableErrors(isChecked)
        }
    }
}