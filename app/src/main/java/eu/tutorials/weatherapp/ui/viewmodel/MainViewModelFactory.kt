package eu.tutorials.weatherapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.tutorials.weatherapp.data.Repository

//Todo 5: create a viewModel factory to setup dependency for the viewmodel class
class MainViewModelFactory(private val application: Application,private val repository: Repository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(application,repository) as T
        }else{
            throw  ClassNotFoundException()
        }
    }
}