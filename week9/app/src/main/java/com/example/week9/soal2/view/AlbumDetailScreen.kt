package com.example.week9.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week9.soal2.model.AlbumDetail
import com.example.week9.soal2.model.Track
import com.example.week9.soal2.viewmodel.AlbumDetailUiState
import com.example.week9.soal2.viewmodel.AlbumDetailViewModel

@Composable
fun AlbumDetailScreen(
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel(factory = AlbumDetailViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumDetailContent(state = state, onBackClick = onBackClick, onRetry = viewModel::load)
}

@Composable
fun AlbumDetailContent(state: AlbumDetailUiState, onBackClick: () -> Unit, onRetry: () -> Unit) {
    Scaffold(containerColor = MusicColors.Background) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = MusicColors.TextPrimary)
                }
                Text("Detail Album", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Box(Modifier.fillMaxSize()) {
                when (state) {
                    AlbumDetailUiState.Loading -> MusicLoading(
                        "Memuat detail album & track list...",
                        Modifier.align(Alignment.Center),
                    )
                    is AlbumDetailUiState.Error -> MusicError(state.message, onRetry, Modifier.align(Alignment.Center))
                    is AlbumDetailUiState.Success -> AlbumDetailBody(state.detail, innerPadding.calculateBottomPadding())
                }
            }
        }
    }
}

@Composable
private fun AlbumDetailBody(detail: AlbumDetail, bottomPadding: Dp) {
    val album = detail.album
    var descExpanded by rememberSaveable(album.id) { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = bottomPadding + 16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(key = "cover") {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(8.dp))
                MusicImage(
                    url = album.coverUrl,
                    contentDescription = album.title,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    album.title,
                    color = MusicColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                )
                Text(album.artist, color = MusicColors.Green, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (album.year.isNotEmpty()) InfoChip("Tahun ${album.year}")
                    if (album.genre.isNotEmpty()) InfoChip(album.genre)
                }
            }
        }

        item(key = "desc") {
            Column(Modifier.padding(top = 20.dp)) {
                Text("Deskripsi Album", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.height(6.dp))
                if (album.description.isNotEmpty()) {
                    ExpandableText(
                        text = album.description,
                        expanded = descExpanded,
                        onToggle = { descExpanded = !descExpanded },
                        collapsedLines = 3,
                    )
                } else {
                    Text("Belum ada deskripsi untuk album ini.", color = MusicColors.TextSecondary, fontSize = 13.sp)
                }
            }
        }

        item(key = "tracks-header") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            ) {
                Icon(Icons.Filled.MusicNote, contentDescription = null, tint = MusicColors.Green, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "Daftar Lagu",
                    color = MusicColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f),
                )
                CountBadge("${detail.tracks.size} Lagu")
            }
        }

        if (detail.tracks.isEmpty()) {
            item(key = "no-tracks") {
                Text("Daftar lagu tidak tersedia.", color = MusicColors.TextSecondary, fontSize = 13.sp)
            }
        }

        itemsIndexed(detail.tracks, key = { _, t -> t.id }) { index, track ->
            TrackRow(track = track, fallbackNumber = index + 1)
            if (index < detail.tracks.lastIndex) HorizontalDivider(color = MusicColors.Border)
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Text(
        text,
        color = MusicColors.TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(MusicColors.SurfaceHigh, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Composable
private fun TrackRow(track: Track, fallbackNumber: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
    ) {
        Text(
            "%02d".format(if (track.number > 0) track.number else fallbackNumber),
            color = MusicColors.Green,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.width(36.dp),
        )
        Column(Modifier.weight(1f)) {
            Text(
                track.title,
                color = MusicColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (track.genre.isNotEmpty()) {
                Text(track.genre, color = MusicColors.TextSecondary, fontSize = 11.sp)
            }
        }
        Icon(Icons.Filled.Schedule, contentDescription = null, tint = MusicColors.TextSecondary, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(4.dp))
        Text(track.durationText, color = MusicColors.TextSecondary, fontSize = 12.sp)
    }
}