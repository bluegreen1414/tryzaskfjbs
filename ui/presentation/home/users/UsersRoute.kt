package com.kapirti.social_chat_food_video.ui.presentation.home.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kapirti.social_chat_food_video.common.composable.CraneTabBar
import com.kapirti.social_chat_food_video.common.composable.CraneTabsUsers
import com.kapirti.social_chat_food_video.common.composable.HomeBackground
import com.kapirti.social_chat_food_video.common.composable.KapirtiIcons
import com.kapirti.social_chat_food_video.core.viewmodel.IncludeAccountViewModel
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.Hotel
import com.kapirti.social_chat_food_video.model.Restaurant
import com.kapirti.social_chat_food_video.model.UserFlirt
import com.kapirti.social_chat_food_video.model.UserLP
import com.kapirti.social_chat_food_video.model.UserMarriage
import com.kapirti.social_chat_food_video.ui.presentation.filter.FilterScreen
import com.kapirti.social_chat_food_video.ui.presentation.story.Story

enum class CraneScreen {
    Flirt, Marriage, Language, Cafes, Restaurants, Hotels
}

@Composable
internal fun HomeRoute(
    includeAccountViewModel: IncludeAccountViewModel,
    onCreateStoryClick: () -> Unit,
    onItemClicked: (String, String) -> Unit,
    navigateStory: (String) -> Unit,
    navigateCountry: () -> Unit,
    navigateLanguage: () -> Unit,
    navigateSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CraneHomeContent(
        modifier = modifier,
        navigateUser = onItemClicked,
        includeAccountViewModel = includeAccountViewModel,
        onCreateStoryClick = onCreateStoryClick,
        navigateStory = navigateStory,
        navigateCountry = navigateCountry,
        navigateLanguage = navigateLanguage,
        navigateSearch = navigateSearch
    )
}

//    val myId = viewModel.currentUserId
/**
    HomeScreen(users.filterNot { it.id == myId }, onItemClicked = {
        onItemClicked.invoke(it, myId!!)
    })*/




@Composable
private fun CraneHomeContent(
    includeAccountViewModel: IncludeAccountViewModel,
    navigateUser: (String, String) -> Unit,
    onCreateStoryClick: () -> Unit,
    navigateStory: (String) -> Unit,
    navigateCountry: () -> Unit,
    navigateLanguage: () -> Unit,
    navigateSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val usersFlirt by viewModel.usersFlirt.collectAsStateWithLifecycle()
    val usersMarriage by viewModel.usersMarriage.collectAsStateWithLifecycle()
    val languageUsers by viewModel.usersLP.collectAsStateWithLifecycle()
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()
    val cafes by viewModel.cafes.collectAsStateWithLifecycle()
    val hotels by viewModel.hotels.collectAsStateWithLifecycle()
    var tabSelected by remember { mutableStateOf(CraneScreen.Flirt) }
    val myId = viewModel.currentUserId
    val myName = viewModel.username
    val myPhoto = viewModel.photo
    val stories = viewModel.allStories
    val myStory = viewModel.myStory
    var filterDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = { HomeTabBar(tabSelected, onTabSelected = { tabSelected = it }) },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = navigateSearch) {
                    Icon(imageVector = KapirtiIcons.Search, contentDescription = "add")
                }
                Spacer(Modifier.height(16.dp))
                FloatingActionButton(onClick = {filterDialog = true}) {
                    Icon(imageVector = KapirtiIcons.Filter, contentDescription = "add")
                }
                Spacer(Modifier.height(16.dp))

                if (tabSelected == CraneScreen.Language) {
                    FloatingActionButton(onClick = navigateLanguage) {
                        Icon(imageVector = KapirtiIcons.Language, contentDescription = "add")
                    }
                } else {
                    FloatingActionButton(onClick = navigateCountry) {
                        Icon(imageVector = KapirtiIcons.Flag, contentDescription = "add")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        HomeBackground(Modifier.fillMaxSize())
        SearchContent(
            tabSelected,
            usersFlirt,//.filterNot { it.id == myId },
            usersMarriage,
            languageUsers,
            onItemFlirtClicked = {
                includeAccountViewModel.addFlirt(it)
                navigateUser(it.id, myId)
            },
            onItemMarriageClicked = {
                includeAccountViewModel.addMarriage(it)
                navigateUser(it.id, myId)
            },
            onItemLanguageClicked = {
                includeAccountViewModel.addLanguage(it)
                navigateUser(it.id, myId)
            },
            hotels = hotels,
            restaurants = restaurants,
            cafes = cafes,
            onItemCafeClicked = {
                includeAccountViewModel.addCafe(it)
                navigateUser(it.id, myId)
            },
            onItemRestaurantClicked = {
                includeAccountViewModel.addRestaurant(it)
                navigateUser(it.id, myId)
            },
            onItemHotelClicked = {
                includeAccountViewModel.addHotel(it)
                navigateUser(it.id, myId)
            },
            modifier = modifier.padding(innerPadding),
            myId = myId ?: "",
            myName = myName ?: "",
            myPhoto = myPhoto ?: "",
            stories = stories,
            myStory = myStory,
            onCreateStoryClick = { viewModel.onAddClick(onCreateStoryClick) },
            onStoryWatchClick = navigateStory,
        )
    }

    if(filterDialog) { FilterScreen(onDismiss = { filterDialog = false }) }
}



