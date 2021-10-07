package com.evapharma.cafeteriaapp

import com.evapharma.cafeteriaapp.models.*

//? Constants of fragments:
//! Orders fragment
var PHONE_NUMBER:String=""
var OTP_CODE=""
const val IMG_HEIGHT:Int=130
const val IMG_WIDTH:Int =139

var orderDetailsList = mutableListOf<OrderDetailsItem>().apply{
    add(OrderDetailsItem(item_name = "Korolos Ragie", qty = 1, price = 5.0))
    add(OrderDetailsItem(item_name = "Habiba Khaled", qty =1 , price = 3.0))
    add(OrderDetailsItem(item_name = "Fries", qty = 1, price = 10.0))
    add(OrderDetailsItem(item_name = "Burger", qty = 3, price = 15.0))
    add(OrderDetailsItem(item_name = "Pizza", qty = 3, price = 20.0))
    add(OrderDetailsItem(item_name = "Indoomie", qty = 1, price = 3.5))
}

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
    add(FoodItem(0,"Cheese pizza","dummy text","https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg",28.5,2.5))
    add(FoodItem(1,"Meat pizza","dummy text","https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg",50.5,3.0))
}

var sandwichesMenu = mutableListOf<FoodItem>().apply{
    add(FoodItem(0,"Hot dog","dummy text","https://images.media-allrecipes.com/userphotos/9391099.jpg",10.5,4.5))
    add(FoodItem(1,"Hamburger","dummy text","https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg",22.5,1.5))
}

var menusList = mutableListOf<Menu>().apply{
    add(Menu(0,"https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg","Pizza",pizzaMenu))
    add(Menu(1,"https://images.media-allrecipes.com/userphotos/9391099.jpg","Sandwiches",sandwichesMenu))
}

const val MEALS_MENU = "MEALS_MENU"
const val CATEGORY_DATA = "CATEGORY_DATA"
const val PRODUCT_DATA = "PRODUCT_DATA"


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
