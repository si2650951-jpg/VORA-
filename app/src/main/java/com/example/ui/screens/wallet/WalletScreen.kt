package com.example.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun WalletScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val transactions by viewModel.userWalletTxns.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Wallet & Coin Balance", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Purchase coins to send gifts and support room hosts", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        // Balance Gold Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = VoraGold.copy(alpha = 0.2f),
            borderColor = VoraGold
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Coin Balance", fontSize = 13.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🪙 ${currentUser?.coins ?: 0}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = VoraGold
                    )
                }
                Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = VoraGold, modifier = Modifier.size(54.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Coin Packages", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CoinPackageCard("100 Coins", "$0.99", 100, viewModel, Modifier.weight(1f))
            CoinPackageCard("500 Coins", "$4.99", 500, viewModel, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CoinPackageCard("1,200 Coins", "$9.99", 1200, viewModel, Modifier.weight(1f))
            CoinPackageCard("5,000 Coins", "$39.99", 5000, viewModel, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Transaction History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Icon(Icons.Default.History, contentDescription = "History", tint = TextSecondary)
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (transactions.isEmpty()) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("No coin transactions yet.", color = TextSecondary, fontSize = 13.sp)
            }
        } else {
            transactions.forEach { txn ->
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = txn.description, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                            Text(text = txn.transactionType, color = TextSecondary, fontSize = 11.sp)
                        }
                        Text(
                            text = if (txn.coinsAmount > 0) "+${txn.coinsAmount}" else "${txn.coinsAmount}",
                            fontWeight = FontWeight.Bold,
                            color = if (txn.coinsAmount > 0) VoraEmerald else VoraRose,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinPackageCard(
    title: String,
    price: String,
    coinsAmount: Int,
    viewModel: VoraViewModel,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🪙 $title", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(price, color = VoraCyan, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.buyCoinPackage(coinsAmount, price) },
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Top-up", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
