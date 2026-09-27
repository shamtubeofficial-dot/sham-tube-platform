package com.shamtube.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shamtube.app.R
import com.shamtube.app.data.model.Video
import com.shamtube.app.ui.components.EmptyState
import com.shamtube.app.ui.components.VideoCard
import com.shamtube.app.viewmodel.ShamTubeViewModel

@Composable
fun LibraryScreen(
    viewModel: ShamTubeViewModel,
    onOpenVideo: (String) -> Unit,
    onOpenChannel: (String) -> Unit,
) {
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val saved = videos.filter { it.savedByMe }
    val liked = videos.filter { it.likedByMe }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("المكتبة", style = MaterialTheme.typography.headlineLarge)
            Text(
                "كل ما حفظته أو شاهدته في مكان واحد",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                LibraryShortcut(
                    icon = Icons.Outlined.History,
                    title = stringResource(R.string.history),
                    count = videos.size,
                    modifier = Modifier.weight(1f),
                )
                LibraryShortcut(
                    icon = Icons.Outlined.PlaylistPlay,
                    title = stringResource(R.string.playlists),
                    count = 3,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        item {
            LibraryShortcut(
                icon = Icons.Outlined.Download,
                title = stringResource(R.string.downloads),
                count = 0,
                subtitle = stringResource(R.string.downloads_placeholder),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (saved.isNotEmpty()) {
            item { Text(stringResource(R.string.watch_later), style = MaterialTheme.typography.titleLarge) }
            items(saved, key = { "saved-${it.id}" }) { video ->
                VideoCard(video, { onOpenVideo(video.id) }, { onOpenChannel(video.channelId) }, {})
            }
        }
        if (liked.isNotEmpty()) {
            item { Text(stringResource(R.string.liked_videos), style = MaterialTheme.typography.titleLarge) }
            items(liked, key = { "liked-${it.id}" }) { video ->
                VideoCard(video, { onOpenVideo(video.id) }, { onOpenChannel(video.channelId) }, {})
            }
        }
        if (saved.isEmpty() && liked.isEmpty()) {
            item {
                EmptyState(
                    title = "مكتبتك تنتظرك",
                    message = "احفظ أو أعجب بالفيديوهات لتظهر هنا",
                )
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun LibraryShortcut(
    icon: ImageVector,
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.padding(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    subtitle ?: "$count فيديو",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun SubscriptionsScreen(
    viewModel: ShamTubeViewModel,
    onOpenChannel: (String) -> Unit,
) {
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("الاشتراكات", style = MaterialTheme.typography.headlineLarge)
            Text(
                "تابع القنوات التي تحبها",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        items(channels, key = { it.id }) { channel ->
            androidx.compose.material3.ListItem(
                headlineContent = { Text(channel.name) },
                supportingContent = { Text("${channel.subscribers} مشترك") },
                leadingContent = {
                    com.shamtube.app.ui.components.Avatar(
                        channel.avatarUrl,
                        channel.name,
                        Modifier.size(52.dp),
                    )
                },
                trailingContent = {
                    androidx.compose.material3.TextButton(onClick = {
                        viewModel.toggleSubscription(channel)
                    }) {
                        Text(if (channel.isSubscribed) "مشترك" else "اشتراك")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChannel(channel.id) },
            )
        }
    }
}