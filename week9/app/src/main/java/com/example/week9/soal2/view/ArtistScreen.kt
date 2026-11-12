package com.example.week9.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week9.soal2.model.Artist
import com.example.week9.soal2.model.ArtistPage
import com.example.week9.soal2.viewmodel.ArtistState
import com.example.week9.soal2.viewmodel.ArtistUiState
import com.example.week9.soal2.viewmodel.ArtistViewModel

@Composable
fun ArtistScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: ArtistViewModel = viewModel(factory = ArtistViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ArtistContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onAlbumClick = onAlbumClick,
    )
}

@Composable
fun ArtistContent(
    state: ArtistUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onAlbumClick: (String) -> Unit,
) {
    Scaffold(containerColor = MusicColors.Background) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            MusicHeader()
            MusicSearchBar(query = state.query, onQueryChange = onQueryChange, onSearch = onSearch)

            Box(Modifier.fillMaxSize()) {
                when (val content = state.content) {
                    ArtistState.Loading -> MusicLoading("Memuat data artis...", Modifier.align(Alignment.Center))
                    is ArtistState.Error -> MusicError(content.message, onSearch, Modifier.align(Alignment.Center))
                    is ArtistState.Success -> ArtistBody(
                        page = content.page,
                        bottomPadding = innerPadding.calculateBottomPadding(),
                        onAlbumClick = onAlbumClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun ArtistBody(page: ArtistPage, bottomPadding: Dp, onAlbumClick: (String) -> Unit) {
    var bioExpanded by rememberSaveable(page.artist.id) { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = bottomPadding + 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }, key = "hero") {
            ArtistHero(page.artist)
        }

        if (page.artist.biography.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }, key = "bio") {
                Column {
                    Text("Tentang Artis", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(6.dp))
                    ExpandableText(
                        text = page.artist.biography,
                        expanded = bioExpanded,
                        onToggle = { bioExpanded = !bioExpanded },
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }, key = "disco-header") {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Column(Modifier.weight(1f)) {
                    Text("Diskografi Album", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Koleksi rilisan resmi", color = MusicColors.TextSecondary, fontSize = 12.sp)
                }
                CountBadge("${page.albums.size} Album")
            }
        }

        if (page.albums.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }, key = "no-album") {
                Text("Belum ada album untuk artis ini.", color = MusicColors.TextSecondary, fontSize = 13.sp)
            }
        }

        items(page.albums, key = { it.id }) { album ->
            AlbumCard(album = album, onClick = { onAlbumClick(album.id) })
        }
    }
}

@Composable
private fun ArtistHero(artist: Artist) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        MusicImage(url = artist.bannerUrl, contentDescription = artist.name, modifier = Modifier.fillMaxSize())
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.35f to Color.Transparent,
                        1f to MusicColors.Background,
                    )
                )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
        ) {
            MusicImage(
                url = artist.thumbUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(2.dp, MusicColors.Background, CircleShape),
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    artist.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(artist.genre.ifEmpty { "-" }, color = MusicColors.Green, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    if (artist.country.isNotEmpty()) {
                        Text("  •  ", color = MusicColors.TextSecondary, fontSize = 12.sp)
                        Text(artist.country, color = MusicColors.TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}