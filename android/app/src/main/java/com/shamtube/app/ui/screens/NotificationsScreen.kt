package com.shamtube.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shamtube.app.R
import com.shamtube.app.data.model.Notification
import com.shamtube.app.data.model.NotificationType
import com.shamtube.app.ui.components.Avatar

private val sampleNotifications = listOf(
    Notification("n1", "فيديو جديد من شام اليوم", "صباح دمشق متاح الآن للمشاهدة", NotificationType.NEW_VIDEO, "https://i.pravatar.cc/150?img=12", "منذ 8 دقائق"),
    Notification("n2", "لديك مشترك جديد", "انضم سامر إلى قناتك", NotificationType.NEW_SUBSCRIBER, "https://i.pravatar.cc/150?img=68", "منذ ساعة"),
    Notification("n3", "أعجب ليان بفيديوك", "صباح دمشق | جولة هادئة", NotificationType.LIKE, "https://i.pravatar.cc/150?img=47", "منذ ساعتين"),
    Notification("n4", "تعليق جديد على فيديوك", "يا سلام على الصباح الدمشقي", NotificationType.COMMENT, "https://i.pravatar.cc/150?img=47", "أمس"),
    Notification("n5", "رد جديد على تعليقك", "ننتظر جلسات أكثر", NotificationType.REPLY, "https://i.pravatar.cc/150?img=44", "أمس"),
)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.notifications)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Text(
                    "آخر التنبيهات",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
            items(sampleNotifications, key = { it.id }) { notification ->
                NotificationItem(notification)
            }
        }
    }
}

@Composable
private fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.52f)
            },
        ),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(notification.imageUrl, null, Modifier.size(48.dp))
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(notification.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    notification.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    notification.timeLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Icon(
                notification.type.icon(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private fun NotificationType.icon(): ImageVector = when (this) {
    NotificationType.NEW_VIDEO -> Icons.Outlined.PlayCircleOutline
    NotificationType.NEW_SUBSCRIBER -> Icons.Outlined.PersonAddAlt
    NotificationType.LIKE -> Icons.Outlined.FavoriteBorder
    NotificationType.COMMENT, NotificationType.REPLY -> Icons.Outlined.Comment
}