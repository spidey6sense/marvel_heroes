package becker.alexey.marvelheroes.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import becker.alexey.marvelheroes.data.ServiceLocator
import kotlinx.coroutines.launch

class MainViewModel(
    context: Application
) : AndroidViewModel(context) {

    private val repository = ServiceLocator.instance(context)
        .getRepository()

    init {
        resetData()
    }

    val heroesFlow = repository.getHeroesFlow().cachedIn(viewModelScope)

    private fun resetData() = viewModelScope.launch {
        repository.clearHeroes()
    }

}