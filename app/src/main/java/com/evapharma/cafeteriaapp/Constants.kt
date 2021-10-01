package com.evapharma.cafeteriaapp

import com.evapharma.cafeteriaapp.models.Order

//GLOBALS
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


//? Constants of activities:
//! Splash activity

//! Login activity



//------------------------------------------------------
//? Constants of helpers:


//------------------------------------------------------
//? Constants of models:


//------------------------------------------------------
//? Constants of services:
