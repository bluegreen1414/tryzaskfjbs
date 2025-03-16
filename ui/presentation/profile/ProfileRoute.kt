package com.kapirti.social_chat_food_video.ui.presentation.profile

import androidx.annotation.OptIn
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.kapirti.social_chat_food_video.common.composable.ProfileToolbar
import androidx.compose.runtime.remember
import androidx.media3.common.util.UnstableApi
import com.kapirti.social_chat_food_video.common.complex.ProfileContent
import com.kapirti.social_chat_food_video.common.composable.HomeBackground

@OptIn(UnstableApi::class)
@Composable
internal fun ProfileRoute(
    popUp: () -> Unit,
    navigateSettings: () -> Unit,
    navigateNickname: () -> Unit,
    navigateProfilePhoto: () -> Unit,
    navigateUsername: () -> Unit,
    navigateBio: () -> Unit,
    navigateUserExplore: (String) -> Unit,
    onAddClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val media = viewModel.media
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ProfileToolbar(
                title = viewModel.displayName ?: "displayName",
                onNicknameClick = navigateNickname,
                onSettingsClick = navigateSettings,
                onAddClick = { viewModel.onAddClick(onAddClick) },
                popUp = popUp,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        HomeBackground(Modifier.fillMaxSize())
        ProfileContent(
            username = viewModel.username ?: "",
            photo = viewModel.photo ?: "",
            bio = viewModel.bio ?: "",
            age = viewModel.birthday ?: "",
            gender = viewModel.gender ?: "",
            aim = viewModel.accountType ?: "",
            postsList = media,
            profile = true,
            navigateProfilePhoto = navigateProfilePhoto,
            navigateUsername = navigateUsername,
            navigateBio = navigateBio,
            modifier = modifier.padding(innerPadding),
            navigateUserExplore = { navigateUserExplore(viewModel.myId) }
        )
    }
}


