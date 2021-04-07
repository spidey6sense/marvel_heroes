package becker.alexey.marvelheroes.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import becker.alexey.marvelheroes.R
import becker.alexey.marvelheroes.databinding.ItemNetworkStateBinding

class LoadStateAdapter(
    private val mAdapter: HeroesAdapter
) : LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent) { mAdapter.retry() }
    }
}

class NetworkStateItemViewHolder(
    parent: ViewGroup,
    val mRetryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_network_state, parent, false)
) {
    private val mBinding = ItemNetworkStateBinding.bind(itemView)
    private val mProgressBar = mBinding.progressBar
    private val mErrorMsg = mBinding.errorMsg
    private val mRetry = mBinding.retryButton
        .also {
            it.setOnClickListener { mRetryCallback() }
        }

    fun bindTo(loadState: LoadState) {
        mProgressBar.isVisible = loadState is LoadState.Loading
        mRetry.isVisible = loadState is LoadState.Error
        mErrorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        mErrorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }
}