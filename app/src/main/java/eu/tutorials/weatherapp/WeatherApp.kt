package eu.tutorials.weatherapp

import android.app.Application
import eu.tutorials.weatherapp.data.Repository
import eu.tutorials.weatherapp.data.network.RetrofitApi

class WeatherApp:Application() {
    //TOdo 7: Initialize the Repository class
    val repository: Repository by lazy {
        Repository(RetrofitApi.service)
    }
}