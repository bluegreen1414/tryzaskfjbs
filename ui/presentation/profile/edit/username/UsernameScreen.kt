package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.username

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.common.composable.BasicField
import com.kapirti.social_chat_food_video.common.ext.fieldModifier

@Composable
fun UsernameScreen(
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UsernameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = uiState.isDoneBtnWorking,
                popUp = popUp,
                title = stringResource(R.string.username)
            ) { viewModel.saveUsername(popUp) }
        }
    ) { innerPadding ->
        UsernameContent(
            text = R.string.name_and_surname,
            value = uiState.text,
            onValueChange = viewModel::onTextChange,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun UsernameContent(
    @StringRes text: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val fieldModifier = Modifier.fieldModifier()
        BasicField(text, value, onValueChange, fieldModifier)
    }
}
