package becker.alexey.marvelheroes.ui.main

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import becker.alexey.marvelheroes.R
import becker.alexey.marvelheroes.data.HeroEntity
import becker.alexey.marvelheroes.data.ThumbnailEnum
import becker.alexey.marvelheroes.ui.details.DetailsActivity
import becker.alexey.marvelheroes.ui.main.MainActivity.Companion.PREVENT_MULTIPLE_CLICKS
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HeroesViewHolder(view: View, private val glide: RequestManager) :
    RecyclerView.ViewHolder(view) {
    private val mNameText: TextView = view.findViewById(R.id.hero_name)
    private val mThumbnailImage: ImageView = view.findViewById(R.id.hero_thumbnail)
    private var mHero: HeroEntity? = null

    init {
        view.setOnClickListener {
            if (!PREVENT_MULTIPLE_CLICKS) {
                PREVENT_MULTIPLE_CLICKS = true
                mHero?.id?.let {
                    val context = view.context as Activity
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra(DetailsActivity.DETAILS_HERO_ID, it)
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        ActivityOptions.makeSceneTransitionAnimation(
                            context,
                            Pair.create(
                                mThumbnailImage,
                                ViewCompat.getTransitionName(mThumbnailImage)
                            ),
                            Pair.create(mNameText, ViewCompat.getTransitionName(mNameText))
                        ).toBundle()
                    )
                }

                GlobalScope.launch(Dispatchers.Main) {
                    delay(1000)
                    PREVENT_MULTIPLE_CLICKS = false
                }
            }
        }
    }

    fun bind(hero: HeroEntity?) {
        this.mHero = hero
        mNameText.text = hero?.name ?: ""
        if (hero?.thumbnail?.startsWith(ThumbnailEnum.HTTPS.value) == true && !(hero.thumbnail.contains(
                ThumbnailEnum.THUMBNAIL_NOT_AVAILABLE.value
            ))
        ) {
            glide.load(hero.thumbnail + ThumbnailEnum.THUMBNAIL_SMALL.value + hero.thumbnailExtension)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.batman_shadow)
                .circleCrop()
                .into(mThumbnailImage)
        } else {
            glide
                .load(R.drawable.batman_shadow)
                .circleCrop()
                .into(mThumbnailImage)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: RequestManager): HeroesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hero, parent, false)
            return HeroesViewHolder(view, glide)
        }
    }

    fun updateHero(item: HeroEntity?) {
        mHero = item
    }
}