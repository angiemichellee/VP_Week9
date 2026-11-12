package com.example.week9.core

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

/**
 * Pengganti runCatching untuk coroutine: CancellationException WAJIB dilempar ulang,
 * kalau tidak, pembatalan coroutine (mis. ViewModel dibuang) ikut "tertelan".
 */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

/** Exception yang sengaja kita lempar sendiri (mis. data kosong) dengan pesan yang ramah. */
class AppException(message: String) : Exception(message)

/** Ubah exception teknis menjadi pesan yang bisa dibaca pengguna. */
fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> when (code()) {
        401, 403 -> "HTTP ${code()} · API key tidak valid"
        404 -> "HTTP 404 · Data tidak ditemukan"
        429 -> "HTTP 429 · Batas request harian habis"
        else -> "HTTP ${code()}"
    }
    is AppException -> message ?: "Terjadi kesalahan."
    is IOException -> message ?: "Tidak ada koneksi internet."
    is kotlinx.serialization.SerializationException -> "Format data dari server tidak sesuai."
    else -> message ?: "Terjadi kesalahan."
}