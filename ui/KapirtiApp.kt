package com.kapirti.social_chat_food_video.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kapirti.social_chat_food_video.ui.theme.KapirtiTheme
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkManager
import com.kapirti.social_chat_food_video.common.func.InterstitialAdManager
import com.kapirti.social_chat_food_video.core.data.NetworkMonitor
import kotlinx.coroutines.CoroutineScope

@Composable
fun KapirtiApp(
    isDarkTheme: Boolean,
    interstitialAdManager: InterstitialAdManager,
    networkMonitor: NetworkMonitor,
    shortcutParams: ShortcutParams?,
    appState: KapirtiAppState = rememberAppState(
        networkMonitor = networkMonitor,
    ),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val modifier = Modifier.fillMaxSize()

    KapirtiTheme(isDarkTheme) {
        val isOffline by appState.isOffline.collectAsStateWithLifecycle()
        val notConnectedMessage = stringResource(AppText.not_connected)


        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { innerPadding ->
            KapirtiNavGraph(modifier, interstitialAdManager, shortcutParams)
        }


        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = SnackbarDuration.Indefinite,
                )
            }
        }
    }
}



/**



val clearAndNavigateFlirt: () -> Unit = {
navController.navigate(LuccaDestinations.USERS_FLIRT_ROUTE){
launchSingleTop = true
popUpTo(0) { inclusive = true }
}
}





val navigateAndPopUpRestaurantToChatExist: () -> Unit = {
navController.navigate(LuccaDestinations.CHATEXIST_ROUTE){
launchSingleTop = true
popUpTo(LuccaDestinations.RESTAURANT_ROUTE){ inclusive = true }
}
}
val navigateAndPopUpRestaurantToChatNope: () -> Unit = {
navController.navigate(LuccaDestinations.CHATNOPE_ROUTE){
launchSingleTop = true
popUpTo(LuccaDestinations.RESTAURANT_ROUTE){ inclusive = true }
}
}


val navigateGalleryTimeline: () -> Unit = {
navController.navigate(LuccaDestinations.GALLERY_TIMELINE_ROUTE){ launchSingleTop = true }
}
/**    val navigateWallet: () -> Unit = {
navController.navigate(LuccaDestinations.WALLET_ROUTE){ launchSingleTop = true }
}*/
val navigateChatExist: () -> Unit = {
navController.navigate(LuccaDestinations.CHATEXIST_ROUTE){ launchSingleTop = true }
}
/**    val navigateBlockedUser: () -> Unit = {
navController.navigate(LuccaDestinations.BLOCKED_USERS_ROUTE){ launchSingleTop = true }
}*/
val navigateCallInfo: () -> Unit = {
navController.navigate(LuccaDestinations.CALL_INFO_ROUTE){ launchSingleTop = true }
}
/** val navigateTimeline: () -> Unit = {
navController.navigate(LuccaDestinations.TIMELINE_ROUTE){ launchSingleTop = true }
}


val navigateToTimeline: () -> Unit = {
navController.navigate(LuccaDestinations.TIMELINE_ROUTE) {
popUpTo(navController.graph.findStartDestination().id) {
saveState = true
}
launchSingleTop = true
restoreState = true
}
}*/



/**
 *
val navigateVideoCall: () -> Unit = {
navController.navigate(ZepiDestinations.VIDEO_CALL_ROUTE){ launchSingleTop = true }
}
 * */
 */

/**

@Composable
fun LuccaNavGraph(
restartApp : () -> Unit,
restartAppEdit: () -> Unit,
restartAppProfile: () -> Unit,
restartAppFlirt: () -> Unit,
restartAppSettings: () -> Unit,
popUpScreen: () -> Unit,

navigateLogin: () -> Unit,


navigateChatsToChatExist: () -> Unit,

isExpandedScreen: Boolean,
modifier: Modifier = Modifier,
openDrawer: () -> Unit = {},
closeDrawer: () -> Unit = {},
sizeAwareDrawerState: DrawerState,
navController: NavHostController = rememberNavController(),
startDestination: String = LuccaDestinations.SPLASH_ROUTE,
currentRoute: String,


//navigateToSubscriptions: () -> Unit,
navigateToSettings: () -> Unit,
navigateToProfile: () -> Unit,
navigateToChats: () -> Unit,
navigateToBookmarks: () -> Unit,
//navigateToTimeline: () -> Unit,
//navigateToHome: () -> Unit,
navigateToUsersFlirt: () -> Unit,
navigateToUsersMarriage: () -> Unit,
navigateToLanguagePractice: () -> Unit,
navigateToRestaurants: () -> Unit,
navigateToCafes: () -> Unit,
navigateToHotels: () -> Unit,
//navigateToAddPost: () -> Unit,
navigateToTetris: () -> Unit,
navigateToGameRacing: () -> Unit,


payState: PaymentUiState,
onGooglePayButtonClick: () -> Unit,
shortcutParams: ShortcutParams?,

sensorManager: SensorManager,
accelerometer: Sensor?
) {
val activity = LocalContext.current as Activity
// val navController = rememberNavController()

navController.addOnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
// Lock the layout of the Camera screen to portrait so that the UI layout remains
// constant, even on orientation changes. Note that the camera is still aware of
// orientation, and will assign the correct edge as the bottom of the photo or video.
if (navDestination.route?.contains("camera") == true) {
activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
} else {
activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}
}

ModalNavigationDrawer(
drawerContent = {
AppDrawer(
currentRoute = currentRoute,
//navigateToTimeline = navigateToTimeline,
//navigateToHome = navigateToHome,
navigateToFlirt = navigateToUsersFlirt,
navigateToMarriage = navigateToUsersMarriage,
navigateToLanguagePractice = navigateToLanguagePractice,
navigateToRestaurants = navigateToRestaurants,
navigateToCafes = navigateToCafes,
navigateToHotels = navigateToHotels,
//navigateToAddPost = navigateToAddPost,
navigateToChats = navigateToChats,
navigateToProfile = navigateToProfile,
navigateToSettings = navigateToSettings,
navigateToBookmarks = navigateToBookmarks,
navigateToTetris = navigateToTetris,
navigateToGameRacing = navigateToGameRacing,
closeDrawer = closeDrawer,
)
},
drawerState = sizeAwareDrawerState,
gesturesEnabled = !isExpandedScreen
) {
Row {
if (isExpandedScreen) {
AppNavRail(
currentRoute = currentRoute,
//navigateToTimeline = navigateToTimeline,
navigateToSettings = navigateToSettings,
//navigateToHome = navigateToHome,
//navigateToAddPost = navigateToAddPost,
navigateToChats = navigateToChats,
navigateToProfile = navigateToProfile,
)
}

NavHost(
navController = navController,
startDestination = startDestination,
modifier = modifier,
) {
/**composable(
route = LuccaDestinations.TIMELINE_ROUTE,
) {
TimelineRoute(
openDrawer = openDrawer,
isExpandedScreen = isExpandedScreen,
navigateLogin = navigateLogin,
navigateEdit = navigateEdit,
// includeUserIdViewModel = includeUserIdViewModel,
modifier = modifier,
//navigateUserProfile = navigateUserProfile,
//  navigateLike = navigateLike
)
}*/
/**  composable(LuccaDestinations.HOME_ROUTE) {
HomeRoute(
openDrawer = openDrawer,
navigateSearch = navigateSearch,
navigateUserProfile = navigateUserProfile,
includeUserIdViewModel = includeUserIdViewModel,
)
}*/


composable(LuccaDestinations.HOTELS_ROUTE){
HotelsRoute(
isExpandedScreen = isExpandedScreen,
includeHotelViewModel = includeHotelViewModel,
popUp = popUpScreen,
openDrawer = openDrawer,
navigateHotel = navigateHotel,
navigateEdit = navigateEdit
)
}
composable(LuccaDestinations.RESTAURANTS_ROUTE){
RestaurantsRoute(
isExpandedScreen = isExpandedScreen,
includeRestaurantViewModel = includeRestaurantViewModel,
popUp = popUpScreen,
openDrawer = openDrawer,
navigateRestaurant = navigateRestaurant,
navigateEdit = navigateEdit
)
}
composable(LuccaDestinations.CAFES_ROUTE){
CafesRoute(
isExpandedScreen = isExpandedScreen,
includeCafeViewModel = includeCafeViewModel,
popUp = popUpScreen,
openDrawer = openDrawer,
navigateCafe = navigateCafe,
navigateEdit = navigateEdit
)
}
/** composable(LuccaDestinations.ADD_POST_ROUTE) {
AddPostRoute(
navigateLogin = navigateLogin,
navigateEdit = navigateEdit,
isExpandedScreen = isExpandedScreen,
openDrawer = openDrawer,
)
}*/
composable(route = LuccaDestinations.CHATS_ROUTE) {
ChatsRoute(
isExpandedScreen = isExpandedScreen,
openDrawer = openDrawer,
navigateLogin = navigateLogin,
navigateRegister = navigateRegister,
navigateCallInfo = navigateCallInfo,
modifier = Modifier.fillMaxSize(),
includeUserCallViewModel = includeUserCallViewModel,
includeAccountInfoViewModel = includeAccountInfoViewModel,
navigateChatsToChatExist = navigateChatsToChatExist
)
}
composable(LuccaDestinations.BOOKMARKS_ROUTE){
BookmarksRoute(
isExpandedScreen = isExpandedScreen,
includeCafeViewModel = includeCafeViewModel,
includeHotelViewModel = includeHotelViewModel,
includeRestaurantViewModel = includeRestaurantViewModel,
openDrawer = openDrawer,
navigateLogin = navigateLogin,
navigateRegister = navigateRegister,
navigateCafe = navigateCafe,
navigateRestaurant = navigateRestaurant,
navigateHotel = navigateHotel
)
}