@Composable
private fun HomeTabBar(
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    CraneTabBar(
        modifier = modifier,
    ) { tabBarModifier ->
        CraneTabsUsers(
            modifier = tabBarModifier,
            titles = CraneScreen.values().map { it.name },
            tabSelected = tabSelected,
            onTabSelected = { newTab -> onTabSelected(CraneScreen.values()[newTab.ordinal]) }
        )
    }
}


@Composable
private fun SearchContent(
    tabSelected: CraneScreen,
    flirtUsers: List<UserFlirt>,
    marriageUsers: List<UserMarriage>,
    languageUsers: List<UserLP>,
    onItemFlirtClicked: (UserFlirt) -> Unit,
    onItemMarriageClicked: (UserMarriage) -> Unit,
    onItemLanguageClicked: (UserLP) -> Unit,
    hotels: List<Hotel>,
    restaurants: List<Restaurant>,
    cafes: List<Cafe>,
    onItemHotelClicked: (Hotel) -> Unit,
    onItemRestaurantClicked: (Restaurant) -> Unit,
    onItemCafeClicked: (Cafe) -> Unit,
    myId: String,
    myName: String,
    myPhoto: String,
    myStory: Story?,
    stories: List<Story>,
    onCreateStoryClick: () -> Unit,
    onStoryWatchClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (tabSelected) {
        CraneScreen.Flirt -> FlirtContent(flirtUsers, onItemFlirtClicked,
            myId = myId, myName = myName, myPhoto = myPhoto, stories = stories, onCreateStoryClick = onCreateStoryClick,
            onStoryWatchClick = onStoryWatchClick, myStory = myStory, modifier = modifier)
        CraneScreen.Marriage -> MarriageContent(marriageUsers, onItemMarriageClicked,
            myId = myId, myName = myName, myPhoto = myPhoto, stories = stories, onCreateStoryClick = onCreateStoryClick,
            onStoryWatchClick = onStoryWatchClick, myStory = myStory, modifier = modifier)
        CraneScreen.Language -> LanguageContent(languageUsers, onItemLanguageClicked,
            myId = myId, myName = myName, myPhoto = myPhoto, stories = stories,
            onCreateStoryClick = onCreateStoryClick, onStoryWatchClick = onStoryWatchClick,
            myStory = myStory, modifier = modifier)
        CraneScreen.Cafes -> CafesContent(cafes, onItemCafeClicked,
            myId = myId, myName = myName, myPhoto = myPhoto, stories = stories, onCreateStoryClick = onCreateStoryClick,
            onStoryWatchClick = onStoryWatchClick, myStory = myStory, modifier = modifier)
        CraneScreen.Restaurants -> RestaurantsContent(restaurants, onItemRestaurantClicked,
            myId = myId, myName = myName, myPhoto = myPhoto, stories = stories, onCreateStoryClick = onCreateStoryClick,
            onStoryWatchClick = onStoryWatchClick, myStory = myStory, modifier = modifier)
        CraneScreen.Hotels -> HotelsContent(hotels, onItemHotelClicked,
            myId = myId, myName = myName, myPhoto = myPhoto, stories = stories, onCreateStoryClick = onCreateStoryClick,
            onStoryWatchClick = onStoryWatchClick, myStory = myStory, modifier = modifier)
    }
}
