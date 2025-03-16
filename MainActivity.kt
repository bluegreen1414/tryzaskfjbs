package com.kapirti.social_chat_food_video

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import com.kapirti.social_chat_food_video.common.func.InterstitialAdManager
import com.kapirti.social_chat_food_video.core.data.NetworkMonitor
import com.kapirti.social_chat_food_video.model.Theme
import com.kapirti.social_chat_food_video.ui.KapirtiApp
import com.kapirti.social_chat_food_video.ui.ShortcutParams
import com.kapirti.social_chat_food_video.ui.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: MainViewModel by viewModels()
    private lateinit var interstitialAdManager: InterstitialAdManager

    @OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        //installSplashScreen()
//        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        super.onCreate(savedInstanceState)

        val bundle = intent!!.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                val value = bundle[key]
                android.util.Log.d("myTag", "Main activity Key: $key Value: $value")
                if (key == "theSender") {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.setData(("https://socialite.google.com/chat/" + value.toString()).toUri())
                    startActivity(intent)
                }
            }
        }

        interstitialAdManager = InterstitialAdManager(this)
        setContent {
            val theme by viewModel.theme

            val isDarkTheme = when (theme) {
                Theme.Dark -> true
                Theme.Light -> false
            }

            KapirtiApp(
                isDarkTheme = isDarkTheme,
                interstitialAdManager = interstitialAdManager,
                networkMonitor = networkMonitor,
                shortcutParams = extractShortcutParams(intent),
            )

            KeepScreenOn()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.saveIsOnline()
    }
    override fun onPause() {
        super.onPause()
        viewModel.saveLastSeen()
    }

    private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
        if (intent == null || intent.action != Intent.ACTION_SEND) return null
        val shortcutId = intent.getStringExtra(
            ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
        ) ?: return null
        val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
        return ShortcutParams(shortcutId, text)
    }
}

@Composable
private fun KeepScreenOn() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}


//

/**
private fun scheduleDailyNotification(context: Context) {
    val currentTime = Calendar.getInstance()
    val targetTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    // Eğer saat 12'yi geçtikse, ertesi gün için ayarlıyoruz
    if (currentTime.after(targetTime)) {
        targetTime.add(Calendar.DAY_OF_YEAR, 1)
    }

    val delayMillis = targetTime.timeInMillis - currentTime.timeInMillis

    // Worker için zamanlayıcıyı ayarlıyoruz
    val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
        .build()

    // Zamanlanmış işi enqueue ediyoruz
    WorkManager.getInstance(context).enqueue(notificationRequest)
}

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Bildirim gönderme fonksiyonu
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_notification_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Daily Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Günlük Bildirim")
            .setContentText("Saat 12'deki bildirim!")
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}*/
/**
launchCatching {
try {
val user = accountService.linkAccount(email, password)
firestoreService.saveUser(
User(
id = user.uid,
name = user.displayName ?: user.email ?: ""
)
)
navigateAndPopUpRegisterToEdit()
} catch (ex: FirebaseAuthException) {
launchCatching { snackbarHostState.showSnackbar(ex.localizedMessage ?: "") }
onButtonChange()
throw ex
}
}
}


data class SettingsUiState(val isAnonymousAccount: Boolean = true, val email: String = "")









import androidx.activity.compose.setContent
import com.kapirti.social_chat_food_video.ui.presentation.crane.calendar.launchCalendarActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.DpPropKey
//import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
//import androidx.compose.animation.transition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kapirti.social_chat_food_video.ui.presentation.crane.base.CraneScaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.ui.presentation.crane.ui.CraneHome
import com.kapirti.social_chat_food_video.ui.presentation.crane.ui.LandingScreen
import com.kapirti.social_chat_food_video.ui.presentation.crane.ui.OnExploreItemClicked
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)

setContent {
MainScreen(
onExploreItemClicked = { },//launchDetailsActivity(context = this, item = it) },
onDateSelectionClicked = { launchCalendarActivity(this) }
)
}
}
}

@VisibleForTesting
@Composable
fun MainScreen(onExploreItemClicked: OnExploreItemClicked, onDateSelectionClicked: () -> Unit) {
CraneScaffold {
//var splashShown by remember { mutableStateOf(SplashState.Shown) }
//val transition = transition(splashTransitionDefinition, splashShown)
Box {
/**LandingScreen(
modifier = Modifier.alpha(transition[splashAlphaKey]),
onTimeout = { splashShown = SplashState.Completed }
)*/
MainContent(
// modifier = Modifier.alpha(transition[contentAlphaKey]),
// topPadding = transition[contentTopPaddingKey],
onExploreItemClicked = onExploreItemClicked,
onDateSelectionClicked = onDateSelectionClicked
)
}
}
}


