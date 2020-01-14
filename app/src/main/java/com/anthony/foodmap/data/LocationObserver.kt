package com.anthony.foodmap.data

import android.app.Activity
import android.content.Context
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.*

class LocationObserver(val context: Context?, val locationCallback: LocationCallback) : LifecycleObserver {


    private val locationRequest: LocationRequest
    private val providerClient: FusedLocationProviderClient

    private val UPDATE_INTERVAL = 10 * 1000 /* 10 secs */.toLong()
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    init {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
        LocationServices.getSettingsClient(context as Activity).checkLocationSettings(locationSettingsRequest)
        providerClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addLocationListener() {
        providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationListener() {
        providerClient.removeLocationUpdates(this.locationCallback)
    }
}