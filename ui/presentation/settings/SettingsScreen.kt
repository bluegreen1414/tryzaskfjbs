package com.kapirti.social_chat_food_video.ui.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.kapirti.social_chat_food_video.common.composable.DangerousCardEditor
import com.kapirti.social_chat_food_video.common.composable.DialogCancelButton
import com.kapirti.social_chat_food_video.common.composable.DialogConfirmButton
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import com.kapirti.social_chat_food_video.common.composable.RegularCardEditor
import com.kapirti.social_chat_food_video.common.composable.ThemeCardEditor
import com.kapirti.social_chat_food_video.common.ext.card
import com.kapirti.social_chat_food_video.common.ext.spacer
import com.kapirti.social_chat_food_video.model.Theme

@Composable
fun SettingsScreen(
    navigateLogin: () -> Unit,
    navigateRegister: () -> Unit,
    navigateCountry: () -> Unit,
    navigateDeleteAccount: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateTetris: () -> Unit,
    navigateRacingCar: () -> Unit,
    navigateBlockedUser: () -> Unit,
    restartApp: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val theme by viewModel.theme
    var showSignOutDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState(false))
    val country by viewModel.country.collectAsState()
    val language by viewModel.language.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegularCardEditor(
            stringResource(AppText.rate),
            KapirtiIcons.StarRate,
            "",
            Modifier.card()
        ) { viewModel.rate() }
        RegularCardEditor(
            stringResource(AppText.share),
            KapirtiIcons.Share,
            "",
            Modifier.card()
        ) { viewModel.share() }
        RegularCardEditor(
            stringResource(AppText.feedback),
            KapirtiIcons.Feedback,
            "",
            Modifier.card(),
        ) { navigateFeedback()}
        RegularCardEditor(country,
            KapirtiIcons.Flag,
            "",
            Modifier.card()
        ) { navigateCountry()}
      /**  RegularCardEditor(language,
            Icons.Default.Language,
            "",
            Modifier.card()
        ) { viewModel.onLanguageClick(navigateEdit)}*/

        RegularCardEditor(
            stringResource(AppText.tetris_title),
            KapirtiIcons.Tetris,
            "",
            Modifier.card(),
        ) { navigateTetris()}
        RegularCardEditor(
            stringResource(AppText.racing_car_title),
            KapirtiIcons.RacingCar,
            "",
            Modifier.card(),
        ) { navigateRacingCar()}


        ThemeCardEditor(
            title = AppText.dark_mode,
            content = "",
            checked = theme == Theme.Dark,
            onCheckedChange = {
                viewModel.onThemeChanged(if (it) Theme.Dark else Theme.Light)
            },
            modifier = Modifier.card()
        )
        Spacer(modifier = Modifier.spacer())

        if (uiState.isAnonymousAccount) {
            RegularCardEditor(stringResource(AppText.log_in),
                    KapirtiIcons.Login, "", Modifier.card()) { navigateLogin() }
            RegularCardEditor(stringResource(AppText.register),
                KapirtiIcons.PersonAdd, "", Modifier.card()) { navigateRegister() }
        } else {
            RegularCardEditor(stringResource(id = AppText.blocked_users_title),
                KapirtiIcons.Block, "", Modifier.card()) { navigateBlockedUser() }
            RegularCardEditor(stringResource(AppText.sign_out),
                KapirtiIcons.Logout, "", Modifier.card()) { showSignOutDialog = true }
            DangerousCardEditor(stringResource(AppText.delete_my_account),
                KapirtiIcons.Delete, "", Modifier.card()) { navigateDeleteAccount()}
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showSignOutDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.sign_out) {
                    viewModel.onSignOutClick(restartApp = restartApp)
                    showSignOutDialog = false
                }
            },
            onDismissRequest = { showSignOutDialog = false }
        )
    }
}
