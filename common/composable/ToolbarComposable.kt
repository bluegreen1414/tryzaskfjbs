package com.kapirti.social_chat_food_video.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.R.drawable as AppIcon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color
/**


@Composable
fun PostTopBar(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSharePost: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)),
        modifier = modifier.padding(end = 16.dp)
    ) {
        Row(Modifier.padding(horizontal = 8.dp)) {
            FavoriteButton(onClick = { /* Functionality not available */ })
            BookmarkButton(isBookmarked = isFavorite, onClick = onToggleFavorite)
            ShareButton(onClick = onSharePost)
            TextSettingsButton(onClick = { /* Functionality not available */ })
        }
    }
}




 */



@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BackAppBar(
    @StringRes title: Int,
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    imageVector = KapirtiIcons.ArrowBack,
                    contentDescription = stringResource(id = AppText.cd_back),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatsAppBar(
    onCallsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(AppText.chats_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        actions = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onCallsClick
                ) {
                    Icon(
                        imageVector = KapirtiIcons.Phone,
                        contentDescription = stringResource(id = AppText.calls_title),
                    )
                }

                IconButton(
                    onClick = onProfileClick
                ) {
                    Icon(
                        imageVector = KapirtiIcons.Account,
                        contentDescription = stringResource(id = AppText.profile_title),
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShopToolbar(
    title: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        actions = {
            IconButton(onClick = action) {
                Icon(
                    imageVector = KapirtiIcons.Add,
                    contentDescription = stringResource(id = AppText.add_post_title)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileToolbar(
    title: String,
    popUp: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNicknameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, modifier = Modifier.clickable { onNicknameClick() })},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    imageVector = KapirtiIcons.ArrowBack,
                    contentDescription = stringResource(id = AppText.cd_back),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        actions = {
            Row {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = KapirtiIcons.Add,
                        contentDescription = stringResource(id = AppText.add_post_title)
                    )
                }
                Spacer(Modifier.width(5.dp))
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = KapirtiIcons.Settings,
                        contentDescription = stringResource(id = AppText.settings_title)
                    )
                }
            }
        },
    )
}


/**
 *


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
openDrawer: () -> Unit,
onSearchClick: () -> Unit,
// onFilterClick: () -> Unit,
modifier: Modifier = Modifier,
topAppBarState: TopAppBarState = rememberTopAppBarState(),
scrollBehavior: TopAppBarScrollBehavior? =
TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
CenterAlignedTopAppBar(
title = {
Image(
painter = painterResource(id = AppIcon.logo),
contentDescription = stringResource(AppText.app_name),
modifier = Modifier.size(40.dp)
)
},
navigationIcon = {
IconButton(onClick = openDrawer) {
Icon(
imageVector = Icons.Default.Menu,
contentDescription = stringResource(id = AppText.cd_menu),
tint = MaterialTheme.colorScheme.primary
)
}
},
actions = {
Row{
IconButton(onClick = onSearchClick ) {
Icon(
imageVector = Icons.Filled.Search,
contentDescription = stringResource(id = AppText.search)
)
}
Spacer(modifier = Modifier.width(3.dp))
/** IconButton(onClick = onFilterClick ) {
Icon(
imageVector = Icons.Filled.FilterList,
contentDescription = stringResource(id = AppText.filters_title)
)
}*/
}

},
scrollBehavior = scrollBehavior,
modifier = modifier
)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineToolbar(
isExpandedScreen: Boolean,
openDrawer: () -> Unit,
onActionsClick: () -> Unit,
) {
CenterAlignedTopAppBar(
title = {
Text(
text = stringResource(AppText.timeline_title),
style = MaterialTheme.typography.titleLarge
)
},
navigationIcon = {
if (!isExpandedScreen) {
IconButton(onClick = openDrawer) {
Icon(
imageVector = Icons.Default.Menu,
contentDescription = stringResource(id = AppText.cd_menu),
tint = MaterialTheme.colorScheme.primary
)
}
}
},
actions = {
IconButton(
onClick = { onActionsClick() }
) {
Icon(
imageVector = Icons.Default.Add,
contentDescription = stringResource(id = AppText.add_video)
)
}
}
)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolbar(
accountType: String,
isExpandedScreen: Boolean,
isAnonymousAccount: Boolean,
openDrawer: () -> Unit,
onSettingsClick: () -> Unit,
) {
CenterAlignedTopAppBar(
title = {
Text(
text = accountType,
style = MaterialTheme.typography.titleLarge
)
},
navigationIcon = {
if (!isExpandedScreen) {
IconButton(onClick = openDrawer) {
Icon(
imageVector = Icons.Default.Menu,
contentDescription = stringResource(id = AppText.cd_menu),
tint = MaterialTheme.colorScheme.primary
)
}
}
},
actions = {
if (!isAnonymousAccount) {
IconButton(
onClick = { onSettingsClick() }
) {
Icon(
imageVector = LuccaIcons.Settings,
contentDescription = stringResource(id = AppText.settings_title)
)
}
}
}
)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryToolbar(
//like: String,
isExpandedScreen: Boolean,
enabledLike: Boolean,
timelineVisible: Boolean = false,
popUp: () -> Unit,
onLikeClick: () -> Unit,
onActionClick: () -> Unit = {},
) {
CenterAlignedTopAppBar(
title = {
Text(
text = "Likes", //"$like Likes",
style = MaterialTheme.typography.titleLarge,
color = Color.Red
)
},
navigationIcon = {
if (!isExpandedScreen) {
IconButton(onClick = popUp) {
Icon(
imageVector = Icons.AutoMirrored.Filled.ArrowBack,
contentDescription = stringResource(id = AppText.back),
tint = MaterialTheme.colorScheme.primary,
)
}
}
},
actions = {
Row {
IconButton(
enabled = enabledLike,
onClick = onLikeClick
) {
Icon(
imageVector = Icons.Filled.Favorite,
contentDescription = stringResource(id = AppText.cd_icon),
tint = DarkOrange
)
}
if (timelineVisible) {
Spacer(Modifier.width(5.dp))
IconButton(
onClick = onActionClick
) {
Icon(
imageVector = Icons.Default.Timeline,
contentDescription = stringResource(id = AppText.cd_icon),
tint = DarkOrange
)
}
}
}
}
)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersToolbar(
@StringRes text: Int,
isExpandedScreen: Boolean,
openDrawer: () -> Unit,
) {
CenterAlignedTopAppBar(
title = {
Text(
text = stringResource(text),
style = MaterialTheme.typography.titleLarge
)
},
navigationIcon = {
if (!isExpandedScreen) {
IconButton(onClick = openDrawer) {
Icon(
imageVector = LuccaIcons.Chats,
contentDescription = stringResource(id = AppText.cd_menu),
tint = MaterialTheme.colorScheme.primary
)
}
}
},
)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuToolbarCountry(
@StringRes text: Int,
isExpandedScreen: Boolean,
openDrawer: () -> Unit,
onCountryClick: () -> Unit,
) {
CenterAlignedTopAppBar(
title = {
Text(
text = stringResource(text),
style = MaterialTheme.typography.titleLarge
)
},
navigationIcon = {
if (!isExpandedScreen) {
IconButton(onClick = openDrawer) {
Icon(
imageVector = Icons.Default.Menu,
contentDescription = stringResource(id = AppText.cd_menu),
tint = MaterialTheme.colorScheme.primary
)
}
}
},
actions = {
IconButton(onClick = { onCountryClick() }) {
Icon(
imageVector = LuccaIcons.Flag,
contentDescription = stringResource(id = AppText.cd_menu),
tint = DarkOrange
)
}
}
)
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuToolbarLP(
isExpandedScreen: Boolean,
openDrawer: () -> Unit,
onLangClick: () -> Unit,
) {
CenterAlignedTopAppBar(
title = {
Text(
text = stringResource(AppText.language_practice_title),
style = MaterialTheme.typography.titleLarge
)
},
navigationIcon = {
if (!isExpandedScreen) {
IconButton(onClick = openDrawer) {
Icon(
imageVector = Icons.Default.Menu,
contentDescription = stringResource(id = AppText.cd_menu),
tint = MaterialTheme.colorScheme.primary
)
}
}
},
actions = {
Row {
IconButton(onClick = onLangClick) {
Icon(
imageVector = LuccaIcons.Language,
contentDescription = stringResource(id = AppText.cd_icon),
tint = DarkOrange
)
}
/** if (timelineVisible) {
Spacer(Modifier.width(5.dp))
IconButton(
onClick = onActionClick
) {
Icon(
imageVector = Icons.Default.Timeline,
contentDescription = stringResource(id = AppText.cd_icon),
tint = DarkOrange
)
}
}*/
}
}
)
}









@Composable
fun PostTopBar(
isFavorite: Boolean,
onToggleFavorite: () -> Unit,
onSharePost: () -> Unit,
modifier: Modifier = Modifier
) {
Surface(
shape = RoundedCornerShape(8.dp),
border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)),
modifier = modifier.padding(end = 16.dp)
) {
Row(Modifier.padding(horizontal = 8.dp)) {
FavoriteButton(onClick = { /* Functionality not available */ })
BookmarkButton(isBookmarked = isFavorite, onClick = onToggleFavorite)
ShareButton(onClick = onSharePost)
TextSettingsButton(onClick = { /* Functionality not available */ })
}
}
}




 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackDoneToolbar(
    title: String,
    isDoneBtnWorking: Boolean,
    popUp: () -> Unit,
    onDoneClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {Text(title)},
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    imageVector = KapirtiIcons.ArrowBack,
                    contentDescription = stringResource(id = AppText.back),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        actions = {
            IconButton(onClick = onDoneClick) {
                if (isDoneBtnWorking) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = KapirtiIcons.Done,
                        contentDescription = stringResource(id = AppText.done),
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackReportToolbar(
    displayName: String,
    popUp: () -> Unit,
    onReportClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    imageVector = KapirtiIcons.ArrowBack,
                    contentDescription = stringResource(id = AppText.back),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        actions = {
            IconButton(onClick = onReportClick) {
                Icon(
                    imageVector = KapirtiIcons.MoreVert,
                    contentDescription = stringResource(id = AppText.cd_icon),
                )
            }
        }
    )
}
