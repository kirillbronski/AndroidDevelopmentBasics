package com.kbcoding.androiddevelopmentbasics.apps.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbcoding.androiddevelopmentbasics.CatsAdapterListener
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.catsAdapter
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentCatsListBinding
import com.kbcoding.androiddevelopmentbasics.viewModel.CatListItem
import com.kbcoding.androiddevelopmentbasics.viewModel.CatsViewModel
import dagger.hilt.android.AndroidEntryPoint

    @AndroidEntryPoint
    class CatsListActivity : AppCompatActivity(), CatsAdapterListener {

        private val viewModel by viewModels<CatsViewModel>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val binding = FragmentCatsListBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val adapter = catsAdapter(this)
            binding.catsRecyclerView.layoutManager = LinearLayoutManager(this)
            (binding.catsRecyclerView.itemAnimator as? DefaultItemAnimator)
                ?.supportsChangeAnimations = false
            binding.catsRecyclerView.adapter = adapter
            viewModel.catsLiveData.observe(this) {
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
            val intent = Intent(this, CatDetailsActivity::class.java)
            intent.putExtra(CatDetailsActivity.EXTRA_CAT_ID, cat.id)
            startActivity(intent)
        }

    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_fragments)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }