@file:OptIn(ExperimentalLayoutApi::class)

package com.kapirti.social_chat_food_video.ui.presentation.filter


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kapirti.social_chat_food_video.R.string as AppText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kapirti.social_chat_food_video.common.composable.BasicDivider
import com.kapirti.social_chat_food_video.core.constants.ConsGender.MALE
import com.kapirti.social_chat_food_video.model.LuccaRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    genderSelected: String = "false",
    onGenderClick: (String) -> Unit = {},
    onDismiss: () -> Unit
) {
    var maxCalories by remember { mutableFloatStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = AppText.cancel)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(AppText.filters_title),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Open search */ }) {
                            Icon(
                                Icons.Default.LockReset,
                                contentDescription = stringResource(id = AppText.filters_title)
                            )
                        }
                        /**val resetEnabled = sortState != defaultFilter
                        IconButton(
                        onClick = { /* TODO: Open search */ },
                        enabled = resetEnabled
                        ) {
                        val alpha = if (resetEnabled) {
                        ContentAlpha.high
                        } else {
                        ContentAlpha.disabled
                        }
                        CompositionLocalProvider(LocalContentAlpha provides alpha) {
                        Text(
                        text = "stringResource(id = R.string.reset)",
                        style = MaterialTheme.typography.body2
                        )
                        }
                        }*/
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                )
            },
            modifier = Modifier.padding(vertical = 20.dp),
            containerColor = Color.Yellow
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                GenderSection(
                    genderSelected = genderSelected,
                    onGenderClick = onGenderClick,
                    filters = LuccaRepo.getGender()
                )

                Spacer(modifier = Modifier.height(10.dp))
                BasicDivider()
                Spacer(modifier = Modifier.height(5.dp))

                MaxCalories(
                    sliderPosition = maxCalories,
                    onValueChanged = { newValue ->
                        maxCalories = newValue
                    }
                )

            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenderSection(
    genderSelected: String,
    onGenderClick: (String) -> Unit,
    filters: List<String>
) {
    FilterTitle(text = stringResource(AppText.gender))
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)
            .padding(horizontal = 4.dp)
    ) {
        filters.forEach { filter ->
            SectionItem(
                text = if (filter == MALE) {
                    stringResource(AppText.male)
                } else { stringResource (AppText.female)},
                selected = if (genderSelected == filter) true else false,
                onClicked = { onGenderClick(filter) },
            )
        }
    }
}

@Composable
private fun FilterTitle(text: String) {
    Text(
        text = text,
        fontSize = 40.sp,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.titleSmall,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun SectionItem(text: String, selected: Boolean, onClicked: () -> Unit){
    val color = if(selected) Color.Blue else Color.Black
    Text(
        text = text,
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier
            .border(1.dp, color, CircleShape)
            .background(color, CircleShape)
            .clickable { onClicked() }
            .padding(10.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MaxCalories(sliderPosition: Float, onValueChanged: (Float) -> Unit) {
    FlowRow {
        FilterTitle(text = stringResource(id = AppText.age))
        Text(
            text = "stringResource(id = AppText.per_serving)",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(top = 5.dp, start = 10.dp)
        )
    }
    Slider(
        value = sliderPosition,
        onValueChange = { newValue ->
            onValueChanged(newValue)
        },
        valueRange = 0f..300f,
        steps = 5,
        modifier = Modifier
            .fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.onBackground,
            activeTrackColor = MaterialTheme.colorScheme.onSurface
        )
    )
}



    /**
    var sortState by remember { mutableStateOf(SnackRepo.getSortDefault()) }
    val defaultFilter = SnackRepo.getSortDefault()


        val priceFilters = remember { SnackRepo.getPriceFilters() }
        val categoryFilters = remember { SnackRepo.getCategoryFilters() }
        val lifeStyleFilters = remember { SnackRepo.getLifeStyleFilters() }

                SortFiltersSection(
                    sortState = sortState,
                    onFilterChange = { filter ->
                        sortState = filter.name
                    }
                )
                FilterChipSection(
                    title = "stringResource(id = R.string.price)",
                    filters = priceFilters
                )
    FilterChipSection(
    title = "stringResource(id = R.string.category)",
    filters = categoryFilters
    )


                FilterChipSection(
                    title = "stringResource(id = R.string.lifestyle)",
                    filters = lifeStyleFilters
                )
            }
        }
    }
}



@Composable
fun SortFiltersSection(sortState: String, onFilterChange: (Filter) -> Unit) {
    FilterTitle(text = "stringResource(id = R.string.sort)")
    Column(Modifier.padding(bottom = 24.dp)) {
        SortFilters(
            sortState = sortState,
            onChanged = onFilterChange
        )
    }
}

@Composable
fun SortFilters(
    sortFilters: List<Filter> = SnackRepo.getSortFilters(),
    sortState: String,
    onChanged: (Filter) -> Unit
) {

    sortFilters.forEach { filter ->
        SortOption(
            text = filter.name,
            icon = filter.icon,
            selected = sortState == filter.name,
            onClickOption = {
                onChanged(filter)
            }
        )
    }
}

@Composable
fun SortOption(
    text: String,
    icon: ImageVector?,
    onClickOption: () -> Unit,
    selected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(top = 14.dp)
            .selectable(selected) { onClickOption() }
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}*/
