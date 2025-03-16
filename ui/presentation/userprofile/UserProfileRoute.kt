package com.kapirti.social_chat_food_video.ui.presentation.userprofile

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.complex.ProfileContent
import com.kapirti.social_chat_food_video.common.composable.AdsBannerToolbar
import com.kapirti.social_chat_food_video.common.composable.BackReportToolbar
import com.kapirti.social_chat_food_video.common.composable.DialogCancelButton
import com.kapirti.social_chat_food_video.common.composable.DialogConfirmButton
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import com.kapirti.social_chat_food_video.core.viewmodel.IncludeAccountViewModel


@OptIn(UnstableApi::class)
@Composable
internal fun UserProfileRoute(
    userId: String,
    modifier: Modifier = Modifier,
    popUp: () -> Unit,
    onChatClicked: (String) -> Unit,
    onVideoCallClicked: () -> Unit,
    navigateUserExplore: (String) -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by viewModel.uiState
    val media = viewModel.media

    LaunchedEffect(userId) {
        viewModel.getUser(userId)
    }

    LaunchedEffect(uiState.navigateRoomId) {
        uiState.navigateRoomId?.let {
            onChatClicked(it)
            viewModel.clearRoomId()
        }
    }


    val accountInfo by viewModel.accountInfo
    val context = LocalContext.current
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            BackReportToolbar(
                displayName = accountInfo.username,
                popUp = popUp,
            ) { viewModel.onReportClick() }
        },
        bottomBar = { AdsBannerToolbar()}
    ) { innerPadding ->
        HomeBackground(Modifier.fillMaxSize())
        ProfileContent(
            online = accountInfo.online,
            username = accountInfo.username,
            photo = accountInfo.photo,
            bio = accountInfo.bio,
            age = accountInfo.age,
            gender = accountInfo.gender,
            aim = accountInfo.aim,
            onChatClick = { viewModel.generatedRoomId()},
            onSoundCallClick = { Toast.makeText(context, "This function is not working now", Toast.LENGTH_SHORT).show()},
            onVideoCallClick = { Toast.makeText(context, "This function is not working now", Toast.LENGTH_SHORT).show()},
          //  onVideoCallClick = onVideoCallClicked,
           // onSoundCallClick = { viewModel.onSoundClick(snackbarHostState) },
            postsList = media,
            modifier = modifier.padding(innerPadding),
            navigateUserExplore = { navigateUserExplore(userId) }
        )
    }
    if (viewModel.showReportDialog.value) {
        AlertDialog(
            title = { Text(stringResource(R.string.report)) },
            text = { Text(stringResource(R.string.report_user)) },
            dismissButton = {
                DialogCancelButton(R.string.cancel) {
                    viewModel.showReportDialog.value = false
                }
            },
            confirmButton = { DialogConfirmButton(R.string.report) { viewModel.onReportButtonClick() } },
            onDismissRequest = { viewModel.showReportDialog.value = false },
        )
    }
    if (viewModel.showReportDone.value) {
        AlertDialog(
            title = { Text(stringResource(R.string.thank_you)) },
            text = { Text(stringResource(R.string.report_done_text)) },
            confirmButton = { DialogConfirmButton(R.string.ok) { viewModel.onReportDoneDismiss(popUp) } },
            onDismissRequest = { viewModel.onReportDoneDismiss(popUp) },
        )
    }
}

    /**
        Column {
            uiState.user?.let { usr ->
                Text("user id : $userId")
                Text("name : ${usr.name}")
//                Text("surname : ${usr.surname}")
//                Text("Last seen : ${DateUtils.getRelativeTimeSpanString(usr.lastSeen!!.seconds * 1000) }")
                Button(
                    onClick = {
                        viewModel.generatedRoomId()
                        //viewModel.generatedRoomId()?.let { onChatClicked(it) }
                    }
                ) {
                    Text("chat")
                }
                Button(onClick = onVideoCallClicked) { Text("video call") }
                Button(onClick = {}) { Text("voice call") }
            }

        }
    }
}*/
