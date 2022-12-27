package org.sdvina.feedmore.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class AppNavigation(navController: NavHostController) {
    val navigateToAddFeed: () -> Unit = {
        navController.navigate(AppDestinations.ADD_FEED_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToManageFeed: () -> Unit = {
        navController.navigate(AppDestinations.MANAGE_FEED_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(AppDestinations.SETTINGS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
