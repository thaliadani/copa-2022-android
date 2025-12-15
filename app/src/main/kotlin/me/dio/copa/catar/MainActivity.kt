package me.dio.copa.catar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.ui.MainScreen
import me.dio.copa.catar.ui.theme.Copa2022Theme
import me.dio.copa.catar.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Copa2022Theme {
                val uiState by viewModel.uiState.collectAsState()
                val searchText by viewModel.searchText.collectAsState()

                MainScreen(
                    uiState = uiState,
                    searchText = searchText,
                    onSearchTextChange = viewModel::onSearchTextChange,
                    enableNotification = viewModel::enableNotification,
                    disableNotification = viewModel::disableNotification
                )
            }
        }
    }
}