@Composable
private fun MainContent(
modifier: Modifier = Modifier,
topPadding: Dp = 0.dp,
onExploreItemClicked: OnExploreItemClicked,
onDateSelectionClicked: () -> Unit
) {
Column(modifier = modifier) {
Spacer(Modifier.padding(top = topPadding))
CraneHome(
modifier = modifier,
onExploreItemClicked = onExploreItemClicked,
onDateSelectionClicked = onDateSelectionClicked
)
}
}








/**
enum class SplashState { Shown, Completed }

private val splashAlphaKey = FloatPropKey("Splash alpha")
private val contentAlphaKey = FloatPropKey("Content alpha")
private val contentTopPaddingKey = DpPropKey("Top padding")

private val splashTransitionDefinition = transitionDefinition<SplashState> {
state(SplashState.Shown) {
this[splashAlphaKey] = 1f
this[contentAlphaKey] = 0f
this[contentTopPaddingKey] = 100.dp
}
state(SplashState.Completed) {
this[splashAlphaKey] = 0f
this[contentAlphaKey] = 1f
this[contentTopPaddingKey] = 0.dp
}
transition {
splashAlphaKey using tween(
durationMillis = 100
)
contentAlphaKey using tween(
durationMillis = 300
)
contentTopPaddingKey using spring(
stiffness = StiffnessLow
)
}
}

*/
 */
/**

/**
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContent {
MultiArrowOverlay()
}
}
}

@Composable
private fun MultiArrowOverlay(){
Box(modifier = Modifier.fillMaxSize()) {
Canvas(modifier = Modifier.fillMaxSize()) {
val arrows = listOf(
ArrowData(
startX = size.width * 0.5f, startY = size.height * 0.8f,
endX = size.width * 0.5f, endY = size.height * 0.6f
),
ArrowData(
startX = size.width * 0.7f, startY = size.height * 0.7f,
endX = size.width * 0.55f, endY = size.height * 0.5f
),
ArrowData(
startX = size.width * 0.3f, startY = size.height * 0.65f,
endX = size.width * 0.4f, endY = size.height * 0.45f
)
)

arrows.forEach { arrow ->
drawLine(
color = Color.Green,
start = Offset(arrow.startX, arrow.startY),
end = Offset(arrow.endX, arrow.endY),
strokeWidth = 8f
)

val arrowHeadSize = 40f
val angle = Math.atan2(
(arrow.endY - arrow.startY).toDouble(),
(arrow.endX - arrow.startX).toDouble()
).toFloat()

drawLine(
color = Color.Green,
start = Offset(arrow.endX, arrow.endY),
end = Offset(
arrow.endX - arrowHeadSize * kotlin.math.cos(angle - Math.PI.toFloat() / 6),
arrow.endY - arrowHeadSize * kotlin.math.sin(angle - Math.PI.toFloat() / 6)
),
strokeWidth = 8f
)
drawLine(
color = Color.Green,
start = Offset(arrow.endX, arrow.endY),
end = Offset(
arrow.endX - arrowHeadSize * kotlin.math.cos(angle + Math.PI.toFloat() / 6),
arrow.endY - arrowHeadSize * kotlin.math.sin(angle + Math.PI.toFloat() / 6)
),
strokeWidth = 8f
)
}
}

Text(
text = "birinci ozellık",
modifier = Modifier
.offset(x = 0.dp, y = (-150).dp)
.align(Alignment.Center),
color = Color.Yellow
)
Text(
text = "ikinci ozellık",
modifier = Modifier
.offset(x = 80.dp, y = (-220).dp)
.align(Alignment.CenterEnd),
color = Color.Yellow
)
Text(
text = "ucuncu ozellık",
modifier = Modifier
.offset(x = (-80).dp, y = (-180).dp)
.align(Alignment.CenterStart),
color = Color.Yellow
)
}
}


data class ArrowData(
val startX: Float,
val startY: Float,
val endX: Float,
val endY: Float
)

*/


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.content.pm.ShortcutManagerCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.kapirti.social_chat_food_video.core.data.NetworkMonitor
import com.kapirti.social_chat_food_video.model.Theme
import com.kapirti.social_chat_food_video.ui.LuccaApp
import com.kapirti.social_chat_food_video.ui.ShortcutParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import com.kapirti.social_chat_food_video.ui.presentation.pay.wallet.CheckoutViewModel
import com.kapirti.social_chat_food_video.ui.presentation.pay.wallet.PaymentUiState






@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val paymentDataLauncher = registerForActivityResult(GetPaymentDataResult()) { taskResult ->
when (taskResult.status.statusCode) {
CommonStatusCodes.SUCCESS -> {
taskResult.result!!.let {
Log.i("Google Pay result:", it.toJson())
model.setPaymentData(it)
}
}
//CommonStatusCodes.CANCELED -> The user canceled
//AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
//CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
}
}

private val model: CheckoutViewModel by viewModels()
private val viewModel: MainViewModel by viewModels()



@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
//enableEdgeToEdge()
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
window.isNavigationBarContrastEnforced = false
}
super.onCreate(savedInstanceState)
// runBlocking { SociaLiteAppWidget().updateAll(this@MainActivity) }
setContent {
val payState: PaymentUiState by model.paymentUiState.collectAsStateWithLifecycle()
val theme by viewModel.theme

val isDarkTheme = when (theme) {
Theme.Dark -> true
Theme.Light -> false
}

val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

LuccaApp(
shortcutParams = extractShortcutParams(intent),
widthSizeClass = widthSizeClass,
networkMonitor = networkMonitor,
isDarkTheme = isDarkTheme,
sensorManager = sensorManager,
accelerometer = accelerometer,
payState = payState,
onGooglePayButtonClick = this::requestPayment,
)

inAppReview(this)
}
}

private fun requestPayment() {
val task = model.getLoadPaymentDataTask(priceCents = 1000L)
task.addOnCompleteListener(paymentDataLauncher::launch)
}
private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
if (intent == null || intent.action != Intent.ACTION_SEND) return null
val shortcutId = intent.getStringExtra(
ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
) ?: return null
val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
return ShortcutParams(shortcutId, text)
}



override fun onBackPressed() {
super.onBackPressed()
finishAffinity()
}
}














/**
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kapirti.social_chat_food_video.whatsappclose.core.navigation.AppComposeNavigator
import com.kapirti.social_chat_food_video.whatsappclose.ui.WhatsAppCloneMain
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

@Inject
internal lateinit var appComposeNavigator: AppComposeNavigator

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)

setContent { WhatsAppCloneMain(appComposeNavigator) }
}
}

*/








//voice call mainactivity
/**
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.kapirti.social_chat_food_video.voicecall.AudioCallSampleApp
import com.kapirti.social_chat_food_video.voicecall.ui.CustomCallActivity
import com.kapirti.social_chat_food_video.voicecall.ui.screens.MainScreen
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.AutoStartPermissionInfo.alreadyAskedForAutoStart
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.AutoStartPermissionInfo.showAutoStartPermissionRequest
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.componentNames
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.isAudioPermissionGranted
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.isIgnoringBatteryOptimizations
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.requestAudioPermission
import com.kapirti.social_chat_food_video.voicecall.utils.permissions.requestIgnoreBatteryOptimizations
import com.kapirti.social_chat_food_video.voicecall.viewmodel.MainViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RingingState
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.StreamCallId
import io.getstream.video.android.ui.common.StreamCallActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
// This is just the simplest and fastest way to create the view model without any dependencies
// In a real app you should utilize a different method of creating the view model.
private val viewModel: MainViewModel = MainViewModel(AudioCallSampleApp.instance)

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)

// If the app is in foreground during an incoming call, the StreamCallActivity would be launched.
showComposeCallActivityOnIncomingCall()

val resultLauncher = registerForActivityResult(
ActivityResultContracts.RequestPermission(),
) { granted ->
// Handle the permissions result here
if (!granted) {
Toast.makeText(
this@MainActivity,
"Permission for audio needs to be granted.",
Toast.LENGTH_LONG
).show()
}
}

// Proceed with set content
setContent {
VideoTheme {
// A surface container using the 'background' color from the theme
Surface(
modifier = Modifier.fillMaxSize(), color = VideoTheme.colors.basePrimary
) {
val context = LocalContext.current
val userUiState = viewModel.userState
MainScreen(userState = userUiState, onLogin = { userId, token ->
viewModel.login(context = context, userId = userId, token = token)
}, onLogout = {
viewModel.logout(context = context)
}, onDial = { members ->
if (isAudioPermissionGranted()) {
startOutgoingCallActivity(members)
} else {
resultLauncher.requestAudioPermission()
}
})
}
}
}
}

override fun onResume() {
super.onResume()
checkBatteryAndAutoStartPermissions()
}

private fun checkBatteryAndAutoStartPermissions() {
if (!isIgnoringBatteryOptimizations()) {
// If we are under battery optimization request to disable it.
requestIgnoreBatteryOptimizations()
}
if (!alreadyAskedForAutoStart()) {
// We need to check if we can ask for auto start permission.
showAutoStartPermissionRequest(componentNames)
}
}

/*
Monitors the ringingCall and if any, starts the default call activity.
*/
@OptIn(ExperimentalCoroutinesApi::class)
private fun showComposeCallActivityOnIncomingCall() {
lifecycleScope.launch {
StreamVideo.instanceState.flatMapLatest { instance ->
instance?.state?.ringingCall ?: flowOf(null)
}.collectLatest { call ->
if (call != null) {
lifecycleScope.launch {
// Monitor the ringingState on a non-null call
call.state.ringingState.collectLatest {
if (it is RingingState.Incoming) {
startIncomingCallActivity(call)
}
}
}
}
}
}
}

private fun startOutgoingCallActivity(members: List<String>) {
// All permissions were granted
// ensure that audio permission is granted
val intent = StreamCallActivity.callIntent(
this,
StreamCallId(
"audio_call", UUID.randomUUID().toString()
),
members,
true,
action = NotificationHandler.ACTION_OUTGOING_CALL,
// use ComposeStreamCallActivity::class.java for default
clazz = CustomCallActivity::class.java,
)
startActivity(intent)
}

// Same as outgoing, but with different action
private fun startIncomingCallActivity(call: Call) {
val intent = StreamCallActivity.callIntent(
this@MainActivity,
StreamCallId.fromCallCid(call.cid),
emptyList(),
true,
NotificationHandler.ACTION_INCOMING_CALL,
// use ComposeStreamCallActivity::class.java for default behavior
CustomCallActivity::class.java,
)
startActivity(intent)
}
}


*/








// normal main activity below


/**
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.content.pm.ShortcutManagerCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.kapirti.social_chat_food_video.core.data.NetworkMonitor
import com.kapirti.social_chat_food_video.model.Theme
import com.kapirti.social_chat_food_video.ui.LuccaApp
import com.kapirti.social_chat_food_video.ui.ShortcutParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.util.Log
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import com.kapirti.social_chat_food_video.ui.presentation.wallet.CheckoutViewModel
import com.kapirti.social_chat_food_video.ui.presentation.wallet.PaymentUiState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val paymentDataLauncher = registerForActivityResult(GetPaymentDataResult()) { taskResult ->
when (taskResult.status.statusCode) {
CommonStatusCodes.SUCCESS -> {
taskResult.result!!.let {
Log.i("Google Pay result:", it.toJson())
model.setPaymentData(it)
}
}
//CommonStatusCodes.CANCELED -> The user canceled
//AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
//CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
}
}

private val model: CheckoutViewModel by viewModels()
private val viewModel: MainViewModel by viewModels()

@Inject
lateinit var networkMonitor: NetworkMonitor

private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
private val accelerometer by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }



@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
//enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
enableEdgeToEdge()
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
window.isNavigationBarContrastEnforced = false
}
super.onCreate(savedInstanceState)
// runBlocking { SociaLiteAppWidget().updateAll(this@MainActivity) }
setContent {
val payState: PaymentUiState by model.paymentUiState.collectAsStateWithLifecycle()
val theme by viewModel.theme

val isDarkTheme = when (theme) {
Theme.Dark -> true
Theme.Light -> false
}

val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

LuccaApp(
shortcutParams = extractShortcutParams(intent),
widthSizeClass = widthSizeClass,
networkMonitor = networkMonitor,
isDarkTheme = isDarkTheme,
sensorManager = sensorManager,
accelerometer = accelerometer,
payState = payState,
onGooglePayButtonClick = this::requestPayment,
)

inAppReview(this)
}
}

private fun requestPayment() {
val task = model.getLoadPaymentDataTask(priceCents = 1000L)
task.addOnCompleteListener(paymentDataLauncher::launch)
}
private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
if (intent == null || intent.action != Intent.ACTION_SEND) return null
val shortcutId = intent.getStringExtra(
ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
) ?: return null
val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
return ShortcutParams(shortcutId, text)
}

override fun onResume() {
super.onResume()
viewModel.saveIsOnline()
}
override fun onPause() {
super.onPause()
viewModel.saveLastSeen()
}
}

private fun inAppReview(context: Context) {
val manager = ReviewManagerFactory.create(context)
val request = manager.requestReviewFlow()

request.addOnCompleteListener { request ->
if(request.isSuccessful) {
val reviewInfo = request.result
val flow = manager.launchReviewFlow(context as Activity, reviewInfo!!)
flow.addOnCompleteListener { _ -> }
}
}
}
*/

//normal place up


/**
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.content.pm.ShortcutManagerCompat
import com.kapirti.video_food_delivery_shopping.core.data.NetworkMonitor
import com.kapirti.video_food_delivery_shopping.ui.ShortcutParams
import com.kapirti.video_food_delivery_shopping.model.Theme
//import com.zepi.social_chat_food.soci.widget.SociaLiteAppWidget
import com.kapirti.video_food_delivery_shopping.ui.ZepiApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val viewModel: MainViewModel by viewModels()

@Inject
lateinit var networkMonitor: NetworkMonitor


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
enableEdgeToEdge()
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
window.isNavigationBarContrastEnforced = false
}
super.onCreate(savedInstanceState)
// runBlocking { SociaLiteAppWidget().updateAll(this@MainActivity) }
setContent {

val theme by viewModel.theme

val isDarkTheme = when (theme) {
Theme.Dark -> true
Theme.Light -> false
}

val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
ZepiApp(
shortcutParams = extractShortcutParams(intent),
widthSizeClass = widthSizeClass,
networkMonitor = networkMonitor,
isDarkTheme = isDarkTheme,
showInterstitialAds = { }//showInterstitialAd() }
)

}
}

private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
if (intent == null || intent.action != Intent.ACTION_SEND) return null
val shortcutId = intent.getStringExtra(
ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
) ?: return null
val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
return ShortcutParams(shortcutId, text)
}
}
*/

