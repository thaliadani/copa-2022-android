package me.dio.copa.catar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.dio.copa.catar.domain.usecase.DisableNotificationUseCase
import me.dio.copa.catar.domain.usecase.EnableNotificationUseCase
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val enableNotificationUseCase: EnableNotificationUseCase,
    private val disableNotificationUseCase: DisableNotificationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    init {
        fetchMatches()
    }

    private fun fetchMatches() {
        viewModelScope.launch {
            getMatchesUseCase()
                .catch { _uiState.value = MainUiState.Error(it) }
                .combine(_searchText) { matches, text ->
                    if (text.isBlank()) {
                        matches
                    } else {
                        matches.filter {
                            it.team1.displayName.contains(text, ignoreCase = true) ||
                                    it.team2.displayName.contains(text, ignoreCase = true)
                        }
                    }
                }
                .collect { _uiState.value = MainUiState.Success(it) }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun enableNotification(id: String) {
        viewModelScope.launch {
            enableNotificationUseCase(id)
        }
    }

    fun disableNotification(id: String) {
        viewModelScope.launch {
            disableNotificationUseCase(id)
        }
    }
}