/**                composable(LuccaDestinations.SUBSCRIPTIONS_ROUTE) {
SubscribeScreen()
}*/
composable(LuccaDestinations.GAME_RACING_ROUTE){
GameRacingRoute(
sensorManager = sensorManager, accelerometer = accelerometer
)
}
composable(LuccaDestinations.TETRIS_ROUTE){
TetrisRoute()
}

/**
/**   composable(
route = "chat/{chatId}?text={text}",
arguments = listOf(
navArgument("chatId") { type = NavType.LongType },
navArgument("text") { defaultValue = "" },
),
deepLinks = listOf(
navDeepLink {
action = Intent.ACTION_VIEW
uriPattern = "https://socialite.google.com/chat/{chatId}"
},
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
val text = backStackEntry.arguments?.getString("text")
ChatScreen(
chatId = chatId,
foreground = true,
onBackPressed = { navController.popBackStack() },
onCameraClick = { navController.navigate("chat/$chatId/camera") },
onPhotoPickerClick = {},//{ navController.navigateToPhotoPicker(chatId) },
onVideoClick = { uri -> navController.navigate("videoPlayer?uri=$uri") },
prefilledText = text,
modifier = Modifier.fillMaxSize(),
)
}*/
/**   composable(
route = "chat/{chatId}?text={text}",
arguments = listOf(
navArgument("chatId") { type = NavType.LongType },
navArgument("text") { defaultValue = "" },
),
deepLinks = listOf(
navDeepLink {
action = Intent.ACTION_VIEW
uriPattern = "https://socialite.google.com/chat/{chatId}"
},
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
val text = backStackEntry.arguments?.getString("text")
ChatScreen(
chatId = chatId,
foreground = true,
onBackPressed = { navController.popBackStack() },
onCameraClick = { navController.navigate("chat/$chatId/camera") },
onPhotoPickerClick = {},//{ navController.navigateToPhotoPicker(chatId) },
onVideoClick = { uri -> navController.navigate("videoPlayer?uri=$uri") },
prefilledText = text,
modifier = Modifier.fillMaxSize(),
)
}*/
/**   composable(
route = "chat/{chatId}?text={text}",
arguments = listOf(
navArgument("chatId") { type = NavType.LongType },
navArgument("text") { defaultValue = "" },
),
deepLinks = listOf(
navDeepLink {
action = Intent.ACTION_VIEW
uriPattern = "https://socialite.google.com/chat/{chatId}"
},
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
val text = backStackEntry.arguments?.getString("text")
ChatScreen(
chatId = chatId,
foreground = true,
onBackPressed = { navController.popBackStack() },
onCameraClick = { navController.navigate("chat/$chatId/camera") },
onPhotoPickerClick = {},//{ navController.navigateToPhotoPicker(chatId) },
onVideoClick = { uri -> navController.navigate("videoPlayer?uri=$uri") },
prefilledText = text,
modifier = Modifier.fillMaxSize(),
)
}*/