/**
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.content.pm.ShortcutManagerCompat
import com.kapirti.video_food_delivery_shopping.core.data.NetworkMonitor
import com.kapirti.video_food_delivery_shopping.ui.ShortcutParams
import com.kapirti.video_food_delivery_shopping.model.Theme
//import com.zepi.social_chat_food.soci.widget.SociaLiteAppWidget
import com.kapirti.video_food_delivery_shopping.ui.ZepiApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val viewModel: MainViewModel by viewModels()

@Inject
lateinit var networkMonitor: NetworkMonitor


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
enableEdgeToEdge()
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
window.isNavigationBarContrastEnforced = false
}
super.onCreate(savedInstanceState)
// runBlocking { SociaLiteAppWidget().updateAll(this@MainActivity) }
setContent {

val theme by viewModel.theme

val isDarkTheme = when (theme) {
Theme.Dark -> true
Theme.Light -> false
}

val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
ZepiApp(
shortcutParams = extractShortcutParams(intent),
widthSizeClass = widthSizeClass,
networkMonitor = networkMonitor,
isDarkTheme = isDarkTheme,
showInterstitialAds = { }//showInterstitialAd() }
)

}
}

private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
if (intent == null || intent.action != Intent.ACTION_SEND) return null
val shortcutId = intent.getStringExtra(
ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
) ?: return null
val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
return ShortcutParams(shortcutId, text)
}
}
*/




/**
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.zepi.social_chat_food.MainViewModel
import com.zepi.social_chat_food.iraaa.QChatApp
import com.zepi.social_chat_food.iraaa.core.data.NetworkMonitor
import com.zepi.social_chat_food.iraaa.model.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private var mInterstitialAd: InterstitialAd? = null


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)

setContent {
val theme by viewModel.theme

val isDarkTheme = when (theme) {
Theme.Dark -> true
Theme.Light -> false
}

val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
QChatApp(
widthSizeClass = widthSizeClass,
networkMonitor = networkMonitor,
isDarkTheme = isDarkTheme,
showInterstitialAds = { }//showInterstitialAd() }
)
// inAppReview(this)
}
}
}

private fun showInterstitialAd() {
if (mInterstitialAd != null) {
mInterstitialAd?.show(this)
loadInterstialAd()
} else {
loadInterstialAd()
}
}

private fun loadInterstialAd() {
var adRequest = AdRequest.Builder().build()

InterstitialAd.load(
this,
ADS_INTERSTITIAL_ID,
adRequest,
object : InterstitialAdLoadCallback() {
override fun onAdFailedToLoad(adError: LoadAdError) {
mInterstitialAd = null
}

override fun onAdLoaded(interstitialAd: InterstitialAd) {
mInterstitialAd = interstitialAd
}
}
)
}
}

private fun inAppReview(context: Context) {
val manager = ReviewManagerFactory.create(context)
val request = manager.requestReviewFlow()

request.addOnCompleteListener { request ->
if(request.isSuccessful) {
val reviewInfo = request.result
val flow = manager.launchReviewFlow(context as Activity, reviewInfo!!)
flow.addOnCompleteListener { _ -> }
}
}
}*/





/**
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.content.pm.ShortcutManagerCompat
import com.kapirti.video_food_delivery_shopping.core.data.NetworkMonitor
import com.kapirti.video_food_delivery_shopping.ui.ShortcutParams
import com.kapirti.video_food_delivery_shopping.model.Theme
//import com.zepi.social_chat_food.soci.widget.SociaLiteAppWidget
import com.kapirti.video_food_delivery_shopping.ui.ZepiApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val viewModel: MainViewModel by viewModels()

@Inject
lateinit var networkMonitor: NetworkMonitor


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
enableEdgeToEdge()
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
window.isNavigationBarContrastEnforced = false
}
super.onCreate(savedInstanceState)
// runBlocking { SociaLiteAppWidget().updateAll(this@MainActivity) }
setContent {

val theme by viewModel.theme

val isDarkTheme = when (theme) {
Theme.Dark -> true
Theme.Light -> false
}

val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
ZepiApp(
shortcutParams = extractShortcutParams(intent),
widthSizeClass = widthSizeClass,
networkMonitor = networkMonitor,
isDarkTheme = isDarkTheme,
showInterstitialAds = { }//showInterstitialAd() }
)

}
}

private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
if (intent == null || intent.action != Intent.ACTION_SEND) return null
val shortcutId = intent.getStringExtra(
ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
) ?: return null
val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
return ShortcutParams(shortcutId, text)
}
}
*/

 */
