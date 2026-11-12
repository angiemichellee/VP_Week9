package com.example.week9.soal1.model

/** Model domain yang dipakai UI (sudah bersih dari bentuk JSON server). */
data class PokemonCard(
    val id: String,
    val name: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String,
    val type: String,
    val hp: String,
    val artist: String,
    val imageUrl: String?,
    val marketPrice: Double?,
    val lowPrice: Double?,
    val activeListings: Int?,
    val sellers: Int?,
    val printing: String,
)