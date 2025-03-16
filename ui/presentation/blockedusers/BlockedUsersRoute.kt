package com.kapirti.social_chat_food_video.ui.presentation.blockedusers

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.layout.padding
import com.kapirti.social_chat_food_video.common.composable.BackAppBar
import com.kapirti.social_chat_food_video.common.composable.EmptyScreen

@Composable
fun BlockedUsersRoute(
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: BlockedUsersViewModel = hiltViewModel(),
) {
    val blockedUsers by viewModel.blockedUsers.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
     //   bottomBar = { AdsBannerToolbar(ADS_BLOCKED_USERS_BANNER_ID) },
        topBar = {
            BackAppBar(
                title = AppText.blocked_users_title,
                popUp = popUp,
            )
        }
    ) { innerPadding ->

        if (blockedUsers.isEmpty()) {
            EmptyScreen(contentPadding = innerPadding)
        } else {
            BlockedUsersScreen(
                blockedUsers = blockedUsers,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}
