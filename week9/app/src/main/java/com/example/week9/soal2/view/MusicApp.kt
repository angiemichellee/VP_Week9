package com.example.week9.soal2.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
data object ArtistRoute

@Serializable
data class AlbumRoute(val albumId: String)

@Composable
fun MusicApp() {
    MusicTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = ArtistRoute) {
            composable<ArtistRoute> {
                ArtistScreen(onAlbumClick = { id -> navController.navigate(AlbumRoute(id)) })
            }
            composable<AlbumRoute> {
                AlbumDetailScreen(onBackClick = { navController.navigateUp() })
            }
        }
    }
}