package me.dio.copa.catar.ui.viewmodel

import me.dio.copa.catar.domain.model.Match

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val matches: List<Match>) : MainUiState()
    data class Error(val throwable: Throwable) : MainUiState()
}