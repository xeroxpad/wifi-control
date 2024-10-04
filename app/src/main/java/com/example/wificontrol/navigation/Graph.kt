package com.example.wificontrol.navigation

sealed class Graph(val route: String) {
    data object Home : Graph("home")
    data object Search : Graph("search")
    data object Statistics : Graph("statistics")
    data object Profile : Graph("profile")
}