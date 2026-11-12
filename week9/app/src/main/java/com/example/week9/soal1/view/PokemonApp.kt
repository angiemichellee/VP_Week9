package com.example.week9.soal1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week9.soal1.model.PokemonCard
import com.example.week9.soal1.viewmodel.CardsState
import com.example.week9.soal1.viewmodel.PokemonUiState
import com.example.week9.soal1.viewmodel.PokemonViewModel

@Composable
fun PokemonApp() {
    PokemonTheme {
        PokemonScreen()
    }
}

@Composable
fun PokemonScreen(viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory)) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PokemonContent(
        state = state,
        quickSearches = viewModel.quickSearches,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onQuickSearch = viewModel::onQuickSearch,
        onCardClick = viewModel::selectCard,
    )

    state.selectedCard?.let { card ->
        PokemonDetailSheet(card = card, onDismiss = { viewModel.selectCard(null) })
    }
}

@Composable
fun PokemonContent(
    state: PokemonUiState,
    quickSearches: List<String>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onQuickSearch: (String) -> Unit,
    onCardClick: (PokemonCard) -> Unit,
) {
    Scaffold(containerColor = PokeColors.Background) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            PokeHeader()
            SearchField(query = state.query, onQueryChange = onQueryChange, onSearch = onSearch)
            QuickSearchRow(items = quickSearches, selected = state.query, onClick = onQuickSearch)

            Box(Modifier.fillMaxSize()) {
                when (val cards = state.cards) {
                    CardsState.Loading -> LoadingState(Modifier.align(Alignment.Center))
                    is CardsState.Error -> PokeErrorState(
                        message = cards.message,
                        onRetry = onSearch,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    is CardsState.Success -> CardGrid(
                        cards = cards.cards,
                        bottomPadding = innerPadding.calculateBottomPadding(),
                        onCardClick = onCardClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun PokeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PokeColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(40.dp)
                .background(PokeColors.Red, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.CatchingPokemon, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("PokéTracker", color = PokeColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Real-time TCG Card Valuation", color = PokeColors.TextSecondary, fontSize = 12.sp)
        }
        Row(
            modifier = Modifier
                .background(PokeColors.Green.copy(alpha = 0.12f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(6.dp).background(PokeColors.Green, CircleShape))
            Spacer(Modifier.width(6.dp))
            Text("LIVE API", color = PokeColors.Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text("Cari kartu Pokémon...", color = PokeColors.TextSecondary) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = PokeColors.Red) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Hapus", tint = PokeColors.TextSecondary)
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch()
        }),
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PokeColors.Surface,
            unfocusedContainerColor = PokeColors.Surface,
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            focusedTextColor = PokeColors.TextPrimary,
            unfocusedTextColor = PokeColors.TextPrimary,
            cursorColor = PokeColors.Red,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .border(1.dp, PokeColors.Border, RoundedCornerShape(14.dp)),
    )
}

@Composable
private fun QuickSearchRow(items: List<String>, selected: String, onClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 12.dp),
    ) {
        items(items) { name ->
            val isSelected = name.equals(selected.trim(), ignoreCase = true)
            Text(
                text = name,
                color = if (isSelected) PokeColors.TextPrimary else PokeColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .background(
                        if (isSelected) PokeColors.Red.copy(alpha = 0.25f) else PokeColors.Surface,
                        RoundedCornerShape(50),
                    )
                    .border(1.dp, if (isSelected) PokeColors.Red else PokeColors.Border, RoundedCornerShape(50))
                    .clickableNoRipple { onClick(name) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
            )
        }
    }
}

@Composable
private fun CardGrid(
    cards: List<PokemonCard>,
    bottomPadding: androidx.compose.ui.unit.Dp,
    onCardClick: (PokemonCard) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = bottomPadding + 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Market Results (${cards.size})",
                    color = PokeColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                )
                Text("USD · Near Mint", color = PokeColors.TextSecondary, fontSize = 11.sp)
            }
        }
        items(cards, key = { it.id }) { card ->
            PokemonCardItem(card = card, onClick = { onCardClick(card) })
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = PokeColors.Red)
        Spacer(Modifier.size(12.dp))
        Text("Mengambil data kartu...", color = PokeColors.TextSecondary, fontSize = 13.sp)
    }
}