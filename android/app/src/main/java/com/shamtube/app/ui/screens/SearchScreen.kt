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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shamtube.app.R
import com.shamtube.app.ui.components.EmptyState
import com.shamtube.app.ui.components.SearchBar
import com.shamtube.app.ui.components.VideoCard
import com.shamtube.app.viewmodel.ShamTubeViewModel

@Composable
fun SearchScreen(
    viewModel: ShamTubeViewModel,
    onBack: () -> Unit,
    onOpenVideo: (String) -> Unit,
    onOpenChannel: (String) -> Unit,
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val results by viewModel.searchResults.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.search)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.Tune, contentDescription = stringResource(R.string.filter))
                }
            },
        )
        SearchBar(
            value = query,
            onValueChange = viewModel::updateQuery,
            onSearch = { viewModel.submitSearch() },
            placeholder = stringResource(R.string.search_videos_channels),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        if (query.isBlank()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        stringResource(R.string.recent_searches),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                if (recentSearches.isEmpty()) {
                    item {
                        Text(
                            "ستظهر هنا الكلمات التي تبحث عنها",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                } else {
                    items(recentSearches) { term ->
                        AssistChip(
                            onClick = { viewModel.submitSearch(term) },
                            label = { Text(term) },
                            leadingIcon = {
                                Icon(Icons.Outlined.FilterList, contentDescription = null)
                            },
                        )
                    }
                }
            }
        } else if (results.isEmpty()) {
            EmptyState(
                title = stringResource(R.string.no_results),
                message = "جرّب كلمة بحث مختلفة أو استعرض التصنيفات",
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    ) {
                        Text(
                            "${results.size} نتيجة",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f),
                        )
                        TextButton(onClick = {}) { Text(stringResource(R.string.filter)) }
                    }
                }
                items(results, key = { it.id }) { video ->
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