/**
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.common.composable.FullScreenPhoto
import com.kapirti.social_chat_food_video.common.composable.UserImage
import com.kapirti.social_chat_food_video.common.ext.user.Header
import com.kapirti.social_chat_food_video.common.ext.user.ProfileBodyCafe
import com.kapirti.social_chat_food_video.common.ext.user.ProfileBodyHotel
import com.kapirti.social_chat_food_video.common.ext.user.ProfileBodyRestaurant
import com.kapirti.social_chat_food_video.common.ext.user.ProfileBodyUserFlirt
import com.kapirti.social_chat_food_video.common.ext.user.ProfileBodyUserLanguagePractice
import com.kapirti.social_chat_food_video.common.ext.user.ProfileBodyUserMarriage
import com.kapirti.social_chat_food_video.common.ext.user.Title
import com.kapirti.social_chat_food_video.core.constants.ConsAds
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.Hotel
import com.kapirti.social_chat_food_video.model.Restaurant
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.model.UserLP
import com.kapirti.social_chat_food_video.model.UserMarriage

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileUserFlirtBody(
userFlirt: UserFlirt,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
ProfileBodyUserFlirt(
adsCode = ConsAds.ADS_PROFILE_BANNER_ID,
gender = userFlirt.gender,
birthday = userFlirt.birthday,
description = userFlirt.description,
dateOfCreation = userFlirt.dateOfCreation ?: Timestamp.now(),
scroll = scroll,
navigateGallery = navigateGallery,
hobby = userFlirt.hobby,
photos = emptyList()
)
Title(
textFirst = userFlirt.displayName,
textSecond = "${userFlirt.name} ${userFlirt.surname}",
textThird = userFlirt.country,
) { scroll.value }
UserImage(
imageUrl = userFlirt.photo,
onImageClicked = { activeId = userFlirt.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = userFlirt.photo,
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileUserMarriageBody(
userMarriage: UserMarriage,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
ProfileBodyUserMarriage(
adsCode = ConsAds.ADS_PROFILE_BANNER_ID,
gender = userMarriage.gender,
birthday = userMarriage.birthday,
description = userMarriage.description,
dateOfCreation = userMarriage.dateOfCreation ?: Timestamp.now(),
scroll = scroll,
navigateGallery = navigateGallery,
hobby = userMarriage.hobby,
photos = emptyList()
)
Title(
textFirst = userMarriage.displayName,
textSecond = "${userMarriage.name} ${userMarriage.surname}",
textThird = userMarriage.country,
) { scroll.value }
UserImage(
imageUrl = userMarriage.photo,
onImageClicked = { activeId = userMarriage.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = userMarriage.photo, //photos.first { it.id == activeId },
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileUserLanguagePracticeBody(
userLP: UserLP,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
ProfileBodyUserLanguagePractice(
adsCode = ConsAds.ADS_PROFILE_BANNER_ID,
gender = userLP.gender,
birthday = userLP.birthday,
description = userLP.description,
motherTongue = userLP.motherTongue,
learnLanguage = userLP.learnLanguage,
dateOfCreation = userLP.dateOfCreation ?: Timestamp.now(),
scroll = scroll,
navigateGallery = navigateGallery,
hobby = userLP.hobby,
photos = emptyList()
)
Title(
textFirst = userLP.displayName,
textSecond = "${userLP.name} ${userLP.surname}",
textThird = userLP.country,
) { scroll.value }
UserImage(
imageUrl = userLP.photo,
onImageClicked = { activeId = userLP.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = userLP.photo, //photos.first { it.id == activeId },
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileHotelBody(
hotel: Hotel,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
ProfileBodyHotel(
adsCode = ConsAds.ADS_PROFILE_BANNER_ID,
description = hotel.description,
address = hotel.address,
dateOfCreation = hotel.dateOfCreation ?: Timestamp.now(),
scroll = scroll,
photos = hotel.photos,
navigateGallery = navigateGallery,
)
Title(
textFirst = hotel.name,
textSecond = hotel.city,
textThird = hotel.country,
) { scroll.value }
UserImage(
imageUrl = hotel.photo,
onImageClicked = { activeId = hotel.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = hotel.photo, //photos.first { it.id == activeId },
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileRestaurantBody(
restaurant: Restaurant,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
ProfileBodyRestaurant(
adsCode = ConsAds.ADS_PROFILE_BANNER_ID,
description = restaurant.description,
address = restaurant.address,
dateOfCreation = restaurant.dateOfCreation ?: Timestamp.now(),
scroll = scroll,
photos = restaurant.photos,
navigateGallery = navigateGallery,
)
Title(
textFirst = restaurant.name,
textSecond = restaurant.city,
textThird = restaurant.country,
) { scroll.value }
UserImage(
imageUrl = restaurant.photo,
onImageClicked = { activeId = restaurant.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = restaurant.photo,
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileCafeBody(
cafe: Cafe,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
ProfileBodyCafe(
adsCode = ConsAds.ADS_PROFILE_BANNER_ID,
description = cafe.description,
address = cafe.address,
dateOfCreation = cafe.dateOfCreation ?: Timestamp.now(),
scroll = scroll,
photos = cafe.photos,
navigateGallery = navigateGallery,
)
Title(
textFirst = cafe.name,
textSecond = cafe.city,
textThird = cafe.country,
) { scroll.value }
UserImage(
imageUrl = cafe.photo,
onImageClicked = { activeId = cafe.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = cafe.photo,
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}


/**
@Composable
fun ProfileScreen(
user: User,
navigateGallery: () -> Unit,
modifier: Modifier = Modifier,
nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()
){
var activeId by rememberSaveable { mutableStateOf<String?>(null) }
val scrim = remember(activeId) { FocusRequester() }

BoxWithConstraints(
modifier = modifier
.fillMaxSize()
.nestedScroll(nestedScrollInteropConnection)
// .systemBarsPadding()
) {
Box(Modifier.fillMaxSize()) {
val scroll = rememberScrollState(0)
Header()
Body(
user = user,
scroll = scroll,
navigateGallery = navigateGallery,
isProfile = true,
)
Title(user) { scroll.value }
UserImage(
imageUrl = user.photo,
onImageClicked = { activeId = user.photo }
) { scroll.value }
}
}

if (activeId != null) {
FullScreenPhoto(
photo = user.photo, //photos.first { it.id == activeId },
onDismiss = { activeId = null },
modifier = Modifier.focusRequester(scrim)
)

LaunchedEffect(activeId) {
scrim.requestFocus()
}
}
}
*/

