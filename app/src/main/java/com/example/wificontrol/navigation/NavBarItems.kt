package com.example.wificontrol.navigation

import com.example.wificontrol.R

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Home",
            icon = R.drawable.ic_home,
            route = Graph.Home.route
        ),
        BarItem(
            title = "Search",
            icon = R.drawable.ic_search,
            route = Graph.Search.route
        ),
        BarItem(
            title = "Statistics",
            icon = R.drawable.ic_statistics,
            route = Graph.Statistics.route
        ),
        BarItem(
            title = "Profile",
            icon = R.drawable.ic_profile,
            route = Graph.Profile.route
        ),
    )
}