package com.anthony.foodmap.data.source

import com.anthony.foodmap.data.api.ApiService
import javax.inject.Inject

/**
 * Interface to the data layer.
 */
class VenuesRepository @Inject constructor(var apiService: ApiService)
