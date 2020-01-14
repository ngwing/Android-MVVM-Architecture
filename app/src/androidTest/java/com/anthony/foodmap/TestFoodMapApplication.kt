package com.anthony.foodmap
import com.anthony.foodmap.core.dagger.components.DaggerFoodMapComponent

class TestFoodMapApplication: FoodMapApplication() {
    override fun onCreate() {
        super.onCreate()
        initDagger()
    }
    fun initDagger() {
        DaggerFoodMapComponent.builder().build().inject(this)
    }
}