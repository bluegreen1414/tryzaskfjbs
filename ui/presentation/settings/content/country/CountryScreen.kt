package com.kapirti.social_chat_food_video.ui.presentation.settings.content.country

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.ui.presentation.edit.CountryQuestion

@Composable
fun CountryScreen (
    popUp: () -> Unit,
    restartApp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = uiState.isDoneBtnWorking,
                popUp = popUp,
                title = stringResource(R.string.country)
            ) { restartApp() }
        }
    ) { innerPadding ->
        CountryQuestion(
            popUp = popUp,
            selectedAnswer = viewModel.country,
            onOptionSelected = viewModel::onCountryResponse,
            modifier = modifier.padding(innerPadding)
        )
    }
}
