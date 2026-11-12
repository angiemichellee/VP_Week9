package com.example.week9.soal1.service

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * DTO = bentuk data persis seperti JSON dari PokemonPriceTracker API v2.
 * Semua field dibuat nullable + default supaya aman kalau server tidak mengirim field tertentu.
 * Field yang bentuknya bisa berbeda-beda (string / angka / array) memakai JsonElement.
 */
@Serializable
data class CardsResponseDto(
    val data: List<CardDto> = emptyList(),
    val metadata: MetadataDto? = null,
)

@Serializable
data class MetadataDto(
    val total: Int? = null,
    val count: Int? = null,
    val hasMore: Boolean? = null,
)

@Serializable
data class CardDto(
    val id: JsonElement? = null,
    val tcgPlayerId: JsonElement? = null,
    val name: String? = null,
    val setName: String? = null,
    val cardNumber: JsonElement? = null,
    val rarity: String? = null,
    val cardType: JsonElement? = null,
    val types: JsonElement? = null,
    val energyType: JsonElement? = null,
    val hp: JsonElement? = null,
    val artist: String? = null,
    val imageCdnUrl: String? = null,
    val imageCdnUrl400: String? = null,
    val imageCdnUrl800: String? = null,
    val imageUrl: String? = null,
    val prices: PricesDto? = null,
)

@Serializable
data class PricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val listings: Int? = null,
    val sellers: Int? = null,
    val primaryPrinting: String? = null,
)