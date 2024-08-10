package com.example.chatvox.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.lerp
import com.example.chatvox.R
import com.example.chatvox.data.VoicevoxDataStore
import com.example.chatvox.ui.component.ChatVoxTopAppBar
import com.example.chatvox.ui.navigation.Screens
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    toChatScreen: (VoicevoxDataStore.VoicevoxType) -> Unit,
    toSettingsScreen: () -> Unit,
) {
    val actualPageCount = VoicevoxDataStore.list.size
    val loopPageCount = actualPageCount * 4
    val pagerState = rememberPagerState(
        initialPage = actualPageCount,
        pageCount = { loopPageCount }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChatVoxTopAppBar(
                title = stringResource(id = R.string.app_name),
                currentScreen = Screens.Home,
                action = toSettingsScreen
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(vertical = dimensionResource(id = R.dimen.medium_padding)),
        ) {
            val currentItem = VoicevoxDataStore.list[pagerState.currentPage % actualPageCount]

            Text(
                text = currentItem.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.extra_large_padding),
                        vertical = dimensionResource(id = R.dimen.medium_padding)
                    ),
                color = MaterialTheme.colorScheme.primary,
            )
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(id = R.dimen.extra_large_padding),
                    vertical = dimensionResource(id = R.dimen.small_padding)
                ),
            ) { page ->
                val actualPage = page % actualPageCount
                val pageOffset =
                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset
                            )
                        }
                        .scale(
                            lerp(
                                start = 0.9f,
                                stop = 1f,
                                fraction = 1f - pageOffset
                            )
                        )
                        .clickable(
                            onClick = {
                                toChatScreen(VoicevoxDataStore.list[actualPage].voicevoxType)
                            }
                        ),
                ) {
                    Image(
                        painter = painterResource(id = VoicevoxDataStore.list[actualPage].image),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(0.75f)
                            .scale(
                                lerp(
                                    start = 0.9f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset
                                )
                            ),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.extra_large_padding),
                        vertical = dimensionResource(id = R.dimen.medium_padding)
                    )
            ) {
                Text(
                    text = "プロフィール",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.tiny_padding)))
                Text(
                    text = currentItem.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = currentItem.credit,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }


            LaunchedEffect(pagerState.currentPage) {
                // ページをループさせる
                if (pagerState.currentPage < actualPageCount) {
                    pagerState.scrollToPage(pagerState.currentPage + actualPageCount)
                } else if (pagerState.currentPage >= actualPageCount * 2) {
                    pagerState.scrollToPage(pagerState.currentPage - actualPageCount)
                }
            }
        }
    }
}