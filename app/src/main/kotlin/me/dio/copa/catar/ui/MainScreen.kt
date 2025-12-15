package me.dio.copa.catar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.dio.copa.catar.R
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.model.Team
import me.dio.copa.catar.ui.theme.Black
import me.dio.copa.catar.ui.theme.Copa2022Theme
import me.dio.copa.catar.ui.theme.Gold
import me.dio.copa.catar.ui.viewmodel.MainUiState
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    enableNotification: (String) -> Unit,
    disableNotification: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Copa 2022") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Gold,
                    titleContentColor = Black
                )
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            TextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Buscar") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Gold,
                    unfocusedIndicatorColor = Gold,
                    focusedLabelColor = Black,
                    cursorColor = Black,
                )
            )

            when (uiState) {
                MainUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Loading...")
                    }
                }

                is MainUiState.Success -> {
                    LazyColumn {
                        items(uiState.matches) { match ->
                            MatchCard(match = match, enableNotification, disableNotification)
                        }
                    }
                }

                is MainUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Error!")
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCard(
    match: Match,
    enableNotification: (String) -> Unit,
    disableNotification: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            Text(text = match.date.format(formatter), color = Black)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Team(team = match.team1)
                Text(text = "x", modifier = Modifier.padding(horizontal = 16.dp),color = Black)
                Team(team = match.team2)
            }

            IconButton(onClick = {
                if (match.notificationEnabled) {
                    disableNotification(match.id)
                } else {
                    enableNotification(match.id)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (match.notificationEnabled) Gold else Color.Black
                )
            }
        }
    }
}

@Composable
fun Team(team: Team) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = getTeamFlag(team = team)),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = team.displayName,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Black
        )
    }
}

@Composable
fun getTeamFlag(team: Team): Int {
    return when (team.id) {
        "BR" -> R.drawable.br
        "FR" -> R.drawable.fr
        "AR" -> R.drawable.ar
        "RS" -> R.drawable.rs
        "CH" -> R.drawable.ch
        "CM" -> R.drawable.cm
        else -> R.drawable.br
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    Copa2022Theme {
        MainScreen(uiState = MainUiState.Loading, "", {}, {}, {})
    }
}
