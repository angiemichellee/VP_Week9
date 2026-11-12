package com.example.week9.soal1.service

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Endpoint PokemonPriceTracker. Semua fungsi `suspend` -> dipanggil dari coroutine.
 * Header Authorization (Bearer API key) ditambahkan otomatis oleh interceptor di PokemonServiceLocator.
 */
interface PokemonApiService {

    /** Cari kartu berdasarkan nama, mis. search=Charizard. */
    @GET("cards")
    suspend fun searchCards(
        @Query("search") query: String,
        @Query("limit") limit: Int = 20,
    ): CardsResponseDto
}