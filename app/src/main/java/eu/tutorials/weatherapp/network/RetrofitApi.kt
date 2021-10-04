package eu.tutorials.weatherapp.network

import eu.tutorials.weatherapp.Constants
import retrofit.GsonConverterFactory
import retrofit.Retrofit

object RetrofitApi {
    // TODO (STEP 2: Create the Retrofit builder with GsonConverter.)
    // START
    /**
     * Add the built-in converter factory first. This prevents overriding its
     * behavior but also ensures correct behavior when using converters that consume all types.
     */
    val retrofit: Retrofit = Retrofit.Builder()
        // API base URL.
        .baseUrl(Constants.BASE_URL)
        /** Add converter factory for serialization and deserialization of objects. */
        /**
         * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        .addConverterFactory(GsonConverterFactory.create())
        /** Create the Retrofit instances. */
        .build()
    // END

    // TODO (STEP 3:Intialize the Weather service)
    // START
    /**
     * Here we map the service interface in which we declares the end point and the API type
     *i.e GET, POST and so on along with the request parameter which are required.
     */
    val service: WeatherService =
        retrofit.create<WeatherService>(WeatherService::class.java)
}