package eu.tutorials.weatherapp.data

import eu.tutorials.weatherapp.data.network.WeatherService
import eu.tutorials.weatherapp.models.WeatherResponse

class Repository(private val service: WeatherService) {

    suspend fun fetchWeatherData(lat:Double,lon:Double,units:String?,appId:String?):WeatherResponse{
        return service.getWeather(lat,lon,units,appId)
    }
}