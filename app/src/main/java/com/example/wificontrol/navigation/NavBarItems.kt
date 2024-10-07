package com.example.wificontrol.navigation

import com.example.wificontrol.R

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Дом",
            icon = R.drawable.ic_home,
            route = Graph.Home.route
        ),
        BarItem(
            title = "Поиск",
            icon = R.drawable.ic_search,
            route = Graph.Search.route
        ),
        BarItem(
            title = "Статистика",
            icon = R.drawable.ic_statistics,
            route = Graph.Statistics.route
        ),
        BarItem(
            title = "Профиль",
            icon = R.drawable.ic_profile,
            route = Graph.Profile.route
        ),
    )
}