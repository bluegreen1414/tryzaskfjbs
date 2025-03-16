package com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kapirti.social_chat_food_video.ui.Route

fun NavController.navigateToPhotoPicker(chatId: String, navOptions: NavOptions? = null) {
    this.navigate(Route.PhotoPicker(chatId), navOptions)
}

fun NavGraphBuilder.photoPickerScreen(
    onPhotoPicked: () -> Unit,
) {
    composable<Route.PhotoPicker> {
        PhotoPickerRoute(
            onPhotoPicked = onPhotoPicked,
        )
    }
}
