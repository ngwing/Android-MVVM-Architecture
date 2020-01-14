package com.anthony.foodmap.data

data class Venue(val id:String, val name:String, val location: Location, var categories: List<Category>)