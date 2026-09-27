package com.shamtube.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shamtube.app.R
import com.shamtube.app.data.model.Category
import com.shamtube.app.ui.components.AppLogo
import com.shamtube.app.ui.components.CategoryRow
import com.shamtube.app.ui.components.EmptyState
import com.shamtube.app.ui.components.VideoCard
import com.shamtube.app.viewmodel.ShamTubeViewModel

@Composable
fun HomeScreen(
    viewModel: ShamTubeViewModel,
    onOpenVideo: (String) -> Unit,
    onOpenChannel: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    val videos by viewModel.feed.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { AppLogo() },
            actions = {
                IconButton(onClick = onOpenSearch) {
                    Icon(Icons.Outlined.Search, contentDescription = stringResource(R.string.search))
                }
                BadgedBox(
                    badge = { Badge { Text("3") } },
                ) {
                    IconButton(onClick = onOpenNotifications) {
                        Icon(
                            Icons.Outlined.NotificationsNone,
                            contentDescription = stringResource(R.string.notifications),
                        )
                    }
                }
                IconButton(onClick = onOpenProfile) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.profile),
                    )
                }
            },
        )
        CategoryRow(
            selected = selectedCategory,
            onSelect = viewModel::selectCategory,
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (videos.isEmpty()) {
            EmptyState(
                title = stringResource(R.string.no_videos),
                message = stringResource(R.string.app_tagline),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(
                            text = when (selectedCategory) {
                                Category.ALL -> "مختارات لك"
                                else -> selectedCategory.arabic
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "${videos.size} فيديو",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                items(videos, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        onClick = { onOpenVideo(video.id) },
                        onChannelClick = { onOpenChannel(video.channelId) },
                        onMoreClick = {},
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
fun ShortsScreen(
    viewModel: ShamTubeViewModel,
    onOpenVideo: (String) -> Unit,
) {
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Text("القصص", style = MaterialTheme.typography.headlineLarge)
            Text(
                "مقاطع سريعة تستحق لحظة من يومك",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        items(videos.chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                pair.forEach { video ->
                    com.shamtube.app.ui.components.ShortVideoCard(
                        video = video,
                        onClick = { onOpenVideo(video.id) },
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.width(160.dp))
            }
        }
    }
}