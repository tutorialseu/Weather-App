package eu.tutorials.weatherapp.data

import eu.tutorials.weatherapp.data.network.WeatherService
import eu.tutorials.weatherapp.models.WeatherResponse

//Todo 1: Create a service variable as the constructor parameter
class Repository(private val service: WeatherService) {

    //Todo 2: create a suspend function to return the method from the service class
    suspend fun fetchWeatherData(lat:Double,lon:Double,units:String?,appId:String?): WeatherResponse {
        return service.getWeather(lat,lon,units,appId)
    }
}