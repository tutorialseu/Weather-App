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

//Todo 4 : Create the MainViewModel class an extend AndroidViewModel so we can use the application context for
// the LocationLiveData
class MainViewModel(application: Application,private val repository: Repository):AndroidViewModel(application) {

    /**
     * MutableLiveData private field to get/save location updated values
     */
    private val locationData =
        LocationLiveData(application)

    /**
     * LiveData a public field to observe the changes of location
     */
    val getLocationData: LiveData<Location> = locationData

   private var _weatherMutableLiveData = MutableLiveData<Resource<WeatherResponse>>()
    val weatherLiveData:LiveData<Resource<WeatherResponse>>
    get() = _weatherMutableLiveData

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