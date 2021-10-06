package eu.tutorials.weatherapp.models

data class Sys(
    val type: Int = 0,
    val message: Double  = 0.0,
    val country: String = "",
    val sunrise: Long=0L,
    val sunset: Long=0L
)