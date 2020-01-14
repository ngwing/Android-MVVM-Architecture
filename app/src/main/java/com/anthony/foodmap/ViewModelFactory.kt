package com.anthony.foodmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anthony.foodmap.data.source.VenuesRepository
import com.anthony.foodmap.ui.MapViewModel
import com.anthony.foodmap.ui.VenuesViewModel

/**
 * Factory for all ViewModels.
 */
class ViewModelFactory constructor(
        private val venuesRepository: VenuesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(VenuesViewModel::class.java) ->
                        VenuesViewModel(venuesRepository)
                    isAssignableFrom(MapViewModel::class.java) ->
                        MapViewModel()
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
