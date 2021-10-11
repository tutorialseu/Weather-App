package eu.tutorials.weatherapp.data.network


import eu.tutorials.weatherapp.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * An Interface which defines the HTTP operations Functions.
 */
interface WeatherService {
//Todo 3: replace retrofit call with suspend modifier
    @GET("2.5/weather")
   suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String?,
        @Query("appid") appid: String?
    ): WeatherResponse
}
// END

