package com.kapirti.social_chat_food_video.ui.presentation.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.layout.fillMaxSize
import com.kapirti.social_chat_food_video.common.composable.BackAppBar
import com.kapirti.social_chat_food_video.common.composable.HomeBackground

@Composable
fun SettingsRoute(
    popUp: () -> Unit,
    navigateLogin: () -> Unit,
    navigateRegister: () -> Unit,
    navigateCountry: () -> Unit,
    navigateDeleteAccount: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateTetris: () -> Unit,
    navigateRacingCar: () -> Unit,
    restartApp: () -> Unit,
    navigateBlockedUser: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      //  bottomBar = { AdsBannerToolbar(ConsAds.ADS_SETTINGS_BANNER_ID) },
        modifier = modifier,
        topBar = { BackAppBar(title = AppText.settings_title, popUp) }
    ) { innerPadding ->
        HomeBackground(modifier = Modifier.fillMaxSize())
        SettingsScreen(
            navigateLogin = navigateLogin,
            navigateRegister = navigateRegister,
            navigateDeleteAccount = navigateDeleteAccount,
            restartApp = restartApp,
            navigateFeedback = navigateFeedback,
            navigateCountry = navigateCountry,
            navigateTetris = navigateTetris,
            navigateRacingCar = navigateRacingCar,
            snackbarHostState = snackbarHostState,
            modifier = modifier.padding(innerPadding),
            navigateBlockedUser = navigateBlockedUser,
        )
    }
}