/**


@Composable
fun Body(
user: User?,
scroll: ScrollState,
navigateGallery: () -> Unit,
isProfile: Boolean = false,
) {
Column {
Spacer(
modifier = Modifier
.fillMaxWidth()
.statusBarsPadding()
.height(MinTitleOffset)
)
Column(
modifier = Modifier.verticalScroll(scroll)
) {
Spacer(Modifier.height(GradientScroll))
LuccaSurface(Modifier.fillMaxWidth()) {
Column {
Spacer(Modifier.height(ImageOverlap))
Spacer(Modifier.height(TitleHeight))

Spacer(Modifier.height(16.dp))

user?.let {
key(it.photos) {
PhotosCollection(
photos = it.photos,
navigateGallery = navigateGallery
)
}
}
DividerSpacer()

Text(
text = stringResource(R.string.description),
style = MaterialTheme.typography.bodySmall,
color = Color(0x99000000),
modifier = HzPadding
)
Spacer(Modifier.height(16.dp))
Text(
text = user?.let{ it.description } ?: "",
style = MaterialTheme.typography.bodyLarge,
color = Color(0x99000000),
overflow = TextOverflow.Ellipsis,
modifier = HzPadding
)
DividerSpacer()


if(!isProfile) {
Text(
text = stringResource(R.string.online),
style = MaterialTheme.typography.bodySmall,
color = Color(0x99000000),
modifier = HzPadding
)
Spacer(Modifier.height(16.dp))
Text(
text = user?.let {
if (it.online) {
stringResource(id = R.string.online)
} else {
it.lastSeen?.let { itTime ->
timeCustomFormat(itTime.seconds)
}
}
} ?: "",
style = MaterialTheme.typography.bodyLarge,
color = Color(0x99000000),
overflow = TextOverflow.Ellipsis,
modifier = HzPadding
)
DividerSpacer()
}

Text(
text = stringResource(R.string.gender),
style = MaterialTheme.typography.bodySmall,
color = Color(0x99000000),
modifier = HzPadding
)
Spacer(Modifier.height(4.dp))
Text(
text = user?.let{ it.gender } ?: "",
style = MaterialTheme.typography.bodyLarge,
color = Color(0x99000000),
modifier = HzPadding
)
DividerSpacer()

Text(
text = stringResource(R.string.age),
style = MaterialTheme.typography.bodySmall,
color = Color(0x99000000),
modifier = HzPadding
)
Spacer(Modifier.height(4.dp))
Text(
text = user?.let{ it.birthday } ?: "",
style = MaterialTheme.typography.bodyLarge,
color = Color(0x99000000),
modifier = HzPadding
)
DividerSpacer()

Text(
text = stringResource(R.string.join),
style = MaterialTheme.typography.bodySmall,
color = Color(0x99000000),
modifier = HzPadding
)
Spacer(Modifier.height(4.dp))
Text(
text = user?.let { itUser -> itUser.date?.let{ itLast -> timeCustomFormat(itLast.seconds) } } ?: "",
style = MaterialTheme.typography.bodyLarge,
color = Color(0x99000000),
modifier = HzPadding
)

user?.let {
key(it.hobby) {
HobbyCollection(hobbys = it.hobby)
}
}

Spacer(
modifier = Modifier
.padding(bottom = BottomBarHeight)
.navigationBarsPadding()
.height(8.dp)
)
}
}
}
}
}

@Composable
fun Title(user: User?, scrollProvider: () -> Int) {
val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

val timestamp = Timestamp.now()
val age = calculateBirthday(birthday =user?.let { it.birthday } ?: "", timestamp = timestamp)

Column(
verticalArrangement = Arrangement.Bottom,
modifier = Modifier
.heightIn(min = TitleHeight)
.statusBarsPadding()
.offset {
val scroll = scrollProvider()
val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
IntOffset(x = 0, y = offset.toInt())
}
.background(color = Color(0xffffffff))
) {
Spacer(Modifier.height(16.dp))
Text(
text =   age,
style = MaterialTheme.typography.headlineSmall,
color = Color(0xde000000),
modifier = HzPadding
)
Text(
text = user?.let { it.displayName } ?: "displayName",
style = MaterialTheme.typography.titleSmall,
fontSize = 20.sp,
color = Color.Black,//Color(0xbdffffff),
modifier = HzPadding
)
Spacer(Modifier.height(4.dp))
Text(
text = "${user?.let{ it.name }} ${user?.let { it.surname }}",
style = MaterialTheme.typography.headlineSmall,
color = Color(0xffded6fe),
modifier = HzPadding
)

Spacer(Modifier.height(8.dp))
BasicDivider()
}
}


*/
 */
