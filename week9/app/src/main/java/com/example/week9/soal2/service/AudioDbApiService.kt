package com.example.week9.soal2.service

import retrofit2.http.GET
import retrofit2.http.Query

/** Endpoint TheAudioDB (base: https://www.theaudiodb.com/api/v1/json/123/). */
interface AudioDbApiService {

    /** Cari artis: search.php?s={artistName} */
    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistsResponseDto

    /** Cari album artis: searchalbum.php?s={artistName} */
    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") artistName: String): AlbumsResponseDto

    /** Detail album: album.php?m={albumId} */
    @GET("album.php")
    suspend fun getAlbum(@Query("m") albumId: String): AlbumsResponseDto

    /** Track album: track.php?m={albumId} */
    @GET("track.php")
    suspend fun getTracks(@Query("m") albumId: String): TracksResponseDto
}