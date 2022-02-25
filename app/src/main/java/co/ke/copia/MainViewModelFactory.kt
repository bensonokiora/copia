package co.ke.copia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.ke.copia.repo.LocalRepository

class MainViewModelFactory(
    private val repository: LocalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}