/**   composable(
route = "chat/{chatId}?text={text}",
arguments = listOf(
navArgument("chatId") { type = NavType.LongType },
navArgument("text") { defaultValue = "" },
),
deepLinks = listOf(
navDeepLink {
action = Intent.ACTION_VIEW
uriPattern = "https://socialite.google.com/chat/{chatId}"
},
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
val text = backStackEntry.arguments?.getString("text")
ChatScreen(
chatId = chatId,
foreground = true,
onBackPressed = { navController.popBackStack() },
onCameraClick = { navController.navigate("chat/$chatId/camera") },
onPhotoPickerClick = {},//{ navController.navigateToPhotoPicker(chatId) },
onVideoClick = { uri -> navController.navigate("videoPlayer?uri=$uri") },
prefilledText = text,
modifier = Modifier.fillMaxSize(),
)
}*/
composable(
route = "chat/{chatId}/camera",
arguments = listOf(
navArgument("chatId") { type = NavType.LongType },
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
Camera(
onMediaCaptured = { capturedMedia: Media? ->
when (capturedMedia?.mediaType) {
MediaType.PHOTO -> {
navController.popBackStack()
}

MediaType.VIDEO -> {
navController.navigate("videoEdit?uri=${capturedMedia.uri}&chatId=$chatId")
}

else -> {
// No media to use.
navController.popBackStack()
}
}
},
chatId = chatId,
modifier = Modifier.fillMaxSize(),
)
}

// Invoke PhotoPicker to select photo or video from device gallery
/**        photoPickerScreen(
onPhotoPicked = navController::popBackStack,
)*/
/**        photoPickerScreen(
onPhotoPicked = navController::popBackStack,
)*/
/**        photoPickerScreen(
onPhotoPicked = navController::popBackStack,
)*/
/**        photoPickerScreen(
onPhotoPicked = navController::popBackStack,
)*/

composable(
route = "videoEdit?uri={videoUri}&chatId={chatId}",
arguments = listOf(
navArgument("videoUri") { type = NavType.StringType },
navArgument("chatId") { type = NavType.LongType },
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
val videoUri = backStackEntry.arguments?.getString("videoUri") ?: ""
VideoEditScreen(
chatId = chatId,
uri = videoUri,
onCloseButtonClicked = { navController.popBackStack() },
navController = navController,
)
}
composable(
route = "videoPlayer?uri={videoUri}",
arguments = listOf(
navArgument("videoUri") { type = NavType.StringType },
),
) { backStackEntry ->
val videoUri = backStackEntry.arguments?.getString("videoUri") ?: ""
VideoPlayerScreen(
uri = videoUri,
onCloseButtonClicked = { navController.popBackStack() },
)
}
*/


/** composable(LuccaDestinations.SEARCH_ROUTE) {
SearchScreen(
popUp = popUpScreen,
navigateAndPopUpSearchToUserProfile = navigateAndPopUpSearchToUserProfile,
includeUserIdViewModel = includeUserIdViewModel
)
}*/


composable(LuccaDestinations.HOTEL_ROUTE) {
HotelRoute(
isExpandedScreen = isExpandedScreen,
popUp = popUpScreen,
navigateLogin = navigateLogin,
navigateAndPopUpHotelToChatExist = navigateAndPopUpHotelToChatExist,
navigateAndPopUpHotelToChatNope = navigateAndPopUpHotelToChatNope,
navigateGallery = navigateGallery,
includeHotelViewModel = includeHotelViewModel,
includeAccountInfoViewModel = includeAccountInfoViewModel
)
}
composable(LuccaDestinations.RESTAURANT_ROUTE) {
RestaurantRoute(
isExpandedScreen = isExpandedScreen,
popUp = popUpScreen,
navigateLogin = navigateLogin,
navigateAndPopUpRestaurantToChatExist = navigateAndPopUpRestaurantToChatExist,
navigateAndPopUpRestaurantToChatNope = navigateAndPopUpRestaurantToChatNope,
navigateGallery = navigateGallery,
includeRestaurantViewModel = includeRestaurantViewModel,
includeAccountInfoViewModel = includeAccountInfoViewModel
)
}
composable(LuccaDestinations.GALLERY_ROUTE){
GalleryRoute(
isExpandedScreen = isExpandedScreen,
popUp = popUpScreen,
navigateLogin = navigateLogin,
//navigateWallet = navigateWallet,
navigateGalleryTimeline = navigateGalleryTimeline,
)
}
/**
composable(LuccaDestinations.USER_PROFILE_ROUTE) {
UserProfileRoute(
popUp = popUpScreen,
navigateLogin = navigateLogin,
navigateGallery = navigateGallery,
navigateAndPopUpUserProfileToChatExist = navigateAndPopUpUserProfileToChatExist,
navigateAndPopUpUserProfileToChatNope = navigateAndPopUpUserProfileToChatNope,
includeUserUidViewModel = includeUserIdViewModel,
//navigateVideoCall = {}, //navigateVideoCall,
)
}

composable(LuccaDestinations.GALLERY_TIMELINE_ROUTE){
GalleryTimelineRoute(
isExpandedScreen = isExpandedScreen,
popUp = popUpScreen,
navigateLogin = navigateLogin,
navigateWallet = navigateWallet,
includeUserUidViewModel = includeUserIdViewModel,
)
}*/


composable(route = LuccaDestinations.CALL_INFO_ROUTE) {
CallInfoScreen(
popUp = popUpScreen,
includeUserCallViewModel = includeUserCallViewModel
)
}

/**
// composable(ZepiDestinations.VIDEO_CALL_ROUTE) { VideoCallScreen(popUp = popUpScreen) }
composable(
route = LuccaDestinations.BLOCKED_USERS_ROUTE
) {
BlockedUsersRoute(
isExpandedScreen = isExpandedScreen,
popUp = popUpScreen
)
}*/
/**                composable(route = LuccaDestinations.WALLET_ROUTE){
WalletRoute(
payState = payState,
isExpandedScreen = isExpandedScreen,
onGooglePayButtonClick = onGooglePayButtonClick,
popUp = popUpScreen,
navigateLogin = navigateLogin,
navigateRegister = navigateRegister,
)
}*/



/**           composable(
route = "chat/{chatId}?text={text}",
arguments = listOf(
navArgument("chatId") { type = NavType.LongType },
//                navArgument("userId") { type = NavType.StringType },
navArgument("text") { defaultValue = "" },
),
deepLinks = listOf(
navDeepLink {
action = Intent.ACTION_VIEW
uriPattern = "https://socialite.google.com/chat/{chatId}"
},
),
) { backStackEntry ->
val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
//  val userId = backStackEntry.arguments?.getString("userId") ?: "userid"
val text = backStackEntry.arguments?.getString("text")
ChatScreen(
chatId = chatId,
//userUid = userId,
foreground = true,
onBackPressed = { navController.popBackStack() },
onCameraClick = { navController.navigate("chat/$chatId/camera") },
onPhotoPickerClick = {},//{ navController.navigateToPhotoPicker(chatId) },
onVideoClick = { uri -> navController.navigate("videoPlayer?uri=$uri") },
prefilledText = text,
modifier = Modifier.fillMaxSize(),
)
}*/
}
}
}

