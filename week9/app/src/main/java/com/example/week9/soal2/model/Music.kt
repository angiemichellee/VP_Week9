package com.example.week9.soal2.model

/** Model domain yang dipakai UI Soal 2. */
data class Artist(
    val id: String,
    val name: String,
    val genre: String,
    val country: String,
    val thumbUrl: String?,
    val bannerUrl: String?,
    val biography: String,
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val year: String,
    val genre: String,
    val coverUrl: String?,
    val description: String,
)

data class Track(
    val id: String,
    val number: Int,
    val title: String,
    val genre: String,
    val durationMs: Long,
) {
    /** 318626 ms -> "05:18" */
    val durationText: String
        get() {
            val totalSec = durationMs / 1000
            return "%02d:%02d".format(totalSec / 60, totalSec % 60)
        }
}

/** Data halaman artis: info artis + daftar albumnya. */
data class ArtistPage(
    val artist: Artist,
    val albums: List<Album>,
)

/** Data halaman detail album: info album + daftar lagu. */
data class AlbumDetail(
    val album: Album,
    val tracks: List<Track>,
)