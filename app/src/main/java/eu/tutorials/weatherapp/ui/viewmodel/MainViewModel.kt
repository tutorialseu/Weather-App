package eu.tutorials.weatherapp.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.tutorials.weatherapp.data.Repository
import eu.tutorials.weatherapp.models.WeatherResponse
import eu.tutorials.weatherapp.utils.Resource
import kotlinx.coroutines.launch

//Todo 1: extend AndroidViewModel, create the Application and Repository variable in the constructor
class MainViewModel(application: Application, private val repository: Repository): AndroidViewModel(application) {

    /** TOdo 2 initialize the LocationLiveData and create a getter for it
     * MutableLiveData private field to get/save location updated values
     */
    //start
    private val locationData =
        LocationLiveData(application)

    /**
     * LiveData a public field to observe the changes of location
     */
    val getLocationData: LiveData<Location> = locationData
//end
    //Todo 3: create a MutableLivedata for posting the weather response with a type of
    //Resource and create a getter for it
    private var _weatherMutableLiveData = MutableLiveData<Resource<WeatherResponse>>()
    val weatherLiveData:LiveData<Resource<WeatherResponse>>
        get() = _weatherMutableLiveData

    //Todo: 4 create a method to launch a viewmodel scope and make the request to fetch rhe weather data
    fun fetchWeatherData(lat:Double,lon:Double,units:String?,appId:String?) {
        _weatherMutableLiveData.postValue(Resource.loading(null))
        try {
            viewModelScope.launch {
                val weather = repository.fetchWeatherData(lat, lon, units, appId)
                _weatherMutableLiveData.postValue(Resource.success(weather))
            }
        }catch (e:Exception){
            _weatherMutableLiveData.postValue(Resource.error(e.message.toString(),null))
        }
    }
}