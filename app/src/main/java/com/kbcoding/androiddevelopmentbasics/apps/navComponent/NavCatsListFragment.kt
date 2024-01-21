package com.kbcoding.androiddevelopmentbasics.apps.navComponent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.CatsAdapterListener
import com.kbcoding.androiddevelopmentbasics.catsAdapter
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentCatsListBinding
import com.kbcoding.androiddevelopmentbasics.viewModel.CatListItem
import com.kbcoding.androiddevelopmentbasics.viewModel.CatsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavCatsListFragment : BaseFragment<FragmentCatsListBinding>(), CatsAdapterListener {

    private val viewModel by viewModels<CatsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCatsListBinding {
        return FragmentCatsListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = catsAdapter(this)
        binding.catsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        (binding.catsRecyclerView.itemAnimator as? DefaultItemAnimator)
            ?.supportsChangeAnimations = false
        binding.catsRecyclerView.adapter = adapter
        viewModel.catsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCatDelete(cat: CatListItem.Cat) {
        viewModel.deleteCat(cat)
    }

    override fun onCatToggleFavorite(cat: CatListItem.Cat) {
        viewModel.toggleFavorite(cat)
    }

    override fun onCatChosen(cat: CatListItem.Cat) {
        val direction = NavCatsListFragmentDirections
            .actionNavCatsListFragmentToNavCatDetailsFragment(cat.id)
        findNavController().navigate(direction)
    }

}