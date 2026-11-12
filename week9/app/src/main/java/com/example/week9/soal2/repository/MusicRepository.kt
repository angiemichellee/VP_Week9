package com.example.week9.soal2.repository

import com.example.week9.core.AppException
import com.example.week9.core.safeCall
import com.example.week9.soal2.model.Album
import com.example.week9.soal2.model.AlbumDetail
import com.example.week9.soal2.model.Artist
import com.example.week9.soal2.model.ArtistPage
import com.example.week9.soal2.model.Track
import com.example.week9.soal2.service.AlbumDto
import com.example.week9.soal2.service.ArtistDto
import com.example.week9.soal2.service.AudioDbApiService
import com.example.week9.soal2.service.TrackDto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MusicRepository(private val api: AudioDbApiService) {

    /** Ambil info artis + album-albumnya secara paralel (2 request sekaligus dengan async). */
    suspend fun getArtistPage(artistName: String): Result<ArtistPage> = safeCall {
        coroutineScope {
            val artistDeferred = async { api.searchArtist(artistName.trim()) }
            val albumsDeferred = async { api.searchAlbums(artistName.trim()) }

            val artist = artistDeferred.await().artists?.firstOrNull()
                ?: throw AppException("Artis \"$artistName\" tidak ditemukan.")
            val albums = albumsDeferred.await().album.orEmpty()
                .map { it.toDomain() }
                .sortedBy { it.year }

            ArtistPage(artist.toDomain(), albums)
        }
    }

    /** Ambil detail album + track list secara paralel. */
    suspend fun getAlbumDetail(albumId: String): Result<AlbumDetail> = safeCall {
        coroutineScope {
            val albumDeferred = async { api.getAlbum(albumId) }
            val tracksDeferred = async { api.getTracks(albumId) }

            val album = albumDeferred.await().album?.firstOrNull()
                ?: throw AppException("Album tidak ditemukan.")
            val tracks = tracksDeferred.await().track.orEmpty()
                .map { it.toDomain() }
                .sortedBy { it.number }

            AlbumDetail(album.toDomain(), tracks)
        }
    }
}

// ---------- Mapper DTO -> Model ----------
private fun String?.clean(): String = this?.takeIf { it.isNotBlank() && it != "null" }.orEmpty()

private fun ArtistDto.toDomain() = Artist(
    id = idArtist,
    name = strArtist.clean(),
    genre = strGenre.clean().ifEmpty { strStyle.clean() },
    country = strCountry.clean(),
    thumbUrl = strArtistThumb.clean().ifEmpty { null },
    bannerUrl = (strArtistFanart ?: strArtistWideThumb ?: strArtistBanner ?: strArtistThumb).clean().ifEmpty { null },
    biography = strBiographyEN.clean().ifEmpty { strBiography.clean() },
)

private fun AlbumDto.toDomain() = Album(
    id = idAlbum,
    title = strAlbum.clean(),
    artist = strArtist.clean(),
    year = intYearReleased.clean().takeIf { it != "0" }.orEmpty(),
    genre = strGenre.clean().ifEmpty { strStyle.clean() },
    coverUrl = strAlbumThumb.clean().ifEmpty { null },
    description = strDescriptionEN.clean().ifEmpty { strDescription.clean() },
)

private fun TrackDto.toDomain() = Track(
    id = idTrack,
    number = intTrackNumber?.toIntOrNull() ?: 0,
    title = strTrack.clean(),
    genre = strGenre.clean(),
    durationMs = intDuration?.toLongOrNull() ?: 0L,
)