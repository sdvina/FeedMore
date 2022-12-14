package org.sdvina.feedmore.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.ui.entry.EntryListScreen
import org.sdvina.feedmore.ui.entry.EntryViewModel

object AppDestinations {
    const val ENTRY_LIST_ROUTE = "entryList"
    const val ADD_FEED_ROUTE = "addFeed"
    const val MANAGE_FEED_ROUTE = "ManageFeed"
    const val NEW_ENTRY_LIST_ROUTE = "newEntryList"
    const val FAVORITE_ENTRY_LIST_ROUTE = "favoriteEntryList"
    const val SETTINGS_ROUTE = "settings"
    const val ABOUT_ROUTE = "about"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    openDrawer: ()  -> Unit,
    repository: AppRepository
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppDestinations.ENTRY_LIST_ROUTE,
        modifier = Modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(750))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(750))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(750))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(750))
        }
    ){
        composable(AppDestinations.ENTRY_LIST_ROUTE) {
            val entryViewModel: EntryViewModel = viewModel(
                factory = EntryViewModel.provideFactory(repository)
            )
            EntryListScreen(
                navController = navController,
                openDrawer = openDrawer,
                entryViewModel = entryViewModel
            )
        }
        composable(AppDestinations.ADD_FEED_ROUTE) {
            // TODO
        }
        composable(AppDestinations.MANAGE_FEED_ROUTE) {
            // TODO
        }
        composable(AppDestinations.NEW_ENTRY_LIST_ROUTE) {
            // TODO
        }
        composable(AppDestinations.FAVORITE_ENTRY_LIST_ROUTE) {
            // TODO
        }
        composable(AppDestinations.SETTINGS_ROUTE) {
            // TODO
        }
        composable(AppDestinations.ABOUT_ROUTE) {
            // TODO
        }
    }
}