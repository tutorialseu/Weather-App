package eu.tutorials.weatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
//Todo 1: create location Livedata class to listen for changes in location and update data
class LocationLiveData (context: Context) : MutableLiveData<Location>() {


        private var fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)



         // Sets the value. If there are active observers, the value will be dispatched to them.
        private fun setLocationData(location: Location) {
            value = location
        }


           private val locationRequest: LocationRequest = LocationRequest.create()
                .apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }


        /**
         * Called when the number of active observers change to 1 from 0.
         * This callback can be used to know that this LiveData is being used thus should be kept
         * up to date.
         */
        override fun onInactive() {
            super.onInactive()
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }

        @SuppressLint("MissingPermission")
        override fun onActive() {
            super.onActive()
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.also {
                        setLocationData(it)
                    }
                }
            startLocationUpdates()
        }

        /**
         * Callback that triggers on location updates available
         */
        private val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    setLocationData(locationResult.lastLocation)
                }
            }
        }

        /**
         * Initiate Location Updates using Fused Location Provider and
         * attaching callback to listen location updates
         */
        @SuppressLint("MissingPermission")
        private fun startLocationUpdates() {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }

    }