package eu.tutorials.weatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

//Todo 3: create a context in the constructor and extend MutableLiveData, override the active and inactive
//from the MutableLiveData
class LocationLiveData (context: Context) : MutableLiveData<Location>() {


    //Todo 5: create a fusedlocationProviderClient to get fused location
    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)



    // Todo 4: create a method to set the location from the fusedLocationProvider
    private fun setLocationData(location: Location) {
        value = location
    }


    //Todo 6:Create location request with a high accuracy
    private val locationRequest: LocationRequest = LocationRequest.create()
        .apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


    /** Todo 9: override onInactive and onActive method from the MutableLiveData
     * removeLocationUpdates when inactive and set success listener to get last location
     * when active
     * Called when the number of active observers change to 1 from 0.
     * This callback can be used to know that this LiveData is being used thus should be kept
     * up to date.
     */
    //start
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
    //end

    /** Todo 7: create a location callback and setLocationData from
     * last location if result is not null
     * Callback that triggers on location updates available
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                setLocationData(locationResult.lastLocation)
            }
        }
    }

    /** Todo 8: create a method to request location update and pass in locationRequest
     * and locationCallback then null as a Looper
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