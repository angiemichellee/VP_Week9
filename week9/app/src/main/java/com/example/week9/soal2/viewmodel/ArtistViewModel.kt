package com.example.week9.soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.week9.core.toUserMessage
import com.example.week9.soal2.model.ArtistPage
import com.example.week9.soal2.repository.MusicRepository
import com.example.week9.soal2.service.AudioDbServiceLocator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ArtistState {
    data object Loading : ArtistState
    data class Success(val page: ArtistPage) : ArtistState
    data class Error(val message: String) : ArtistState
}

data class ArtistUiState(
    val query: String = "Coldplay",
    val content: ArtistState = ArtistState.Loading,
)

class ArtistViewModel(private val repository: MusicRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private var job: Job? = null

    init {
        search()
    }

    fun onQueryChange(value: String) = _uiState.update { it.copy(query = value) }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return
        job?.cancel()
        _uiState.update { it.copy(content = ArtistState.Loading) }
        job = viewModelScope.launch {
            repository.getArtistPage(query)
                .onSuccess { page -> _uiState.update { it.copy(content = ArtistState.Success(page)) } }
                .onFailure { e -> _uiState.update { it.copy(content = ArtistState.Error(e.toUserMessage())) } }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ArtistViewModel(AudioDbServiceLocator.repository) }
        }
    }
}