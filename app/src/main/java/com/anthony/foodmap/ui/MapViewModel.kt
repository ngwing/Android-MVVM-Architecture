package com.anthony.foodmap.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anthony.foodmap.data.Venue
import com.anthony.foodmap.data.source.VenuesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


/**
 * ViewModel for map.
 */
class MapViewModel: ViewModel() {
    fun onItemClick(venue: Venue) {
        selectedLocation.value = LatLng(venue.location.lat, venue.location.lng)
    }

    fun reset() {
        reset.value = true
    }

    val currentLocation = MutableLiveData<LatLng>()
    val visibleRegionBounds = MutableLiveData<LatLngBounds>()
    val selectedLocation = MutableLiveData<LatLng>()
    val reset = MutableLiveData<Boolean>()

}
