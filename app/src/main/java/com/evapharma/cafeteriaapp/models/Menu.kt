package com.evapharma.cafeteriaapp.models

data class Menu(
    val id:Int,
    val imageUrl:String,
    val name:String,
    val items:MutableList<FoodItem>,
)

