package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun NicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: NicknameViewModel = hiltViewModel()
) {
    val nicknameState by viewModel.nicknameState.collectAsState()

    var nicknameInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nicknameInput,
            onValueChange = { nicknameInput = it },
            label = { Text("Enter Nickname") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.checkNicknameAvailability(nicknameInput) }) {
            Text("Check Availability")
        }

        when (nicknameState) {
            is NicknameState.Loading -> {
                CircularProgressIndicator()
            }
            is NicknameState.Available -> {
                Text("Nickname is available!", color = Color.Green)
                Button(onClick = { viewModel.saveNickname(nicknameInput, "user_uid") }) {
                    Text("Save Nickname")
                }
            }
            is NicknameState.Taken -> {
                Text("Nickname is already taken!", color = Color.Red)
                Text("Suggestions:")
                (nicknameState as NicknameState.Taken).suggestions.forEach { suggestion ->
                    Text(suggestion)
                }
            }
            is NicknameState.Saved -> {
                Text("Nickname saved successfully!", color = Color.Green)
            }
            is NicknameState.Error -> {
                Text("An error occurred!", color = Color.Red)
            }
            else -> {}
        }
    }
}
