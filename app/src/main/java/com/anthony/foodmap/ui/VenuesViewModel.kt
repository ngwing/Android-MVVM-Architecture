package com.anthony.foodmap.ui

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthony.foodmap.Event
import com.anthony.foodmap.R
import com.anthony.foodmap.data.Venue
import com.anthony.foodmap.data.VenueResultsResponse
import com.anthony.foodmap.data.source.VenuesRepository
import com.anthony.foodmap.util.wrapEspressoIdlingResource
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.util.*

/**
 * ViewModel for the venue list screen.
 */
class VenuesViewModel(
        private val venuesRepository: VenuesRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<Venue>>().apply { value = emptyList() }
    val items: LiveData<List<Venue>> = _items

    private val _selectedItem = MutableLiveData<Venue>()
    val selectedItem: LiveData<Venue> = _selectedItem

    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    fun loadVenues(sw: String?, ne: String?, center: LatLng) {

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val singleResponse: Single<Response<VenueResultsResponse?>?>? = venuesRepository.apiService.search(sw, ne, 20)
                singleResponse?.let {
                    it.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { response: Response<VenueResultsResponse?>? ->
                                run {
                                    response?.let {
                                        if (it.isSuccessful && it.body()?.meta?.code == 200) {
                                            val venues = it.body()?.response?.venues
                                            venues?.let {
                                                val sortedList = it.sortedBy { it.location.distanceTo(center) }.filter { it != selectedItem.value }
                                                onLoadSuccess(sortedList)
                                            } ?: onLoadFail()
                                        } else onLoadFail()
                                    } ?: onLoadFail()
                                }
                            }
                }

            }
        }
    }

    fun onLoadSuccess(it: List<Venue>) {
        GlobalScope.launch(Dispatchers.Main) {
            isDataLoadingError.value = false
            _items.value = ArrayList(it)
            _empty.value = false
        }
    }

    fun onLoadFail() {
        GlobalScope.launch(Dispatchers.Main) {
            isDataLoadingError.value = false
            _items.value = emptyList()
            _empty.value = true
            showSnackbarMessage(R.string.loading_venues_error)
        }
    }

    fun onItemClick(venue: Venue, mapViewModel: MapViewModel) {
        if (TextUtils.equals(venue.id, _selectedItem.value?.id))
            return
        _selectedItem.value = venue
        _items.value?.dropWhile { it == venue }
        mapViewModel.onItemClick(venue)
    }

    fun reset() {
        _selectedItem.value = null
    }
}
