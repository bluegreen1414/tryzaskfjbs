package com.kapirti.social_chat_food_video.common.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.ui.presentation.home.users.CraneScreen

@Composable
fun CraneTabBar(
    modifier: Modifier = Modifier,
    children: @Composable (Modifier) -> Unit
) {
    Row(modifier) {
        children(Modifier.weight(1f).align(Alignment.CenterVertically))
    }
}

@Composable
fun CraneTabsUsers(
    modifier: Modifier = Modifier,
    titles: List<String>,
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = tabSelected.ordinal,
        modifier = modifier,
        edgePadding = 16.dp,
        contentColor = MaterialTheme.colorScheme.outline,
        indicator = { },
        divider = { }
    ) {
        titles.forEachIndexed { index, title ->
            val selected = index == tabSelected.ordinal

            var textModifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            if (selected) {
                textModifier =
                    Modifier.border(
                        BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.secondaryContainer
                        ),
                        RoundedCornerShape(16.dp)
                    )
                        .then(textModifier)
            }

            Tab(
                selected = selected,
                onClick = { onTabSelected(CraneScreen.values()[index]) }
            ) {
                Text(
                    modifier = textModifier,
                    fontStyle = FontStyle.Italic,
                    text = title.toUpperCase(
                        //ConfigurationCompat.getLocales(AmbientConfiguration.current)[0]
                    )
                )

            }
        }
    }
}

/**
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = Color.Black,
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .fillMaxWidth()
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(8.dp),
                    content = {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = tab.title,
                                modifier = Modifier
                                    .padding(8.dp),
                                color = if (selectedTabIndex == index) Color.Black else Color.Gray
                            )
                        }
                    }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> {
                //Composible for tab1
            }
            1 -> {
                //Composible for tab2
            }
            2 -> {
                //Composible for tab3
            }
            3 -> {
                //Composible for tab4
            }
        }
    }
}

data class TabItem(val title:String, val icon: ImageVector)*/
