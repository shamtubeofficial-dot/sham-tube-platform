package com.shamtube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.shamtube.app.ui.components.BottomNavigationBar
import com.shamtube.app.ui.navigation.Routes
import com.shamtube.app.ui.screens.AuthMode
import com.shamtube.app.ui.screens.AuthScreen
import com.shamtube.app.ui.screens.ChannelScreen
import com.shamtube.app.ui.screens.CommentsScreen
import com.shamtube.app.ui.screens.HomeScreen
import com.shamtube.app.ui.screens.LibraryScreen
import com.shamtube.app.ui.screens.NotificationsScreen
import com.shamtube.app.ui.screens.PlayerScreen
import com.shamtube.app.ui.screens.SearchScreen
import com.shamtube.app.ui.screens.SettingsScreen
import com.shamtube.app.ui.screens.ShortsScreen
import com.shamtube.app.ui.screens.SubscriptionsScreen
import com.shamtube.app.ui.screens.UploadScreen
import com.shamtube.app.ui.theme.ShamTubeTheme
import com.shamtube.app.viewmodel.AuthViewModel
import com.shamtube.app.viewmodel.ShamTubeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkMode by remember { mutableStateOf(false) }
            ShamTubeTheme(darkTheme = darkMode) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    ShamTubeApp(
                        darkMode = darkMode,
                        onDarkModeChange = { darkMode = it },
                    )
                }
            }
        }
    }
}

@Composable
private fun ShamTubeApp(
    viewModel: ShamTubeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomRoutes = setOf(Routes.Home, Routes.Shorts, Routes.Upload, Routes.Subscriptions, Routes.Library)
    val showBottomBar = currentRoute in bottomRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Routes.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.Home) {
                HomeScreen(
                    viewModel = viewModel,
                    onOpenVideo = { navController.navigate(Routes.player(it)) },
                    onOpenChannel = { navController.navigate(Routes.channel(it)) },
                    onOpenSearch = { navController.navigate(Routes.Search) },
                    onOpenNotifications = { navController.navigate(Routes.Notifications) },
                    onOpenProfile = { navController.navigate(Routes.Login) },
                )
            }
            composable(Routes.Shorts) {
                ShortsScreen(viewModel) { navController.navigate(Routes.player(it)) }
            }
            composable(Routes.Upload) {
                UploadScreen(viewModel) { navController.navigate(Routes.Home) }
            }
            composable(Routes.Subscriptions) {
                SubscriptionsScreen(viewModel) { navController.navigate(Routes.channel(it)) }
            }
            composable(Routes.Library) {
                LibraryScreen(
                    viewModel = viewModel,
                    onOpenVideo = { navController.navigate(Routes.player(it)) },
                    onOpenChannel = { navController.navigate(Routes.channel(it)) },
                )
            }
            composable(Routes.Search) {
                SearchScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onOpenVideo = { navController.navigate(Routes.player(it)) },
                    onOpenChannel = { navController.navigate(Routes.channel(it)) },
                )
            }
            composable(Routes.Notifications) {
                NotificationsScreen { navController.popBackStack() }
            }
            composable(Routes.Settings) {
                SettingsScreen(
                    darkMode = darkMode,
                    onDarkModeChange = onDarkModeChange,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(
                route = Routes.Channel,
                arguments = listOf(navArgument("channelId") { type = NavType.StringType }),
            ) { entry ->
                ChannelScreen(
                    channelId = entry.arguments?.getString("channelId").orEmpty(),
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onOpenSettings = { navController.navigate(Routes.Settings) },
                    onOpenVideo = { navController.navigate(Routes.player(it)) },
                )
            }
            composable(
                route = Routes.Player,
                arguments = listOf(navArgument("videoId") { type = NavType.StringType }),
            ) { entry ->
                PlayerScreen(
                    videoId = entry.arguments?.getString("videoId").orEmpty(),
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onOpenChannel = { navController.navigate(Routes.channel(it)) },
                    onOpenComments = { navController.navigate(Routes.comments(it)) },
                    onOpenVideo = { navController.navigate(Routes.player(it)) },
                )
            }
            composable(
                route = Routes.Comments,
                arguments = listOf(navArgument("videoId") { type = NavType.StringType }),
            ) { entry ->
                CommentsScreen(
                    videoId = entry.arguments?.getString("videoId").orEmpty(),
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Routes.Login) {
                AuthScreen(
                    mode = AuthMode.LOGIN,
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.navigate(authRoute(it)) },
                    onSuccess = { navController.popBackStack() },
                )
            }
            composable(Routes.Register) {
                AuthScreen(
                    mode = AuthMode.REGISTER,
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.navigate(authRoute(it)) },
                    onSuccess = { navController.popBackStack() },
                )
            }
            composable(Routes.ForgotPassword) {
                AuthScreen(
                    mode = AuthMode.FORGOT_PASSWORD,
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.navigate(authRoute(it)) },
                    onSuccess = { navController.popBackStack() },
                )
            }
        }
    }
}

private fun authRoute(mode: AuthMode): String = when (mode) {
    AuthMode.LOGIN -> Routes.Login
    AuthMode.REGISTER -> Routes.Register
    AuthMode.FORGOT_PASSWORD -> Routes.ForgotPassword
}