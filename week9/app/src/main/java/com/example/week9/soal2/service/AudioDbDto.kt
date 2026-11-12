package com.example.week9.soal2.service

import kotlinx.serialization.Serializable

/**
 * DTO = bentuk JSON persis dari TheAudioDB.
 * Catatan: TheAudioDB mengirim angka sebagai string ("2002") dan list kosong sebagai null.
 */
@Serializable
data class ArtistsResponseDto(val artists: List<ArtistDto>? = null)

@Serializable
data class AlbumsResponseDto(val album: List<AlbumDto>? = null)

@Serializable
data class TracksResponseDto(val track: List<TrackDto>? = null)

@Serializable
data class ArtistDto(
    val idArtist: String,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strCountry: String? = null,
    val strArtistThumb: String? = null,
    val strArtistFanart: String? = null,
    val strArtistWideThumb: String? = null,
    val strArtistBanner: String? = null,
    val strBiography: String? = null,
    val strBiographyEN: String? = null,
)

@Serializable
data class AlbumDto(
    val idAlbum: String,
    val strAlbum: String? = null,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strAlbumThumb: String? = null,
    val strDescription: String? = null,
    val strDescriptionEN: String? = null,
)

@Serializable
data class TrackDto(
    val idTrack: String,
    val strTrack: String? = null,
    val strGenre: String? = null,
    val intDuration: String? = null,
    val intTrackNumber: String? = null,
)