package com.kapirti.social_chat_food_video.ui.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kapirti.social_chat_food_video.common.composable.BasicDivider
import com.kapirti.social_chat_food_video.common.composable.LuccaSurface
import com.kapirti.social_chat_food_video.common.ext.search.NoResults
import com.kapirti.social_chat_food_video.common.ext.search.SearchBar
import com.kapirti.social_chat_food_video.common.ext.search.SearchRepo
import com.kapirti.social_chat_food_video.common.ext.search.SearchResults
import com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname.Nickname

@Composable
fun SearchScreen(
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    state: SearchState = rememberSearchState()
) {
    val nicknames = viewModel.nicknames.collectAsStateWithLifecycle(initialValue = emptyList())
//    val recents = viewModel.recents.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(){ innerPadding ->//topBar = { AdsBannerToolbar("ADS_SEARCH_BANNER_ID") }) {
        LuccaSurface(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())
                SearchBar(
                    query = state.query,
                    onQueryChange = { state.query = it },
                    searchFocused = state.focused,
                    onSearchFocusChange = { state.focused = it },
                    onClearQuery = {
                        if(state.query.text.isNotEmpty()){
                            state.query = TextFieldValue("")
                        } else {
                            popUp()
                        }
                        //state.query = TextFieldValue("")
                    },
                    searching = state.searching
                )
                BasicDivider()

                LaunchedEffect(state.query.text) {
                    state.searching = true
                    state.searchResults = SearchRepo.search(state.query.text, nicknames.value)
                    state.searching = false
                }
                when (state.searchDisplay) {
                    SearchDisplay.Categories -> SearchSuggestions(
                        suggestions = emptyList(), //recents.value,
                        onDeleteClick = { },//viewModel.onDeleteClick(it) },
                        onSuggestionSelect = { suggestion ->
                            state.query = TextFieldValue(suggestion)
                        }
                    )
                    SearchDisplay.Suggestions -> SearchSuggestions(
                        suggestions = emptyList(),// recents.value,
                        onDeleteClick = { },//viewModel.onDeleteClick(it) },
                        onSuggestionSelect = { suggestion ->
                            state.query = TextFieldValue(suggestion)
                        }
                    )

                    SearchDisplay.Results -> SearchResults(
                        state.searchResults,
                        onUserClick = { itUser ->
                           // includeUserIdViewModel.addPartnerId(itUser.id)
                           // viewModel.onSearchClick(user = itUser, navigateAndPopUpSearchToUserProfile)
                        }
                    )

                    SearchDisplay.NoResults -> NoResults(state.query.text)
                }
            }
        }
    }
}

enum class SearchDisplay {
    Categories, Suggestions, Results, NoResults
}

@Composable
private fun rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
    //categories: List<SearchCategoryCollection> = SearchRepo.getCategories(),
    searchResults: List<Nickname> = emptyList()
): SearchState {
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching,
          //  categories = categories,
            searchResults = searchResults
        )
    }
}

@Stable
class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    //categories: List<SearchCategoryCollection>,
    searchResults: List<Nickname>
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
   // var categories by mutableStateOf(categories)
    var searchResults by mutableStateOf(searchResults)
    val searchDisplay: SearchDisplay
        get() = when {
            !focused && query.text.isEmpty() -> SearchDisplay.Categories
            focused && query.text.isEmpty() -> SearchDisplay.Suggestions
            searchResults.isEmpty() -> SearchDisplay.NoResults
            else -> SearchDisplay.Results
        }
}

