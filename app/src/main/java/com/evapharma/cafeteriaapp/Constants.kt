package com.evapharma.cafeteriaapp

import com.evapharma.cafeteriaapp.models.FoodItem
import com.evapharma.cafeteriaapp.models.Menu
import com.evapharma.cafeteriaapp.models.Order
import com.evapharma.cafeteriaapp.models.PhoneResponse

//? Constants of fragments:
//! Orders fragment
var PHONE_NUMBER:String=""
var OTP_CODE=""

var orders = mutableListOf<Order>().apply{
    add(Order(orderID = 109, employeeName = "Ahmed Rabie", employeeDepartment = "Pharma"))
    add(Order(orderID = 209, employeeName = "Mohammed Abusarie", employeeDepartment = "IT"))
    add(Order(orderID = 311, employeeName = "Maryam Maher", employeeDepartment = "IT"))
    add(Order(orderID = 322, employeeName = "Ibrahiem Yousef", employeeDepartment = "IT"))
    add(Order(orderID = 355, employeeName = "Mazen Mostafa", employeeDepartment = "Pharma"))
    add(Order(orderID = 377, employeeName = "Hosaam Adly", employeeDepartment = "IT"))
    add(Order(orderID = 111, employeeName = "Raghad Ahmed", employeeDepartment = "Pharma"))
    add(Order(orderID = 122 ,employeeName = "Sam David", employeeDepartment = "Pharma"))
}

//! Meals fragment
var pizzaMenu = mutableListOf<FoodItem>().apply{
    add(FoodItem(0,"Cheese pizza","dummy text","https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg",28.5))
    add(FoodItem(1,"Meat pizza","dummy text","https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg",50.5))
}

var sandwichesMenu = mutableListOf<FoodItem>().apply{
    add(FoodItem(0,"Hot dog","dummy text","https://images.media-allrecipes.com/userphotos/9391099.jpg",10.5))
    add(FoodItem(1,"Hamburger","dummy text","https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg",22.5))
}

var menusList = mutableListOf<Menu>().apply{
    add(Menu(0,"https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg","Pizza","dummy text",pizzaMenu))
    add(Menu(1,"https://images.media-allrecipes.com/userphotos/9391099.jpg","Sandwiches","dummy text",pizzaMenu))
}



//? Constants of activities:
//! Splash activity
const val SPLASH_TIME_OUT: Long = 2500
//! Login activity
const val USER_DATA:String="USER_DATA"
//! New password activity
const val MIN_PASSWORD_LENGTH=8
//! Send OTP activity
const val STARTER:String="0"
const val PHONE_LENGTH:Int=11
const val PHONE_RESPONSE:String="PHONE_RESPONSE"

//------------------------------------------------------
//? Constants of helpers:


//------------------------------------------------------
//? Constants of models:


//------------------------------------------------------
//? Constants of services:
