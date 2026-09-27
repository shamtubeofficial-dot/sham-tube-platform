package com.shamtube.app.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.shamtube.app.R
import com.shamtube.app.data.model.Video
import com.shamtube.app.ui.components.Avatar
import com.shamtube.app.ui.components.CommentItem
import com.shamtube.app.ui.components.EmptyState
import com.shamtube.app.ui.components.VideoCard
import com.shamtube.app.ui.components.formatViews
import com.shamtube.app.viewmodel.ShamTubeViewModel

@Composable
fun PlayerScreen(
    videoId: String,
    viewModel: ShamTubeViewModel,
    onBack: () -> Unit,
    onOpenChannel: (String) -> Unit,
    onOpenComments: (String) -> Unit,
    onOpenVideo: (String) -> Unit,
) {
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val video = videos.firstOrNull { it.id == videoId }

    if (video == null) {
        EmptyState(stringResource(R.string.no_videos), stringResource(R.string.error_generic))
        return
    }

    val comments by viewModel.commentsFor(video.id).collectAsStateWithLifecycle()
    val recommended = videos.filter { it.id != video.id }.take(3)
    var commentText by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            TopAppBar(
                title = { Text(stringResource(R.string.watch_now)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            androidx.compose.material.icons.Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        }
        item {
            VideoPlayer(video.videoUrl)
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
                Text(video.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    "${formatViews(video.views)} مشاهدة  •  ${video.uploadDate}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    VideoAction(
                        icon = Icons.Outlined.ThumbUp,
                        label = formatViews(video.likes),
                        selected = video.likedByMe,
                        onClick = { viewModel.toggleLike(video) },
                    )
                    VideoAction(
                        icon = Icons.Outlined.ThumbDown,
                        label = stringResource(R.string.dislike),
                        onClick = {},
                    )
                    VideoAction(
                        icon = Icons.Outlined.Share,
                        label = stringResource(R.string.share),
                        onClick = {
                            context.startActivity(
                                Intent.createChooser(
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, video.title)
                                    },
                                    stringResource(R.string.share),
                                ),
                            )
                        },
                    )
                    VideoAction(
                        icon = if (video.savedByMe) {
                            androidx.compose.material.icons.Icons.Filled.Bookmark
                        } else {
                            Icons.Outlined.Bookmark
                        },
                        label = if (video.savedByMe) stringResource(R.string.saved) else stringResource(R.string.save),
                        selected = video.savedByMe,
                        onClick = { viewModel.toggleSaved(video) },
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.padding(horizontal = 18.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Avatar(video.channelAvatar, video.channelName, Modifier.size(48.dp))
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                        Text(video.channelName, style = MaterialTheme.typography.titleMedium)
                        Text("قناة موثقة على شام تيوب", style = MaterialTheme.typography.bodySmall)
                    }
                    Button(onClick = {
                        val channel = viewModel.channels.value.firstOrNull { it.id == video.channelId }
                        if (channel != null) viewModel.toggleSubscription(channel)
                    }) {
                        val channel = viewModel.channels.value.firstOrNull { it.id == video.channelId }
                        Text(if (channel?.isSubscribed == true) stringResource(R.string.subscribed) else stringResource(R.string.subscribe))
                    }
                }
            }
        }
        item {
            TextButton(
                onClick = { onOpenChannel(video.channelId) },
                modifier = Modifier.padding(horizontal = 18.dp),
            ) {
                Text("زيارة القناة")
                Icon(Icons.Outlined.ExpandMore, contentDescription = null)
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                Text(video.description, style = MaterialTheme.typography.bodyLarge, maxLines = 4)
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "${stringResource(R.string.comments)} (${video.comments})",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = { onOpenComments(video.id) }) {
                        Text(stringResource(R.string.show_more))
                    }
                }
                if (comments.isEmpty()) {
                    Text(
                        stringResource(R.string.no_comments),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    comments.take(2).forEach { comment ->
                        CommentItem(
                            comment = comment,
                            onLike = { viewModel.toggleCommentLike(comment) },
                            onReply = {
                                replyingTo = comment.id
                                commentText = "@${comment.userName} "
                            },
                        )
                    }
                }
                replyingTo?.let {
                    Text(
                        "الرد على تعليق",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                CommentInput(
                    value = commentText,
                    onValueChange = { commentText = it },
                    onSend = {
                        viewModel.addComment(video.id, commentText, replyingTo)
                        commentText = ""
                        replyingTo = null
                    },
                )
            }
        }
        if (recommended.isNotEmpty()) {
            item {
                Text(
                    "قد يعجبك أيضاً",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                )
            }
            items(recommended, key = { "recommend-${it.id}" }) { recommendedVideo ->
                VideoCard(
                    video = recommendedVideo,
                    onClick = { onOpenVideo(recommendedVideo.id) },
                    onChannelClick = { onOpenChannel(recommendedVideo.channelId) },
                    onMoreClick = {},
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CommentsScreen(
    videoId: String,
    viewModel: ShamTubeViewModel,
    onBack: () -> Unit,
) {
    val comments by viewModel.commentsFor(videoId).collectAsStateWithLifecycle()
    var commentText by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<String?>(null) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.comments)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        androidx.compose.material.icons.Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
        ) {
            if (comments.isEmpty()) {
                item {
                    EmptyState(stringResource(R.string.no_comments), "شارك أول تعليق على هذا الفيديو")
                }
            } else {
                items(comments, key = { it.id }) { comment ->
                    CommentItem(
                        comment = comment,
                        onLike = { viewModel.toggleCommentLike(comment) },
                        onReply = {
                            replyingTo = comment.id
                            commentText = "@${comment.userName} "
                        },
                    )
                }
            }
        }
        if (replyingTo != null) {
            Text(
                "الرد على تعليق",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
        }
        CommentInput(
            value = commentText,
            onValueChange = { commentText = it },
            onSend = {
                viewModel.addComment(videoId, commentText, replyingTo)
                commentText = ""
                replyingTo = null
            },
            modifier = Modifier.padding(14.dp),
        )
    }
}

@Composable
private fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val player = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }
    DisposableEffect(player) {
        onDispose { player.release() }
    }
    AndroidView(
        factory = { viewContext ->
            PlayerView(viewContext).apply {
                this.player = player
                useController = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        update = { it.player = player },
    )
}

@Composable
private fun VideoAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CommentInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.write_comment)) },
            maxLines = 3,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        )
        IconButton(onClick = onSend, enabled = value.isNotBlank()) {
            Icon(Icons.Outlined.Share, contentDescription = stringResource(R.string.done))
        }
    }
}