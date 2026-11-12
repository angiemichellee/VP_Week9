package com.example.week9.soal1.repository

import com.example.week9.core.AppException
import com.example.week9.core.safeCall
import com.example.week9.soal1.model.PokemonCard
import com.example.week9.soal1.service.CardDto
import com.example.week9.soal1.service.PokemonApiService
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

/** Repository = satu-satunya pintu ViewModel ke data (Service) + mapping DTO -> Model. */
class PokemonRepository(private val api: PokemonApiService) {

    suspend fun searchCards(query: String): Result<List<PokemonCard>> = safeCall {
        val cards = api.searchCards(query.trim()).data.mapIndexed { i, dto -> dto.toDomain(i) }
        if (cards.isEmpty()) throw AppException("Kartu \"$query\" tidak ditemukan.")
        cards
    }
}

/** Ambil isi JsonElement sebagai teks; array digabung dengan koma. */
private fun JsonElement?.text(): String = when (this) {
    null, JsonNull -> ""
    is JsonPrimitive -> content
    is JsonArray -> joinToString(", ") { it.text() }
    else -> ""
}

private fun CardDto.toDomain(index: Int) = PokemonCard(
    id = tcgPlayerId.text().ifEmpty { id.text() }.ifEmpty { "card-$index" },
    name = name ?: "Unknown Card",
    setName = setName.orEmpty(),
    cardNumber = cardNumber.text(),
    rarity = rarity.orEmpty(),
    type = cardType.text().ifEmpty { types.text() }.ifEmpty { energyType.text() },
    hp = hp.text(),
    artist = artist.orEmpty(),
    imageUrl = imageCdnUrl800 ?: imageCdnUrl400 ?: imageCdnUrl ?: imageUrl,
    marketPrice = prices?.market,
    lowPrice = prices?.low,
    activeListings = prices?.listings,
    sellers = prices?.sellers,
    printing = prices?.primaryPrinting.orEmpty(),
)