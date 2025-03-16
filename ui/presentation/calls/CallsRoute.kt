package com.kapirti.social_chat_food_video.ui.presentation.calls

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.BackAppBar
import com.kapirti.social_chat_food_video.common.composable.HomeBackground

@Composable
fun CallsRoute(
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: CallHistoryViewModel = hiltViewModel()
) {
    val calls by viewModel.calls.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
//        bottomBar = { AdsBannerToolbar(ConsAds.ADS_SETTINGS_BANNER_ID) },
        modifier = modifier,
        topBar = { BackAppBar(title = R.string.calls_title, popUp) }
    ) { innerPadding ->
        HomeBackground(Modifier.fillMaxSize())
        CallList(
            recordings = calls,
            innerPadding = innerPadding,
        ) { _, recording ->
            //android.util.Log.d("myTag","Call recording ${recording.id} clicked")
        }
    }
}
