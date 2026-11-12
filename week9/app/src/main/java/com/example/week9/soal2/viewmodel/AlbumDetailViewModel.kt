package com.example.week9.soal2.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.week9.core.toUserMessage
import com.example.week9.soal2.model.AlbumDetail
import com.example.week9.soal2.repository.MusicRepository
import com.example.week9.soal2.service.AudioDbServiceLocator
import com.example.week9.soal2.view.AlbumRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Success(val detail: AlbumDetail) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

class AlbumDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MusicRepository,
) : ViewModel() {

    private val albumId: String = savedStateHandle.toRoute<AlbumRoute>().albumId

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = AlbumDetailUiState.Loading
        viewModelScope.launch {
            repository.getAlbumDetail(albumId)
                .onSuccess { _uiState.value = AlbumDetailUiState.Success(it) }
                .onFailure { _uiState.value = AlbumDetailUiState.Error(it.toUserMessage()) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { AlbumDetailViewModel(createSavedStateHandle(), AudioDbServiceLocator.repository) }
        }
    }
}