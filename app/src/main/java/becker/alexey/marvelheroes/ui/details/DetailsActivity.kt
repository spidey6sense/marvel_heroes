package becker.alexey.marvelheroes.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import becker.alexey.marvelheroes.R
import becker.alexey.marvelheroes.data.HeroEntity
import becker.alexey.marvelheroes.data.ThumbnailEnum
import becker.alexey.marvelheroes.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val DETAILS_HERO_ID = "details_id"
    }

    private lateinit var mBinding: ActivityDetailsBinding
    private lateinit var mViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(
            this,
            DetailsViewModelFactory(application, intent.getIntExtra(DETAILS_HERO_ID, 0))
        )
            .get(DetailsViewModel::class.java)
        mViewModel.heroForShow.observe(this) { hero ->
            setHeroDetails(hero)
        }
    }

    private fun setHeroDetails(heroEntity: HeroEntity) {
        mBinding.heroName.text = heroEntity.name
        mBinding.heroBiography.text = heroEntity.biography

        if (!heroEntity.thumbnail.contains(ThumbnailEnum.THUMBNAIL_NOT_AVAILABLE.value)) {
            Glide.with(this)
                .load(heroEntity.thumbnail + ThumbnailEnum.THUMBNAIL_BIG.value + heroEntity.thumbnailExtension)
                .thumbnail(
                    Glide.with(this)
                        .load(heroEntity.thumbnail + ThumbnailEnum.THUMBNAIL_SMALL.value + heroEntity.thumbnailExtension)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        scheduleStartPostponedTransition(mBinding.heroThumbnail)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        scheduleStartPostponedTransition(mBinding.heroThumbnail)
                        return false
                    }
                })
                .circleCrop()
                .into(mBinding.heroThumbnail)
        } else {
            Glide.with(this)
                .load(R.drawable.batman_shadow)
                .circleCrop()
                .into(mBinding.heroThumbnail)
        }
    }

    private fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }
}