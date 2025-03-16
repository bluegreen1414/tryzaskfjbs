package com.kapirti.social_chat_food_video.ui.presentation.story

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation


private val PURPLE = Color(0xC19D00FF)

@Composable
fun StoryRoute(
    myId: String,
    myPhoto: String,
    myName: String,
    myItem: Story?,
    stories: List<Story>,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 7.dp)
    ) {
        LazyRow {
            item { YourStory(myPhoto, myName, onClick = onCreateStoryClick) }
            if(myItem != null){
                item {
                    StoryItem(myItem, onClick = {onStoryWatchClick(myId)})
                }
            }
            if(stories.isNotEmpty()) {
                items(stories.size) { index ->
                    StoryItem(stories[index],
                        onClick = {onStoryWatchClick(stories[index].userId)}
                    )
                }
            }
        }
    }
}

@Composable
private fun StoryItem(stories: Story, onClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(end = 15.dp, top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    BorderStroke(2.dp, color = PURPLE),
                    CircleShape
                )
                .size(70.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .size(70.dp),
                painter = rememberImagePainter(
                    data = stories.photo,
                    builder = {
                        CircleCropTransformation()
                    }
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Content",
            )
        }
        Text(
            modifier = Modifier.width(70.dp),
            fontSize = 13.sp,
            text = stories.username,
            color = Color.Black,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W300,
            maxLines = 1
        )
    }
}

@Composable
private fun YourStory(myPhoto: String, myName: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(end = 15.dp, top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp) // Set the size of the box to 70dp
                .clickable(onClick = onClick) // Make the box clickable
        ) {
            // Load the story image asynchronously
            AsyncImage(
                model = myPhoto, // The URL of the image
                contentDescription = null, // No content description is provided
                modifier = Modifier.clip(CircleShape), // Clip the image to a circle shape
                contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
            )
            // Display the add icon
            Icon(
                Icons.Default.AddCircle, // The add icon
                contentDescription = null, // No content description is provided
                modifier = Modifier
                    .align(Alignment.BottomEnd), // Align the icon to the bottom end of the box
                tint = Color(0xFF2196F3) // Set the color of the icon to blue
            )
        }
        // Display the name of the story
        Text(
            text = myName, // The name of the story
            fontWeight = FontWeight.Normal, // The font weight is normal
            fontSize = 13.sp // The font size is 13sp
        )
    }
}
