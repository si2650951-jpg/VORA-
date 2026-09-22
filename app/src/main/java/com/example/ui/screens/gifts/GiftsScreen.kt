package com.example.ui.screens.gifts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GlassCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun GiftsScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val allGifts by viewModel.allGifts.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Virtual Gift Catalog", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Send animated gifts to hosts in live rooms & calls", fontSize = 12.sp, color = TextSecondary)
            }

            Button(
                onClick = { onNavigate(Screen.Wallet.route) },
                colors = ButtonDefaults.buttonColors(containerColor = VoraGold)
            ) {
                Text("🪙 ${currentUser?.coins ?: 0}", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allGifts) { gift ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = VoraPurple.copy(alpha = 0.4f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = gift.icon, fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = gift.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                        Text(text = "🪙 ${gift.coinPrice} Coins", color = VoraGold, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.showToast("Select a room or user in Chat / Call to send ${gift.name}")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
                        ) {
                            Text("Send Gift", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
