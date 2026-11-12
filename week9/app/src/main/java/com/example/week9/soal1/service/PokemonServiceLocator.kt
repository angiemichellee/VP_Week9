package com.example.week9.soal1.service

import com.example.week9.soal1.repository.PokemonRepository
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

const val POKEMON_API_KEY = "pokeprice_free_867a6869576191693e256e9c74b34644895ab96147549c28"

/** Membuat objek singleton (Retrofit, Repository) secara manual. */
object PokemonServiceLocator {

    private const val BASE_URL = "https://www.pokemonpricetracker.com/api/v2/"

    private val json = Json {
        ignoreUnknownKeys = true   // abaikan field yang tidak kita pakai
        isLenient = true           // angka boleh dibaca sebagai String, dst.
        coerceInputValues = true   // null untuk field non-null -> pakai default
        explicitNulls = false
    }

    private val authInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $POKEMON_API_KEY")
                .build()
        )
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val api: PokemonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokemonApiService::class.java)
    }

    val repository: PokemonRepository by lazy { PokemonRepository(api) }
}