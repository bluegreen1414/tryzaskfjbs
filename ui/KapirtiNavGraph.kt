package com.kapirti.social_chat_food_video.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kapirti.social_chat_food_video.common.func.InterstitialAdManager
import com.kapirti.social_chat_food_video.core.viewmodel.IncludeAccountViewModel
import com.kapirti.social_chat_food_video.ui.presentation.camera.videoedit.VideoEditScreen
import com.kapirti.social_chat_food_video.game.racing.ui.presentation.racing.RacingCarRoute
import com.kapirti.social_chat_food_video.game.tetris.ui.TetrisRoute
import com.kapirti.social_chat_food_video.model.extractChatId
import com.kapirti.social_chat_food_video.ui.presentation.blockedusers.BlockedUsersRoute
import com.kapirti.social_chat_food_video.ui.presentation.calls.CallsRoute
import com.kapirti.social_chat_food_video.ui.presentation.camera.CameraRoute
import com.kapirti.social_chat_food_video.ui.presentation.camera.Media
import com.kapirti.social_chat_food_video.ui.presentation.camera.MediaType
import com.kapirti.social_chat_food_video.ui.presentation.camera.photoedit.PhotoEditScreen
import com.kapirti.social_chat_food_video.ui.presentation.chat.ChatRoute
import com.kapirti.social_chat_food_video.ui.presentation.home.chats.ChatsRoute
import com.kapirti.social_chat_food_video.ui.presentation.edit.EditRoute
import com.kapirti.social_chat_food_video.ui.presentation.home.explore.ExploreRoute
import com.kapirti.social_chat_food_video.ui.presentation.profile.ProfileRoute
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.bio.BioScreen
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname.NicknameScreen
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.photo.ProfilePhotoScreen
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.username.UsernameScreen
import com.kapirti.social_chat_food_video.ui.presentation.home.users.HomeRoute
import com.kapirti.social_chat_food_video.ui.presentation.login.LogInScreen
import com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker.navigation.navigateToPhotoPicker
import com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker.navigation.photoPickerScreen
import com.kapirti.social_chat_food_video.ui.presentation.home.explorephoto.ExplorePhotoRoute
import com.kapirti.social_chat_food_video.ui.presentation.player.VideoPlayerScreen
import com.kapirti.social_chat_food_video.ui.presentation.register.RegisterScreen
import com.kapirti.social_chat_food_video.ui.presentation.search.SearchScreen
import com.kapirti.social_chat_food_video.ui.presentation.settings.SettingsRoute
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.country.CountryScreen
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.deleteaccount.DeleteAccountScreen
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.feedback.FeedbackScreen
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.language.LanguageScreen
import com.kapirti.social_chat_food_video.ui.presentation.splash.SplashScreen
import com.kapirti.social_chat_food_video.ui.presentation.story.StoryScreen
import com.kapirti.social_chat_food_video.ui.presentation.userexplore.UserExploreRoute
import com.kapirti.social_chat_food_video.ui.presentation.userprofile.UserProfileRoute
import com.kapirti.social_chat_food_video.ui.presentation.welcome.WelcomeRoute
import com.kapirti.social_chat_food_video.webrtc.ui.WebRtcActivity

