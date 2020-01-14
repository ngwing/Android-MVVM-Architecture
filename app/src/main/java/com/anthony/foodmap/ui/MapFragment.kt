package com.anthony.foodmap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anthony.foodmap.R
import com.anthony.foodmap.data.LocationObserver
import com.anthony.foodmap.databinding.MapFragBinding
import com.anthony.foodmap.util.color
import com.anthony.foodmap.util.getViewModelFactory
import com.anthony.foodmap.util.toBitmapDescriptor
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var viewModel: MapViewModel
    private lateinit var viewDataBinding: MapFragBinding
    private lateinit var locationObserver: LocationObserver
    private lateinit var locationCallback: LocationCallback
    private var googleMap: GoogleMap? = null
    private var firstTimeLocated = false
    private var permitted = false


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!, getViewModelFactory()).get(MapViewModel::class.java)
        viewDataBinding = MapFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createLocationCallback()
        locationObserver = LocationObserver(context, locationCallback)
        lifecycle.addObserver(locationObserver)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        observeSelectedLocation()
        observeOnBackPressed()
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) { // do work here
                onLocationChanged(locationResult.lastLocation)
            }
        }
    }

    private fun observeOnBackPressed() {
        viewModel.reset.observe(viewLifecycleOwner, Observer { reset ->
            run {

                this.googleMap?.clear()
                var latLng = viewModel.currentLocation.value
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0F))
                addMyLocationMarker(latLng!!)
            }
        })
    }

    fun observeSelectedLocation() {
        viewModel.selectedLocation.observe(viewLifecycleOwner, Observer { latLng ->
            run {
                this.googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                addSelectedLocationMarker(latLng)
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap?.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.margin_bottom))
        if (ActivityCompat.checkSelfPermission(
                        context!!, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ), 0)

            return
        }
        this.googleMap?.setMyLocationEnabled(true)
        this.googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        this.googleMap?.setOnCameraIdleListener(this::onCameraIdle)
        this.googleMap?.setOnMapClickListener(this)
        permitted = true
    }

    fun onLocationChanged(location: Location) { // New location has now been determined
        val latLng = LatLng(location.latitude, location.longitude)
        viewModel.currentLocation.value = latLng
        if (!firstTimeLocated) {
            this.googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0F))
            addMyLocationMarker(latLng)
        }
        if (permitted)
            firstTimeLocated = true
    }

    private fun addMyLocationMarker(latLng: LatLng) {
        this.googleMap?.addMarker(MarkerOptions()
                .position(latLng)
                .icon(R.drawable.ic_black_pin.toBitmapDescriptor(resources)))
    }

    private fun addSelectedLocationMarker(latLng: LatLng) {
        this.googleMap?.addMarker(MarkerOptions()
                .position(latLng)
                .icon(R.drawable.ic_black_pin.toBitmapDescriptor(resources, android.R.color.holo_red_dark.color(resources))))
    }

    override fun onMyLocationClick(@NonNull location: Location) {
        Toast.makeText(context, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMapClick(p0: LatLng?) {
    }

    fun onCameraIdle() {
        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .repeat(1)
                .playOn(viewDataBinding.centerDot)
        this.googleMap?.let {
            var latLngBounds = it.projection.visibleRegion.latLngBounds
            viewModel.visibleRegionBounds.value = latLngBounds
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(googleMap)
                }
            }
        }
    }
}
