package com.anthony.foodmap.util

import com.anthony.foodmap.data.source.VenuesRepository
import kotlinx.coroutines.runBlocking

/**
 * A blocking version of TasksRepository.saveTask to minimize the number of times we have to
 * explicitly add <code>runBlocking { ... }</code> in our tests
 */
fun VenuesRepository.search(sw: String?,
                                      ne: String?,
                                      limit: Int) = runBlocking {
    this@search.apiService.search(sw, ne, limit)
}