package eu.tutorials.weatherapp

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import eu.tutorials.weatherapp.models.*
import kotlinx.coroutines.flow.*
import java.io.IOException


//Todo 6 intialize the datastore
private const val DATA_STORE_FILE_NAME = "weather_prefs.pb"

 val Context.weatherPreferencesStore: DataStore<WeatherUpdate> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = WeatherResponseSerializer
)

//Todo 7 create a class to prepare the data for MainActivity
class WeatherListPref(private val weatherPreference:DataStore<WeatherUpdate>) {


    private val TAG = WeatherListPref::class.java.simpleName

    private fun ProtoWeatherResponse.toWeatherResponse(): WeatherResponse {
        val weatherList = this.getWeather(0)
        return WeatherResponse(
            coord =  Coord(this.coord.lon,this.coord.lat),
            weather = listOf(Weather(weatherList.id,weatherList.main,weatherList.description,
                weatherList.icon)),
            base = this.base,
            main = Main(this.main.temp,this.main.pressure,this.main.humidity,this.main.tempMin,
                this.main.tempMax,this.main.seaLevel,this.main.grndLevel),
            visibility = this.visibility,
            wind = Wind(this.wind.speed,this.wind.deg),
            clouds = Clouds(this.clouds.all),
            dt = this.dt,
            sys = Sys(this.sys.type,this.sys.message,this.sys.country,this.sys.sunrise,this.sys.sunset),
            id = this.id,
            name = this.name,
            cod = this.cod
        )
    }
    private fun WeatherResponse.toProtoWeatherResponse(): ProtoWeatherResponse {
        return ProtoWeatherResponse.newBuilder().setClouds(ProtoClouds.getDefaultInstance())
            .setCoord(ProtoCoord.getDefaultInstance()).setMain(ProtoMain.getDefaultInstance())
            .setSys(ProtoSys.getDefaultInstance())
            .setCod(this.cod)
            .setWind(ProtoWind.getDefaultInstance())
            .setBase(this.base)
            .setVisibility(this.visibility)
            .setDt(this.dt)
            .setId(this.id)
            .setName(this.name)
            .addAllWeather(listOf())
            .build()
    }

    fun weatherPreferenceFlow() = weatherPreference.data.catch { exception ->
        if (exception is IOException) {
            WeatherUpdate.getDefaultInstance().weatherResponse
        } else {
            throw Exception()
        }
    }.map { update ->
        val item = update.weatherResponse.toWeatherResponse()
        Log.i("Response list","$item")
        item
    }




    suspend fun updateWeather(weather: WeatherResponse) {
        weatherPreference.updateData { preferences ->
            preferences.toBuilder().clearWeatherResponse().setWeatherResponse(weather.toProtoWeatherResponse()).build()
        }
    }

}