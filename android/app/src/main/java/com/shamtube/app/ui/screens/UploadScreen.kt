package com.shamtube.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shamtube.app.R
import com.shamtube.app.data.model.Category
import com.shamtube.app.ui.components.CategoryChip
import com.shamtube.app.viewmodel.ShamTubeViewModel

@Composable
fun UploadScreen(
    viewModel: ShamTubeViewModel,
    onUploadComplete: () -> Unit,
) {
    var videoUri by remember { mutableStateOf<String?>(null) }
    var thumbnailUri by remember { mutableStateOf<String?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.ENTERTAINMENT) }
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    val message by viewModel.uploadMessage.collectAsStateWithLifecycle()

    val videoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri -> videoUri = uri?.toString() }
    val thumbnailPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri -> thumbnailUri = uri?.toString() }

    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text(stringResource(R.string.upload_video), style = MaterialTheme.typography.headlineLarge)
            Text(
                stringResource(R.string.upload_ready),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 5.dp),
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                UploadPickerCard(
                    icon = Icons.Outlined.VideoLibrary,
                    title = stringResource(R.string.choose_video),
                    selected = videoUri != null,
                    modifier = Modifier.weight(1f),
                    onClick = { videoPicker.launch("video/*") },
                )
                UploadPickerCard(
                    icon = Icons.Outlined.Image,
                    title = stringResource(R.string.choose_thumbnail),
                    selected = thumbnailUri != null,
                    modifier = Modifier.weight(1f),
                    onClick = { thumbnailPicker.launch("image/*") },
                )
            }
        }
        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.video_title)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
            )
        }
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth().height(130.dp),
                label = { Text(stringResource(R.string.description)) },
                maxLines = 5,
                shape = RoundedCornerShape(14.dp),
            )
        }
        item {
            Text("التصنيف", style = MaterialTheme.typography.titleMedium)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                items(Category.entries.drop(1).size) { index ->
                    val item = Category.entries.drop(1)[index]
                    CategoryChip(item, item == category) { category = item }
                }
            }
        }
        item {
            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.tags)) },
                placeholder = { Text("سوريا، شام، ...") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
            )
        }
        item {
            Button(
                onClick = {
                    if (videoUri != null) {
                        viewModel.uploadVideo(
                            videoUri = videoUri.orEmpty(),
                            thumbnailUri = thumbnailUri,
                            title = title,
                            description = description,
                            category = category,
                            tagsText = tags,
                        )
                    }
                },
                enabled = videoUri != null && title.isNotBlank() && !isUploading,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(stringResource(R.string.uploading))
                } else {
                    Icon(Icons.Outlined.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(stringResource(R.string.upload_video))
                }
            }
        }
        message?.let { status ->
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        status,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(14.dp),
                    )
                }
            }
        }
        item {
            Text(
                "الفيديوهات المختارة تحفظ على هذا الجهاز في الإصدار الحالي. يمكن تبديل مصدر التخزين لاحقاً دون تغيير واجهة التطبيق.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun UploadPickerCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(
                if (selected) "تم الاختيار" else "اضغط للاختيار",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}