package becker.alexey.marvelheroes.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import becker.alexey.marvelheroes.data.HeroEntity
import becker.alexey.marvelheroes.data.ServiceLocator

class DetailsViewModel(
    context: Application, heroId: Int
) : AndroidViewModel(context) {
    private val repository = ServiceLocator.instance(context)
        .getRepository()
    var heroForShow: LiveData<HeroEntity> =
        repository.getHeroDatabase().heroDao().getHeroBiography(heroId).asLiveData()
}