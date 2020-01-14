package com.anthony.foodmap.data

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class Location(val address:String, val crossStreet:String, val lat:Double, val lng:Double, val city:String, val country:String, val state: String){

    fun distanceTo(point: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat, lng,
                point.latitude, point.longitude,
                results)
        return results[0]
    }
}