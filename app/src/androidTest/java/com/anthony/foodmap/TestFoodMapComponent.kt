package com.anthony.foodmap

import com.anthony.foodmap.core.dagger.builders.ActivityBuilder
import com.anthony.foodmap.core.dagger.components.FoodMapComponent
import com.anthony.foodmap.core.dagger.modules.FoodMapAppModule
import com.anthony.foodmap.core.dagger.modules.NetworkModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, FoodMapAppModule::class, NetworkModule::class, ActivityBuilder::class])
interface TestFoodMapComponent : FoodMapComponent {
}