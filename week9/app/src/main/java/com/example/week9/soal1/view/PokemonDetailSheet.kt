package com.example.week9.soal1.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week9.soal1.model.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailSheet(card: PokemonCard, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PokeColors.Background,
        dragHandle = null,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF5A2A2A), PokeColors.Background),
                        )
                    )
                    .padding(top = 24.dp, bottom = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(bottom = 8.dp)
                        .size(width = 36.dp, height = 4.dp)
                        .background(PokeColors.TextSecondary.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.35f), CircleShape),
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Tutup", tint = Color.White, modifier = Modifier.size(18.dp))
                }
                CardImage(
                    url = card.imageUrl,
                    contentDescription = card.name,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .width(190.dp)
                        .height(265.dp),
                )
            }

            Text(
                card.name,
                color = PokeColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
            Text(
                card.setName,
                color = PokeColors.TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PriceBox("Market Price", card.marketPrice.toUsd(), PokeColors.Green, Icons.AutoMirrored.Filled.TrendingUp, Modifier.weight(1f))
                PriceBox("Lowest Price", card.lowPrice.toUsd(), PokeColors.Yellow, Icons.Filled.Sell, Modifier.weight(1f))
            }

            SectionTitle("Market Activity")
            InfoTable(
                listOf(
                    "Active Listings" to (card.activeListings?.toString() ?: "-"),
                    "Verified Sellers" to (card.sellers?.toString() ?: "-"),
                    "Primary Printing" to card.printing.ifEmpty { "-" },
                )
            )

            SectionTitle("Card Specifications")
            InfoTable(
                listOf(
                    "Card Number" to card.cardNumber.ifEmpty { "-" },
                    "Rarity" to card.rarity.ifEmpty { "-" },
                    "Type" to card.type.ifEmpty { "-" },
                    "HP" to card.hp.ifEmpty { "-" },
                    "Illustrator" to card.artist.ifEmpty { "-" },
                )
            )

            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PokeColors.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PriceBox(label: String, value: String, color: Color, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PokeColors.Surface),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(24.dp)
                        .background(color.copy(alpha = 0.15f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(label, color = PokeColors.TextSecondary, fontSize = 11.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        color = PokeColors.TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
    )
}

@Composable
private fun InfoTable(rows: List<Pair<String, String>>) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(PokeColors.Surface, RoundedCornerShape(12.dp))
    ) {
        rows.forEachIndexed { index, (label, value) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 11.dp),
            ) {
                Text(label, color = PokeColors.TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text(value, color = PokeColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            if (index < rows.lastIndex) HorizontalDivider(color = PokeColors.Border.copy(alpha = 0.5f))
        }
    }
}