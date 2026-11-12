package com.example.week9.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.week9.soal2.model.Album

@Composable
fun MusicHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(
            Modifier
                .size(34.dp)
                .background(MusicColors.Green, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Color(0xFF0B1A10), modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text("Music Artist", color = MusicColors.TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MusicSearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text("Cari artis...", color = MusicColors.TextSecondary) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = MusicColors.Green) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Hapus", tint = MusicColors.TextSecondary)
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch()
        }),
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MusicColors.Surface,
            unfocusedContainerColor = MusicColors.Surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = MusicColors.TextPrimary,
            unfocusedTextColor = MusicColors.TextPrimary,
            cursorColor = MusicColors.Green,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
            .border(1.dp, MusicColors.Border, RoundedCornerShape(28.dp)),
    )
}

@Composable
fun MusicImage(url: String?, contentDescription: String?, modifier: Modifier = Modifier) {
    val placeholder: @Composable () -> Unit = {
        Box(Modifier.fillMaxSize().background(MusicColors.SurfaceHigh), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Album, contentDescription = null, tint = MusicColors.TextSecondary)
        }
    }
    if (url == null) {
        Box(modifier) { placeholder() }
    } else {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            loading = { placeholder() },
            error = { placeholder() },
        )
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        MusicImage(
            url = album.coverUrl,
            contentDescription = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp)),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            album.title,
            color = MusicColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(2.dp))
        Row {
            Text(album.year.ifEmpty { "-" }, color = MusicColors.Green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text(
                album.genre,
                color = MusicColors.TextSecondary,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun CountBadge(text: String) {
    Text(
        text,
        color = MusicColors.Green,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(MusicColors.Green.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
fun MusicLoading(message: String, modifier: Modifier = Modifier) {
    Column(modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = MusicColors.Green, strokeWidth = 3.dp)
        Spacer(Modifier.height(16.dp))
        Text(message, color = MusicColors.TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun MusicError(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = MusicColors.Red, modifier = Modifier.size(44.dp))
        Spacer(Modifier.height(12.dp))
        Text("Terjadi Kendala", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(Modifier.height(4.dp))
        Text(message, color = MusicColors.TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MusicColors.Green, contentColor = Color(0xFF0B1A10)),
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Coba Lagi", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ExpandableText(text: String, expanded: Boolean, onToggle: () -> Unit, collapsedLines: Int = 4) {
    Text(
        text,
        color = MusicColors.TextSecondary,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        maxLines = if (expanded) Int.MAX_VALUE else collapsedLines,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        if (expanded) "Tutup ringkasan" else "Baca selengkapnya",
        color = MusicColors.Green,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable(onClick = onToggle).padding(vertical = 4.dp),
    )
}