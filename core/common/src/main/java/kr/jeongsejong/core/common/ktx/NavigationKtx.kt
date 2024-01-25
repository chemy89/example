package kr.jeongsejong.core.common.ktx

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigationSingleTopTo(route: String) =
    this.navigate(route) {
        val targetId = if (currentDestination != null) {
            currentDestination!!.id
        } else {
            this@navigationSingleTopTo.graph.findStartDestination().id
        }

        popUpTo(targetId) {
            saveState = true
            inclusive = true
        }

        restoreState = true
        launchSingleTop = true
    }