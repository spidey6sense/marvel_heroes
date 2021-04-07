package becker.alexey.marvelheroes.ui.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(private val context: Application, private val heroId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(context, heroId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}