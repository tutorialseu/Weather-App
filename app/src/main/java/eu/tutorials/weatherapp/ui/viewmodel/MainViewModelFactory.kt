package eu.tutorials.weatherapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.tutorials.weatherapp.data.Repository

//TOdo 5: extend ViewModelprovider.Factory and create the MainViewmodel dependencies in the constructor
class MainViewModelFactory(private val application: Application, private val repository: Repository
): ViewModelProvider.Factory {
    //Todo 6: return the create class to initialize the MainViewModel as type T
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(application,repository) as T
        }else{
            throw  ClassNotFoundException()
        }
    }
}