@Composable
fun KapirtiNavGraph(
    modifier: Modifier,
    interstitialAdManager: InterstitialAdManager,
    shortcutParams: ShortcutParams?,
) {
    val activity = LocalContext.current as Activity
    val navController = rememberNavController()

    navController.addOnDestinationChangedListener { _: NavController, destination: NavDestination, _: Bundle? ->
        // Lock the layout of the Camera screen to portrait so that the UI layout remains
        // constant, even on orientation changes. Note that the camera is still aware of
        // orientation, and will assign the correct edge as the bottom of the photo or video.
        if (destination.hasRoute<Route.Camera>()) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val includeAccountViewModel: IncludeAccountViewModel = viewModel()

    SocialiteNavSuite(
        navController = navController,
        modifier = modifier,
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
            popExitTransition = {
                scaleOut(
                    targetScale = 0.9f,
                    transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f),
                )
            },
            popEnterTransition = {
                EnterTransition.None
            },
        ) {
            composable<Route.Home> {
                HomeRoute(
                    includeAccountViewModel = includeAccountViewModel,
                    onItemClicked = { userId, myId ->
                        navController.navigate(Route.UserProfile(userId, myId))
                    },
                    onCreateStoryClick = { navController.navigate(Route.Camera("chatiddddd"))},
                    navigateStory = { storyId ->
                        navController.navigate(Route.Story(storyId))
                    },
                    navigateCountry = {
                        navController.navigate(Route.Country) {
                            launchSingleTop = true
                        }
                    },
                    navigateLanguage = {
                        navController.navigate(Route.Language) {
                            launchSingleTop = true
                        }
                    },
                    navigateSearch = {
                        navController.navigate(Route.Search) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Explore> {
                ExploreRoute(
                    onPublisherClick = { userId ->
                        navController.navigate(Route.UserProfile(userId, "myId")) },
                    restartAppExplore = {
                        navController.navigate(Route.Explore) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Chats> {
                ChatsRoute(
                    onChatClicked = { chatId ->
                        //navController.navigate(Route.SingleChat(firstId, secondId, name))
                        navController.navigate(Route.SingleChat(chatId))
                    },
                    onCallClick = { navController.navigate(Route.Calls) },
                    onProfileClick = { navController.navigate(Route.Profile) }
                )
            }
            composable<Route.ExplorePhoto> {
                ExplorePhotoRoute(
                    onPublisherClick = { userId ->
                        navController.navigate(Route.UserProfile(userId, "myId")) },
                    modifier = Modifier.fillMaxSize(),
                )
            }


            composable<Route.Calls> { CallsRoute(popUp = { navController.popBackStack() }) }
            composable<Route.Profile> {
                ProfileRoute(
                    navigateSettings = {
                        navController.navigate(Route.Settings) {
                            launchSingleTop = true
                        }
                    },
                    navigateNickname = {
                        navController.navigate(Route.Nickname) {
                            launchSingleTop = true
                        }
                    },
                    navigateProfilePhoto = {
                        navController.navigate(Route.ProfilePhoto) {
                            launchSingleTop = true
                        }
                    },
                    navigateUsername = {
                        navController.navigate(Route.Username) {
                            launchSingleTop = true
                        }
                    },
                    navigateBio = {
                        navController.navigate(Route.Bio) {
                            launchSingleTop = true
                        }
                    },
                    navigateUserExplore = { id ->
                        navController.navigate(Route.UserExplore(id))
                    },
                    popUp = { navController.popBackStack() },
                    onAddClick = { navController.navigate(Route.Camera("chatiddddd"))},
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.UserProfile> {
                val userId = it.toRoute<Route.UserProfile>().userId
                val myId = it.toRoute<Route.UserProfile>().myId

                UserProfileRoute(
                    userId = userId,
                    onChatClicked = { roomId ->
                        //val chatId = extractChatId(shortcutParams.shortcutId)
                        navController.navigate(Route.SingleChat(roomId))
                    },
                    onVideoCallClicked = {
                        activity.startActivity(Intent(activity, WebRtcActivity::class.java))
                    },
                    navigateUserExplore = { id ->
                        navController.navigate(Route.UserExplore(id))
                    },
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Story> { backStackEntry ->
                val route: Route.Story = backStackEntry.toRoute()
                val id = route.id
                StoryScreen(
                    id = id,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.UserExplore> {
                val id = it.toRoute<Route.UserExplore>().id

                UserExploreRoute(
                    id = id,
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            composable<Route.Splash> {
                SplashScreen(
                    navigateAndPopUpSplashToWelcome = {
                        navController.navigate(Route.Welcome) {
                            launchSingleTop = true
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    },
                    navigateAndPopUpSplashToLogin = {
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    },
                    navigateAndPopUpSplashToHome = {
                        navController.navigate(Route.Explore) {
                            launchSingleTop = true
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Welcome> {
                WelcomeRoute(
                    navigateAndPopUpWelcomeToLogin = {
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                            popUpTo(Route.Welcome) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Login> {
                LogInScreen(
                    navigateAndPopUpLoginToHome = {
                        navController.navigate(Route.Explore) {
                            launchSingleTop = true
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    },
                    navigateAndPopUpLoginToRegister = {
                        navController.navigate(Route.Register) {
                            launchSingleTop = true
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    },
                    navigateAndPopUpLoginToEdit = {
                        navController.navigate(Route.Edit) {
                            launchSingleTop = true
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    },
                    interstitialAdManager = interstitialAdManager,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Register> {
                RegisterScreen(
                    navigateAndPopUpRegisterToEdit = {
                        navController.navigate(Route.Edit) {
                            launchSingleTop = true
                            popUpTo(Route.Register) { inclusive = true }
                        }
                    },
                    navigateAndPopUpRegisterToLogin = {
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                            popUpTo(Route.Register) { inclusive = true }
                        }
                    },
                    interstitialAdManager = interstitialAdManager,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Edit> {
                EditRoute(
                    popUp = { navController.popBackStack() },
                    restartAppEdit = {
                        navController.navigate(Route.Edit) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    restartAppUsers = {
                        navController.navigate(Route.Home) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Settings> {
                SettingsRoute(
                    popUp = { navController.popBackStack() },
                    navigateLogin = {
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                        }
                    },
                    navigateRegister = {
                        navController.navigate(Route.Register) {
                            launchSingleTop = true
                        }
                    },
                    navigateCountry = {
                        navController.navigate(Route.Country) {
                            launchSingleTop = true
                        }
                    },
                    navigateFeedback = {
                        navController.navigate(Route.Feedback) {
                            launchSingleTop = true
                        }
                    },
                    navigateDeleteAccount = {
                        navController.navigate(Route.DeleteAccount) {
                            launchSingleTop = true
                        }
                    },
                    navigateTetris = {
                        navController.navigate(Route.Tetris) {
                            launchSingleTop = true
                        }
                    },
                    navigateRacingCar = {
                        navController.navigate(Route.RacingCar) {
                            launchSingleTop = true
                        }
                    },
                    restartApp = {
                        navController.navigate(Route.Splash) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navigateBlockedUser = {
                        navController.navigate(Route.BlockedUser) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable<Route.DeleteAccount> {
                DeleteAccountScreen(
                    popUp = { navController.popBackStack() },
                    restartApp = {
                        navController.navigate(Route.Splash) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Feedback> {
                FeedbackScreen(
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Country> {
                CountryScreen(
                    popUp = { navController.popBackStack() },
                    restartApp = {
                        navController.navigate(Route.Splash) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Language> {
                LanguageScreen(
                    popUp = { navController.popBackStack() },
                    restartApp = {
                        navController.navigate(Route.Splash) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Tetris> { TetrisRoute(modifier = Modifier.fillMaxSize()) }
            composable<Route.RacingCar> { RacingCarRoute(modifier = Modifier.fillMaxSize()) }
            composable<Route.Nickname> { NicknameScreen(modifier = Modifier.fillMaxSize()) }
            composable<Route.ProfilePhoto> {
                ProfilePhotoScreen(
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Username> {
                UsernameScreen(
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Bio> {
                BioScreen(
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.BlockedUser> {
                BlockedUsersRoute(
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Route.Search> {
                SearchScreen(
                    popUp = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }


            composable<Route.SingleChat>(
                deepLinks = listOf(
                    navDeepLink {
                        action = Intent.ACTION_VIEW
                        uriPattern = "https://socialite.google.com/chat/{roomId}"
                    },
                )
            ) { backStackEntry ->
                val route: Route.SingleChat = backStackEntry.toRoute()
                val roomId = route.roomId

                //val prefilledText = route.text
                val uriText = route.uriText
                val uriMimeType = route.uriMimeType


                //val firstId = route.firstId
                //val secondId = route.secondId
                //val name = route.name
                ChatRoute(
                    chatId = roomId,
                    uriText = uriText,
                    uriMimeType = uriMimeType,
                    onBackPressed = { navController.navigateUp() },
                    onCameraClick = { navController.navigate(Route.Camera(roomId)) },
                    onVoiceCallClicked = {
                        val intent = Intent(activity, WebRtcActivity::class.java)
                        intent.putExtra("roomId", roomId)
                        activity.startActivity(intent)
                    },
                    onVideoClick = { uri -> navController.navigate(Route.VideoPlayer(uri)) },
                    popUp = { navController.popBackStack() },
                    includeAccountViewModel = includeAccountViewModel,
                    onUserClick = { userId ->
                        navController.navigate(Route.UserProfile(userId, "myId"))
                    },
                )
            }
            photoPickerScreen(onPhotoPicked = navController::popBackStack,)
            composable<Route.Camera> { backStackEntry ->
                val route: Route.Camera = backStackEntry.toRoute()
                val chatId = route.chatId
                CameraRoute(
                    navigatePhotoPicker = { navController.navigateToPhotoPicker(chatId) },
                    onMediaCaptured = { capturedMedia: Media? ->
                        when (capturedMedia?.mediaType) {
                            MediaType.PHOTO -> {
                                navController.navigate(
                                    Route.PhotoEdit(
                                        chatId,
                                        capturedMedia.uri.toString(),
                                    ),
                                )
                            }

                            MediaType.VIDEO -> {
                                navController.navigate(
                                    Route.VideoEdit(
                                        chatId,
                                        capturedMedia.uri.toString(),
                                        ),
                                    )
                            }
                            else -> { navController.popBackStack() }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }


            /**          composable<Route.Camera> { backStackEntry ->
            val route: Route.Camera = backStackEntry.toRoute()
            val chatId = route.chatId

            AddRoute(
            navigateAndPopUpAddToVideo = { /*TODO*/ },
            navigateAndPopUpAddToPhoto = { /*TODO*/ },
            modifier = Modifier.fillMaxSize()
            )

            CameraSocialTrying(
            onMediaCaptured = { capturedMedia: Media? ->
            when (capturedMedia?.mediaType) {
            MediaType.PHOTO -> {
            navController.popBackStack()
            }

            MediaType.VIDEO -> {
            navController.navigate(
            Route.VideoEdit(
            chatId,
            capturedMedia.uri.toString(),
            ),
            )
            }

            else -> {
            // No media to use.
            navController.popBackStack()
            }
            }
            },
            modifier = Modifier.fillMaxSize(),
            )
             */

            composable<Route.VideoPlayer> { backStackEntry ->
                val route: Route.VideoPlayer = backStackEntry.toRoute()
                val videoUri = route.uri
                VideoPlayerScreen(
                    uri = videoUri,
                    onCloseButtonClicked = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.VideoEdit> { backStackEntry ->
                val route: Route.VideoEdit = backStackEntry.toRoute()
                val chatId = route.chatId
                val videoUri = route.uri
                VideoEditScreen(
                    chatId = chatId,
                    uri = videoUri,
                    onCloseButtonClicked = { navController.popBackStack() },
                    restartAppExplore = {
                        navController.navigate(Route.Explore) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    restartAppStory = {
                        navController.navigate(Route.Home) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    restartAppChat = {
                        navController.navigate(Route.SingleChat(chatId)) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                )
            }
            composable<Route.PhotoEdit> { backStackEntry ->
                val route: Route.VideoEdit = backStackEntry.toRoute()
                val chatId = route.chatId
                val videoUri = route.uri
                PhotoEditScreen(
                    chatId = chatId,
                    uri = videoUri,
                    onCloseButtonClicked = { navController.popBackStack() },
                    restartAppExplore = {
                        navController.navigate(Route.Explore) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    restartAppStory = {
                        navController.navigate(Route.Home) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    restartAppChat = {
                        navController.navigate(Route.SingleChat(chatId)) {
                            launchSingleTop = true
                            popUpTo(0) { inclusive = true }
                        }
                    },
                )
            }
        }
    }

    if (shortcutParams != null) {

        val chatId = extractChatId(shortcutParams.shortcutId)
        val text = shortcutParams.text
        android.util.Log.d("myTag", "shortcutId : ${shortcutParams.shortcutId}")
        android.util.Log.d("myTag", "nav controller, chat id : $chatId")
        android.util.Log.d("myTag", "nav controller, text : $text")
        navController.navigate(Route.SingleChat(chatId, text))
    }
}

data class ShortcutParams(
    val shortcutId: String,
    val text: String,
)




/**


val includeUserCallViewModel: IncludeUserCallViewModel = viewModel()



KapirtiNavSuite(


/**            composable<Route.Chats> {
ChatsRoute(
// includeAccountInfoViewModel = includeAccountInfoViewModel,
onChatClicked = { chatId -> navController.navigate(Route.ChatThread(chatId)) },
navigateChatExist = {
navController.navigate(Route.ChatExist) {
launchSingleTop = true
}
},
modifier = Modifier.fillMaxSize(),
)
}*/

composable<Route.Users> {
UsersRoute(
includeAccountViewModel = includeAccountViewModel,
navigateAccountDetail = {
navController.navigate(Route.AccountDetail) {
launchSingleTop = true
}
},
//false,
//{}, {},
modifier = Modifier.fillMaxSize(),
)
// onExploreItemClicked = { },//launchDetailsActivity(context = this, item = it) },
// onDateSelectionClicked = { } )//launchCalendarActivity(this) }
}


composable<Route.Explore> {
ExploreRoute(Modifier.fillMaxSize())
}

composable<Route.Shop> {
ShopRoute(
navigateEdit = {
navController.navigate(Route.Edit) {
launchSingleTop = true
}
},
modifier = Modifier.fillMaxSize()
)
}

// Invoke PhotoPicker to select photo or video from device gallery
photoPickerScreen(
onPhotoPicked = navController::popBackStack,
)

composable<Route.VideoEdit> { backStackEntry ->
val route: Route.VideoEdit = backStackEntry.toRoute()
val chatId = route.chatId
val videoUri = route.uri
VideoEditScreen(
chatId = chatId,
uri = videoUri,
onCloseButtonClicked = { navController.popBackStack() },
navController = navController,
)
}

composable<Route.VideoPlayer> { backStackEntry ->
val route: Route.VideoPlayer = backStackEntry.toRoute()
val videoUri = route.uri
VideoPlayerScreen(
uri = videoUri,
onCloseButtonClicked = { navController.popBackStack() },
)
}


 */
