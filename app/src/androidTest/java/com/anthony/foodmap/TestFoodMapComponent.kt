package com.anthony.foodmap

import android.app.Application
import com.anthony.foodmap.core.dagger.builders.ActivityBuilder
import com.anthony.foodmap.core.dagger.components.FoodMapComponent
import com.anthony.foodmap.core.dagger.modules.FoodMapAppModule
import com.anthony.foodmap.core.dagger.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, FoodMapAppModule::class, NetworkModule::class, ActivityBuilder::class])
interface TestFoodMapComponent : FoodMapComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): FoodMapComponent
    }
    fun inject(test: MainActivityTest)
}