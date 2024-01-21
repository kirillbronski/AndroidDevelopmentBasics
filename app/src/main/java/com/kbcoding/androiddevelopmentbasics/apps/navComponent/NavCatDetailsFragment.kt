package com.kbcoding.androiddevelopmentbasics.apps.navComponent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.elveum.elementadapter.setTintColor
import com.kbcoding.androiddevelopmentbasics.BaseFragment
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentCatDetailsBinding
import com.kbcoding.androiddevelopmentbasics.viewModel.CatDetailsViewModel
import com.kbcoding.androiddevelopmentbasics.viewModel.base.assistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavCatDetailsFragment : BaseFragment<FragmentCatDetailsBinding>() {

    @Inject
    lateinit var factory: CatDetailsViewModel.Factory

    private val viewModel by assistedViewModel {
        val args = NavCatDetailsFragmentArgs.fromBundle(requireArguments())
        factory.create(args.catId)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCatDetailsBinding {
        return FragmentCatDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCatDetailsBinding.bind(view)
        viewModel.catLiveData.observe(viewLifecycleOwner) { cat ->
            binding.catNameTextView.text = cat.name
            binding.catDescriptionTextView.text = cat.description
            binding.catImageView.load(cat.photoUrl) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.circle)
            }
            binding.favoriteImageView.setImageResource(
                if (cat.isFavorite) R.drawable.ic_favorite
                else R.drawable.ic_favorite_not
            )
            binding.favoriteImageView.setTintColor(
                if (cat.isFavorite) R.color.highlighted_action
                else R.color.action
            )
        }

        binding.goBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.favoriteImageView.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }
}