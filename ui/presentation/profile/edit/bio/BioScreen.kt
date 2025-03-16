package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.bio

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.R
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.common.composable.DescriptionField
import com.kapirti.social_chat_food_video.common.ext.fieldModifier

@Composable
fun BioScreen(
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = uiState.isDoneBtnWorking,
                popUp = popUp,
                title = stringResource(R.string.description)
            ) { viewModel.saveBio(popUp) }
        }
    ) { innerPadding ->
        BioContent(
            text = R.string.description,
            value = uiState.text,
            onValueChange = viewModel::onTextChange,
            modifier = modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun BioContent(
    @StringRes text: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val fieldModifier = Modifier.fieldModifier()
        DescriptionField(text, value, onValueChange, fieldModifier)
    }
}
