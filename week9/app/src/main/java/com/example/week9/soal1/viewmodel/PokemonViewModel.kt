package com.example.week9.soal1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.week9.core.toUserMessage
import com.example.week9.soal1.model.PokemonCard
import com.example.week9.soal1.repository.PokemonRepository
import com.example.week9.soal1.service.PokemonServiceLocator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CardsState {
    data object Loading : CardsState
    data class Success(val cards: List<PokemonCard>) : CardsState
    data class Error(val message: String) : CardsState
}

data class PokemonUiState(
    val query: String = "Charizard",
    val cards: CardsState = CardsState.Loading,
    val selectedCard: PokemonCard? = null,
)

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    val quickSearches = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar", "Umbreon", "Lugia")

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        search()
    }

    fun onQueryChange(value: String) = _uiState.update { it.copy(query = value) }

    fun onQuickSearch(name: String) {
        _uiState.update { it.copy(query = name) }
        search()
    }

    /** Request hanya dikirim saat tombol search / chip ditekan (hemat kuota 100 request/hari). */
    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return
        searchJob?.cancel()
        _uiState.update { it.copy(cards = CardsState.Loading) }
        searchJob = viewModelScope.launch {
            repository.searchCards(query)
                .onSuccess { list -> _uiState.update { it.copy(cards = CardsState.Success(list)) } }
                .onFailure { e -> _uiState.update { it.copy(cards = CardsState.Error(e.toUserMessage())) } }
        }
    }

    fun selectCard(card: PokemonCard?) = _uiState.update { it.copy(selectedCard = card) }

    companion object {
        val Factory = viewModelFactory {
            initializer { PokemonViewModel(PokemonServiceLocator.repository) }
        }
    }
}