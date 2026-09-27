package com.shamtube.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shamtube.app.R
import com.shamtube.app.data.model.Channel
import com.shamtube.app.ui.components.ChannelHeader
import com.shamtube.app.ui.components.EmptyState
import com.shamtube.app.ui.components.VideoCard
import com.shamtube.app.viewmodel.ShamTubeViewModel

@Composable
fun ChannelScreen(
    channelId: String,
    viewModel: ShamTubeViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenVideo: (String) -> Unit,
) {
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val channel = channels.firstOrNull { it.id == channelId }
    var selectedTab by remember { mutableIntStateOf(0) }

    if (channel == null) {
        EmptyState(stringResource(R.string.channel), stringResource(R.string.no_videos))
        return
    }

    val channelVideos = videos.filter { it.channelId == channel.id }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(channel.name) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            actions = {
                IconButton(onClick = onOpenSettings) {
                    Icon(Icons.Outlined.Settings, contentDescription = stringResource(R.string.settings))
                }
            },
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                ChannelHeader(
                    channel = channel,
                    onSubscribe = { viewModel.toggleSubscription(channel) },
                )
            }
            item {
                TabRow(selectedTabIndex = selectedTab) {
                    listOf("الفيديوهات", "قصيرة", "قوائم التشغيل").forEachIndexed { index, label ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(label) },
                        )
                    }
                }
            }
            if (selectedTab == 0) {
                items(channelVideos, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        onClick = { onOpenVideo(video.id) },
                        onChannelClick = {},
                        onMoreClick = {},
                    )
                }
            } else {
                item {
                    EmptyState(
                        title = if (selectedTab == 1) "لا توجد مقاطع قصيرة" else "لا توجد قوائم تشغيل",
                        message = "ستظهر محتويات القناة هنا",
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}