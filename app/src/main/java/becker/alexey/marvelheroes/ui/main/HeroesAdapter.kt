package becker.alexey.marvelheroes.ui.main

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import becker.alexey.marvelheroes.data.HeroEntity
import com.bumptech.glide.RequestManager

class HeroesAdapter(private val glide: RequestManager) :
    PagingDataAdapter<HeroEntity, HeroesViewHolder>(HEROES_COMPARATOR) {

    override fun onBindViewHolder(holder: HeroesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: HeroesViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            holder.updateHero(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesViewHolder {
        return HeroesViewHolder.create(parent, glide)
    }

    companion object {
        val HEROES_COMPARATOR = object : DiffUtil.ItemCallback<HeroEntity>() {
            override fun areContentsTheSame(oldItem: HeroEntity, newItem: HeroEntity): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: HeroEntity, newItem: HeroEntity): Boolean =
                oldItem.name == newItem.name

            override fun getChangePayload(oldItem: HeroEntity, newItem: HeroEntity): Any? {
                return null
            }
        }
    }
}
