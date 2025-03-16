package com.kapirti.social_chat_food_video.common.composable

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shop
import androidx.compose.material.icons.outlined.VideoLibrary

@Composable
private fun arrowIcon(ltrIcon: ImageVector, rtlIcon: ImageVector): ImageVector =
    if (LocalLayoutDirection.current == LayoutDirection.Ltr) ltrIcon else rtlIcon

@Composable
fun arrowBackIcon() = arrowIcon(ltrIcon = Icons.AutoMirrored.Outlined.ArrowBack,
    rtlIcon = Icons.AutoMirrored.Outlined.ArrowForward)

object KapirtiIcons {
    val Home = Icons.Outlined.Home
    val Timeline = Icons.Outlined.VideoLibrary
    val Shop = Icons.Outlined.Shop
    val Chats = Icons.Outlined.ChatBubbleOutline
    val Settings = Icons.Outlined.Settings

    val Add = Icons.Outlined.Add
    val Done = Icons.Outlined.Done

    val StarRate = Icons.Default.StarRate
    val Share = Icons.Default.Share
    val Feedback = Icons.Default.Feedback
    val Login =  Icons.AutoMirrored.Default.Login
    val PersonAdd = Icons.Default.PersonAdd
    val Logout = Icons.AutoMirrored.Default.Logout
    val Delete =  Icons.Default.Delete

    val ArrowBack = Icons.AutoMirrored.Rounded.ArrowBack
    val Camera = Icons.Default.CameraAlt
    //    val gg = Icons.Default.Came
    val Search = Icons.Default.Search
    var MoreVert = Icons.Default.MoreVert

    val Message = Icons.AutoMirrored.Filled.Message
    var Call = Icons.Filled.Call
    var VideoCall = Icons.Filled.VideoCall
    var Mic = Icons.Filled.Mic
    val VideoLibrary = Icons.Default.VideoLibrary
    val Phone = Icons.Default.Phone
    val Close = Icons.Default.Close
    val Favorite = Icons.Default.Favorite
    val Fire = Icons.Filled.LocalFireDepartment
    val Language = Icons.Default.Language
    val Hotel = Icons.Default.Hotel
    val Restaurant = Icons.Default.Restaurant
    val Cafe = Icons.Default.LocalCafe
    val Account = Icons.Default.AccountCircle
    val Flag = Icons.Default.Flag
    val Bookmarks = Icons.Default.Bookmarks
    // val follow = Icons.AutoMirrored.Filled.FollowTheSigns
    val Report = Icons.Default.Report
    val Email = Icons.Default.Email
    val Visibility = Icons.Default.Visibility
    val VisibilityOff = Icons.Default.VisibilityOff
    val Lock = Icons.Default.Lock
    val Menu = Icons.Default.Menu
    val Tetris = Icons.Default.Games
    val RacingCar = Icons.Default.CarRental
    val Explore = Icons.Default.Explore
    val Filter = Icons.Default.FilterAlt


    val Block = Icons.Default.Block
    val VideoCamOn = Icons.Default.Videocam
    val VideoCamOff = Icons.Default.VideocamOff
    val MicOn = Icons.Default.Mic
    val MicOff = Icons.Default.MicOff
    val CameraFlip = Icons.Default.Cameraswitch
    val CallEnd = Icons.Default.CallEnd
    val PhotoPicker = Icons.Default.PhotoLibrary
    val Flirt = Icons.Default.SwitchAccount
    val Marriage = Icons.Default.SupervisorAccount
    val Female = Icons.Default.Female
    val Male = Icons.Default.Male
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