if (shortcutParams != null) {
// val chatId = extractChatId(shortcutParams.shortcutId)
//val text = shortcutParams.text
//navController.navigate("chat/$chatId?text=$text")
}
}

data class ShortcutParams(
val shortcutId: String,
val text: String,
)


/**
const val POST_ID = "postId"

@SuppressLint("NewApi")
@Composable
fun (

) {
NavHost(
navController = navController,
startDestination = startDestination,
modifier = modifier
) {

*/


/**

composable(QChatDestinations.ARCHIVE_ROUTE){
ArchiveScreen(openChatExistScreen, includeChatViewModel = includeChatViewModel)
}
composable(QChatDestinations.CHAT_ROUTE_EXIST){
ChatScreen(
popUp = popUpScreen,
navigateUserProfile = navigateUserProfile,
includeUserUidViewModel = includeUserUidViewModel,
includeChatViewModel = includeChatViewModel,
showInterstialAd = showInterstitialAds)
}



composable(QChatDestinations.CHATS_ROUTE){
ChatsRoute(
chatsToArchive = chatsToArchive,
chatsToChat = chatsToChat,
includeChatViewModel = includeChatViewModel,
isExpandedScreen = isExpandedScreen,
openDrawer = openDrawer
)
}

*/
 *
 *
 *
 *
 *
 *
 *

*/
/**
@Composable
fun LuccaApp(
    shortcutParams: ShortcutParams?,
    widthSizeClass: WindowWidthSizeClass,
    isDarkTheme: Boolean,
    payState: PaymentUiState,
    onGooglePayButtonClick: () -> Unit,
    sensorManager: SensorManager,
    accelerometer: Sensor?,

) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }


    LuccaTheme(isDarkTheme) {


        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            LuccaNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: LuccaDestinations.USERS_FLIRT_ROUTE

        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)


    }
}

@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        drawerState
    } else {
        DrawerState(DrawerValue.Closed)
    }
}
*/
@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor,
): KapirtiAppState {
    return remember(coroutineScope, networkMonitor) {
        KapirtiAppState(coroutineScope, networkMonitor)
    }
}
/**
@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}
*/
