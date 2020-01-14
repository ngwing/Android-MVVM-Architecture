package com.anthony.foodmap.core.dagger.components

import com.anthony.foodmap.FoodMapApplication
import com.anthony.foodmap.core.dagger.builders.ActivityBuilder
import com.anthony.foodmap.core.dagger.modules.FoodMapAppModule
import com.anthony.foodmap.core.dagger.modules.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, FoodMapAppModule::class, NetworkModule::class, ActivityBuilder::class])
interface FoodMapComponent : AndroidInjector<FoodMapApplication?> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<FoodMapApplication?>()
}