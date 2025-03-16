package com.kapirti.social_chat_food_video.common.complex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import com.kapirti.social_chat_food_video.common.composable.PostGrid
import com.kapirti.social_chat_food_video.common.composable.ProfileImage
import com.kapirti.social_chat_food_video.core.constants.ConsGender.FEMALE
import com.kapirti.social_chat_food_video.core.constants.ConsGender.MALE
import com.kapirti.social_chat_food_video.core.constants.SelectType.FLIRT
import com.kapirti.social_chat_food_video.core.constants.SelectType.HOTEL
import com.kapirti.social_chat_food_video.core.constants.SelectType.LANGUAGE_PRACTICE
import com.kapirti.social_chat_food_video.core.constants.SelectType.MARRIAGE
import com.kapirti.social_chat_food_video.core.constants.SelectType.RESTAURANT
import com.kapirti.social_chat_food_video.core.constants.SelectType.CAFE
import com.kapirti.social_chat_food_video.model.ExploreUserItem


@Composable
fun ProfileContent(
    username: String,
    photo: String,
    bio: String,
    age: String,
    aim: String,
    gender: String,
    postsList: List<ExploreUserItem>,
    online: Boolean = false,
    profile: Boolean = false,
    navigateProfilePhoto: () -> Unit = {},
    navigateUsername: () -> Unit = {},
    navigateBio: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onVideoCallClick: () -> Unit = {},
    onSoundCallClick: () -> Unit = {},
    navigateUserExplore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileHeader(
            username = username,
            profileImageUrl = photo,
            bio = bio,
            posts = postsList.size,
            age = age,
            aim = aim,
            gender = gender,
            profile = profile,
            online = online,
            navigateProfilePhoto = navigateProfilePhoto,
            navigateBio = navigateBio,
            navigateUsername = navigateUsername,
            onChatClick = onChatClick,
            onVideoCallClick = onVideoCallClick,
            onSoundCallClick = onSoundCallClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        PostGrid(
            postsList = postsList,
            navigateUserExplore
        )
    }
}


@Composable
private fun ProfileHeader(
    username: String,
    profileImageUrl: String,
    posts: Int,
    bio: String,
    age: String,
    aim: String,
    gender: String,
    profile: Boolean = false,
    online: Boolean = false,
    onChatClick: () -> Unit = {},
    onSoundCallClick: () -> Unit = {},
    onVideoCallClick: () -> Unit = {},
    navigateProfilePhoto: () -> Unit = {},
    navigateUsername: () -> Unit = {},
    navigateBio: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileImage(profileImageUrl, online, navigateProfilePhoto)
            Spacer(modifier = Modifier.width(16.dp))
            ProfileStats(posts = posts, age = age, aim = aim, gender = gender)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = username, style = MaterialTheme.typography.titleLarge, modifier = Modifier.clickable { navigateUsername() })
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = bio, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.clickable { navigateBio() })
        if (!profile) {
            Spacer(Modifier.height(8.dp))
            ContactPart(
                onChatClick = onChatClick,
                onVideoCallClick = onVideoCallClick,
                onSoundCallClick
            )
        }
    }
}


@Composable
private fun ProfileStats(posts: Int, age: String, aim: String, gender: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(value = posts.toString(), label = "Posts")
        Spacer(modifier = Modifier.width(16.dp))
        StatItem(value = age, label = "Age")
        Spacer(modifier = Modifier.width(16.dp))
        StatIcon(icon = getGenderIcon(gender), label = "Aim")
        Spacer(modifier = Modifier.width(16.dp))
        StatIcon(icon = getAimIcon(aim), label = "Aim")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun StatIcon(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, Modifier.size(24.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ContactPart(
    onChatClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onSoundCallClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = onChatClick ) { Text("chat") }
        Button(onClick = onVideoCallClick) { Text("video call") }
        Button(onClick = onSoundCallClick) { Text("voice call") }
    }
}


private fun getAimIcon(aim: String): ImageVector {
    return when(aim){
        HOTEL -> KapirtiIcons.Hotel
        RESTAURANT -> KapirtiIcons.Restaurant
        CAFE -> KapirtiIcons.Cafe
        FLIRT -> KapirtiIcons.Flirt
        MARRIAGE -> KapirtiIcons.Marriage
        LANGUAGE_PRACTICE -> KapirtiIcons.Language
        else -> KapirtiIcons.Flirt
    }
}
private fun getGenderIcon(gender: String): ImageVector {
    return when(gender){
        MALE -> KapirtiIcons.Male
        FEMALE -> KapirtiIcons.Female
        else -> KapirtiIcons.Female
    }
}
