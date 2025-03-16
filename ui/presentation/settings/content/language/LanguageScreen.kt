package com.kapirti.social_chat_food_video.ui.presentation.settings.content.language

import com.kapirti.social_chat_food_video.ui.presentation.edit.LearnLanguageQuestion
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar

@Composable
fun LanguageScreen (
    popUp: () -> Unit,
    restartApp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = uiState.isDoneBtnWorking,
                popUp = popUp,
                title = stringResource(R.string.language),
            ) { restartApp() }
        }
    ) { innerPadding ->
        LearnLanguageQuestion(
            popUp = popUp,
            selectedAnswer = viewModel.language,
            onOptionSelected = viewModel::onCountryResponse,
            modifier = modifier.padding(innerPadding)
        )
    }
}
