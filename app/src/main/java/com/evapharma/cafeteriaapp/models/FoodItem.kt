package com.evapharma.cafeteriaapp.models

import java.io.Serializable

data class FoodItem(
    val id:Int,
    val name:String,
    val description:String,
    val imageUrl:String,
    val price:Double,
    val rate:Double,
):Serializable

