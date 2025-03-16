package com.kapirti.social_chat_food_video.ui.presentation.settings.content.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.common.composable.BackDoneToolbar
import com.kapirti.social_chat_food_video.common.composable.DescriptionField
import com.kapirti.social_chat_food_video.common.ext.fieldModifier
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.ui.res.stringResource
import com.kapirti.social_chat_food_video.R

@Composable
fun FeedbackScreen (
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedbackViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            BackDoneToolbar(
                isDoneBtnWorking = uiState.isDoneBtnWorking,
                popUp = popUp,
                title = stringResource(R.string.feedback),
            ) { viewModel.saveFeedback(popUp) }
        }
    ) { innerPadding ->
        Column(modifier.padding(innerPadding)) {
            DescriptionField(
                AppText.feedback_description,
                uiState.text,
                viewModel::onTextChange,
                Modifier.fieldModifier()
            )
        }
    }
}
