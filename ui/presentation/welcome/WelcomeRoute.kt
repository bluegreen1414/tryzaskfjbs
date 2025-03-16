package com.kapirti.social_chat_food_video.ui.presentation.welcome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kapirti.social_chat_food_video.common.composable.HomeBackground

@Composable
fun WelcomeRoute(
    navigateAndPopUpWelcomeToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
       // topBar = { AdsBannerToolbar(ADS_WELCOME_BANNER_ID) },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        HomeBackground(modifier = Modifier.fillMaxSize())
        WelcomeScreen(
            navigateAndPopUpWelcomeToLogin = navigateAndPopUpWelcomeToLogin,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
