package com.anthony.foodmap

import android.content.Context
import androidx.multidex.MultiDex
import com.anthony.foodmap.core.dagger.components.DaggerFoodMapComponent
import com.anthony.foodmap.data.source.VenuesRepository
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

open class FoodMapApplication : DaggerApplication() {

    @Inject
    lateinit var venuesRepository: VenuesRepository


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication?> {
        return DaggerFoodMapComponent.builder().create(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}