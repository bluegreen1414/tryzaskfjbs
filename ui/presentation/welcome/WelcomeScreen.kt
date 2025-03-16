package com.kapirti.social_chat_food_video.ui.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kapirti.social_chat_food_video.R.string as AppText
import com.kapirti.social_chat_food_video.R.drawable as AppIcon
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapirti.social_chat_food_video.ui.presentation.welcome.Tags.TAG_ONBOARD_SCREEN_IMAGE_VIEW
import com.kapirti.social_chat_food_video.ui.presentation.welcome.Tags.TAG_ONBOARD_SCREEN_NAV_BUTTON
import com.kapirti.social_chat_food_video.ui.presentation.welcome.Tags.TAG_ONBOARD_TAG_ROW

val onboardPagesList = listOf(
    OnboardPage(
        imageRes = AppIcon.logo,
        title = AppText.welcome_title,
        description = AppText.welcome_description
    ), OnboardPage(
        imageRes = AppIcon.logo,
        title = AppText.review_title,
        description = AppText.review_description
    ), OnboardPage(
        imageRes = AppIcon.logo,
        title = AppText.app_name,
        description = AppText.app_name
    )
)

object Tags {
    const val TAG_ONBOARD_SCREEN = "onboard_screen"
    const val TAG_ONBOARD_SCREEN_IMAGE_VIEW = "onboard_screen_image"
    const val TAG_ONBOARD_SCREEN_NAV_BUTTON = "nav_button"
    const val TAG_ONBOARD_TAG_ROW = "tag_row"
}


@Composable
fun WelcomeScreen(
    navigateAndPopUpWelcomeToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {

    val onboardPages = onboardPagesList

    val currentPage = remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(Tags.TAG_ONBOARD_SCREEN)
    ) {

        OnBoardImageView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            currentPage = onboardPages[currentPage.value]
        )

        OnBoardDetails(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            currentPage = onboardPages[currentPage.value]
        )

        OnBoardNavButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            currentPage = currentPage.value,
            noOfPages = onboardPages.size,
            onNextClicked = {
                currentPage.value++
            },
            onDoneClicked = {
                viewModel.saveOnBoardingState(navigateAndPopUpWelcomeToLogin)
            }
        )

        TabSelector(
            onboardPages = onboardPages,
            currentPage = currentPage.value
        ) { index ->
            currentPage.value = index
        }
    }
}

@Composable
private fun OnBoardDetails(
    modifier: Modifier = Modifier, currentPage: OnboardPage
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(currentPage.title),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(currentPage . description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun OnBoardNavButton(
    modifier: Modifier = Modifier, currentPage: Int, noOfPages: Int, onNextClicked: () -> Unit,
    onDoneClicked: () -> Unit
) {
    Button(
        onClick = {
            if (currentPage < noOfPages - 1) {
                onNextClicked()
            } else {
                onDoneClicked()
                // Handle onboarding completion
            }
        }, modifier = modifier.testTag(TAG_ONBOARD_SCREEN_NAV_BUTTON).fillMaxWidth(0.4f)
    ) {
        Text(text = if (currentPage < noOfPages - 1) stringResource(AppText.next) else "Get Started")
    }
}


@Composable
private fun OnBoardImageView(modifier: Modifier = Modifier, currentPage: OnboardPage) {
    val imageRes = currentPage.imageRes
    Box(
        modifier = modifier
            .testTag(TAG_ONBOARD_SCREEN_IMAGE_VIEW + currentPage.title)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(AppText.cd_icon),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.BottomCenter)
            .graphicsLayer {
                // Apply alpha to create the fading effect
                alpha = 0.6f
            }
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        Pair(0.8f, Color.Transparent), Pair(1f, Color.White)
                    )
                )
            ))
    }
}

@Composable
private fun TabSelector(onboardPages: List<OnboardPage>, currentPage: Int, onTabSelected: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = currentPage,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .testTag(TAG_ONBOARD_TAG_ROW)

    ) {
        onboardPages.forEachIndexed { index, _ ->
            Tab(selected = index == currentPage, onClick = {
                onTabSelected(index)
            }, modifier = Modifier.padding(16.dp), content = {
                Box(
                    modifier = Modifier
                        .testTag("$TAG_ONBOARD_TAG_ROW$index")
                        .size(8.dp)
                        .background(
                            color = if (index == currentPage) MaterialTheme.colorScheme.onPrimary
                            else Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            })
        }
    }
}

data class OnboardPage(
    @DrawableRes val imageRes: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)
