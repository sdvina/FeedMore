package org.sdvina.feedmore.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.data.AppRepository
import org.sdvina.feedmore.ui.theme.AppTheme
import org.sdvina.feedmore.util.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun FeedMoreApp(
    repository: AppRepository
) {
    AppTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val navController = rememberAnimatedNavController()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawer(
                        navController = navController,
                        closeDrawer = { scope.launch { drawerState.close() } },
                        repository = repository
                    )
                }
            ){
                AppNavigation(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },
                    repository = repository
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedMoreAppPreview() {
    AppDataBaseHelper.onCreate(LocalContext.current)
    AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    AppTheme {
        FeedMoreApp(repository = AppRepository.get())
    }
}