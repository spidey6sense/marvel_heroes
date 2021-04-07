package becker.alexey.marvelheroes.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import becker.alexey.marvelheroes.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {
    private lateinit var mHeroesAdapter: HeroesAdapter
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        initViews()
    }

    companion object {
        @Volatile
        var PREVENT_MULTIPLE_CLICKS: Boolean = false
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this, MainViewModelFactory(application))
            .get(MainViewModel::class.java)
    }

    private fun initViews() {
        mHeroesAdapter = HeroesAdapter(Glide.with(this))
        mHeroesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        mBinding.heroesRecyclerList.adapter = mHeroesAdapter.withLoadStateFooter(
            footer = LoadStateAdapter(mHeroesAdapter)
        )

        mBinding.heroesRecyclerList.layoutManager = LinearLayoutManager(this)
        mBinding.swipe.setOnRefreshListener {
            mHeroesAdapter.refresh()
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mHeroesAdapter.loadStateFlow.collectLatest { loadStates ->
                mBinding.swipe.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mViewModel.heroesFlow.collectLatest {
                mHeroesAdapter.submitData(it)
            }
        }
    }
}