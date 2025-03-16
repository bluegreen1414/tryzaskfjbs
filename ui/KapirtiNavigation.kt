package com.kapirti.social_chat_food_video.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import com.kapirti.social_chat_food_video.ui.TopLevelDestination.Companion.isTopLevel
import kotlinx.serialization.Serializable
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Welcome : Route

    @Serializable data object Login : Route
    @Serializable data object Register : Route
    @Serializable data object Edit : Route

    @Serializable data class Camera(val chatId: String) : Route
    @Serializable data class PhotoPicker(val chatId: String) : Route
    @Serializable data class VideoEdit(val chatId: String, val uri: String) : Route
    @Serializable data class PhotoEdit(val chatId: String, val uri: String) : Route
    @Serializable data class VideoPlayer(val uri: String) : Route

    @Serializable data object Settings : Route
    @Serializable data object DeleteAccount: Route
    @Serializable data object Country : Route
    @Serializable data object Language : Route
    @Serializable data object Feedback : Route
    @Serializable data object Tetris : Route
    @Serializable data object RacingCar : Route
    @Serializable data object Nickname : Route
    @Serializable data object ProfilePhoto : Route
    @Serializable data object Username : Route
    @Serializable data object Bio : Route
    @Serializable data object Search : Route
    @Serializable data object BlockedUser : Route

    @Serializable data object Home : Route
    @Serializable data object Explore : Route
    @Serializable data object ExplorePhoto : Route
    @Serializable data object Chats : Route

    @Serializable class Story(val id: String) : Route
    @Serializable data object Profile : Route
    @Serializable class UserProfile(val userId: String, val myId: String) : Route
    @Serializable class UserExplore(val id: String)  : Route
    @Serializable data object Calls : Route


    @Serializable
    //class SingleChat(val firstId: String, val secondId: String, val name: String) : Route
    class SingleChat(val roomId: String , val uriText: String? = null, val uriMimeType: String? = null) : Route
}





/**

@Serializable data object Chats : Route
//   @Serializable data object ChatNope : Route
//  @Serializable data object ChatExist : Route

@Serializable data object Explore : Route

@Serializable data object Users : Route

@Serializable data object Calls : Route



@Serializable
data class Camera(val chatId: Long) : Route

@Serializable
data class PhotoPicker(val chatId: Long) : Route

@Serializable
data class VideoEdit(val chatId: Long, val uri: String) : Route

@Serializable
data class VideoPlayer(val uri: String) : Route

 */




enum class TopLevelDestination(
    val route: Route,
    @StringRes val label: Int,
    val imageVector: ImageVector,
) {
    Home(
        route = Route.Home,
        label = AppText.home_title,
        imageVector = KapirtiIcons.Home,
    ),
    Explore(
        route = Route.Explore,
        label = AppText.app_name,
        imageVector = KapirtiIcons.Explore
    ),
    Camera(
        route = Route.Camera(chatId = ""),
        label = AppText.add_photo,
        imageVector = KapirtiIcons.Camera
    ),
    ExplorePhoto(
        route = Route.ExplorePhoto,
        label = AppText.shop_title,
        imageVector = KapirtiIcons.PhotoPicker
    ),
    Chats(
        route = Route.Chats,
        label = AppText.chats_title,
        imageVector = KapirtiIcons.Chats,
    ),
    ;


    companion object {
        val START_DESTINATION = Explore

        fun fromNavDestination(destination: NavDestination?): TopLevelDestination {
            return entries.find { dest ->
                destination?.hierarchy?.any {
                    it.hasRoute(dest.route::class)
                } == true
            } ?: START_DESTINATION
        }

        fun NavDestination.isTopLevel(): Boolean {
            return entries.any {
                hasRoute(it.route::class)
            }
        }
    }
}

private fun calculateNavigationLayoutType(
    destination: NavDestination?,
    defaultLayoutType: NavigationSuiteType,
): NavigationSuiteType {
    return when {
        destination == null -> defaultLayoutType
        // Never show navigation UI on Camera.
        destination.hasRoute<Route.Camera>() -> NavigationSuiteType.None
        // Top level destinations can show any layout type.
        destination.isTopLevel() -> defaultLayoutType
        // Every other destination goes through a ChatThread. Hide the bottom nav bar
        // since it interferes with composing chat messages.
        defaultLayoutType == NavigationSuiteType.NavigationBar -> NavigationSuiteType.None
        else -> defaultLayoutType
    }
}

@Composable
fun SocialiteNavSuite(
    navController: NavController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val topLevelDestination = TopLevelDestination.fromNavDestination(destination)
    val defaultLayoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
        currentWindowAdaptiveInfo(),
    )
    val layoutType = calculateNavigationLayoutType(destination, defaultLayoutType)

    NavigationSuiteScaffold(
        modifier = modifier,
        layoutType = layoutType,
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach {
                val isSelected = it == topLevelDestination
                item(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = it.imageVector,
                            contentDescription = stringResource(it.label),
                            modifier = Modifier.size(24.dp) ,
                        )
                    },
                    alwaysShowLabel = false,
                )
            }
        },
    ) {
        content()
    }
}
