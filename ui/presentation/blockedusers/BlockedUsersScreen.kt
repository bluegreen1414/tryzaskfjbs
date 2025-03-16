package com.kapirti.social_chat_food_video.ui.presentation.blockedusers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.kapirti.social_chat_food_video.common.composable.BasicDivider
import com.kapirti.social_chat_food_video.common.composable.LuccaSurface
import com.kapirti.social_chat_food_video.common.ext.search.NoResults
import com.kapirti.social_chat_food_video.common.ext.search.SearchBar
import com.kapirti.social_chat_food_video.common.ext.search.SearchBlockResults
import com.kapirti.social_chat_food_video.common.ext.search.SearchRepo
import com.kapirti.social_chat_food_video.model.Block
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.common.ext.search.SearchResult

@Composable
fun BlockedUsersScreen(
    blockedUsers: List<Block>,
    modifier: Modifier = Modifier,
    state: SearchState = rememberSearchState()
) {
    Scaffold { innerPadding ->
        LuccaSurface(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            Column {
                SearchBar(
                    query = state.query,
                    onQueryChange = { state.query = it },
                    searchFocused = state.focused,
                    onSearchFocusChange = { state.focused = it },
                    onClearQuery = { state.query = TextFieldValue("") },
                    searching = state.searching
                )
                BasicDivider()

                LaunchedEffect(state.query.text) {
                    state.searching = true
                    state.searchResults = SearchRepo.searchBlockUser(state.query.text, items = blockedUsers)
                    state.searching = false
                }
                when (state.searchDisplay) {
                    SearchDisplay.Categories -> { BlockedUsersBody(blockedUsers) } //SearchCategories(state.categories)
                    SearchDisplay.Suggestions -> {} /**SearchSuggestions(
                        suggestions = recents.value,
                        onDeleteClick = { viewModel.onDeleteClick(it) },
                        onSuggestionSelect = { suggestion ->
                            state.query = TextFieldValue(suggestion)
                        }
                    )*/

                    SearchDisplay.Results -> SearchBlockResults(state.searchResults)

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
   // categories: List<SearchCategoryCollection> = SearchRepo.getCategories(),
    searchResults: List<Block> = emptyList()
): SearchState {
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching,
            //categories = categories,
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
    searchResults: List<Block>
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

@Composable
private fun BlockedUsersBody(
    block: List<Block>,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(AppText.search_count, block.size),
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xffded6fe),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        LazyColumn(state = scrollState) {
            items(block, key = { it.id }) { blockUser ->
                SearchResult(blockUser.id, blockUser.nickname, {})
            }
        }
    }
}
