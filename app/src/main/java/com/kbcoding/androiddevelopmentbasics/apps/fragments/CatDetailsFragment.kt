package com.kbcoding.androiddevelopmentbasics.apps.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class CatDetailsFragment : BaseFragment<FragmentCatDetailsBinding>(), HasTitle {

    @Inject
    lateinit var factory: CatDetailsViewModel.Factory

    @Inject
    lateinit var router: FragmentRouter

    private val viewModel by assistedViewModel {
        val catId = requireArguments().getLong(ARG_CAT_ID)
        factory.create(catId)
    }

    override val title: String
        get() = getString(R.string.fragment_cat_details)

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCatDetailsBinding {
        return FragmentCatDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            router.goBack()
        }
        binding.favoriteImageView.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    companion object {

        private const val ARG_CAT_ID = "catId"

        fun newInstance(catId: Long): CatDetailsFragment {
            val args = Bundle()
            args.putLong(ARG_CAT_ID, catId)
            val fragment = CatDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}