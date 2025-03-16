package com.kapirti.social_chat_food_video.ui.presentation.settings.content.deleteaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.common.composable.DescriptionField
import com.kapirti.social_chat_food_video.common.composable.PasswordField
import com.kapirti.social_chat_food_video.common.ext.fieldModifier
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.R

@Composable
fun DeleteAccountScreen (
    popUp: () -> Unit,
    restartApp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeleteAccountViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = uiState.isDoneBtnWorking,
                popUp = popUp,
                title = stringResource(R.string.delete_account_title)
            ) {
                viewModel.onDeleteMyAccountClick(
                    restartApp = restartApp,
                )
            }
        }
    ) { innerPadding ->
        Column(modifier.padding(innerPadding)) {
            PasswordField(
                value = uiState.password,
                onNewValue = viewModel::onPasswordChange,
                modifier = Modifier.fieldModifier(),
                isError = uiState.isErrorPassword
            )
            Text(stringResource(AppText.delete_account_title))
            Text(stringResource(AppText.delete_account_description))
            DescriptionField(
                AppText.delete_account_title,
                uiState.text,
                viewModel::onTextChange,
                Modifier.fieldModifier()
            )
        }
    }
}
