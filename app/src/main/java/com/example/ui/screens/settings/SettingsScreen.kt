package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun SettingsScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Application Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Preferences, security & system info", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            SettingRow("Edit Profile", "Name, photos, bio", Icons.Default.Person) { onNavigate(Screen.EditProfile.route) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)
            SettingRow("User Control & Privacy", "Permissions & blocking", Icons.Default.Security) { onNavigate(Screen.UserControl.route) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)
            SettingRow("Wallet & Coin Balance", "Buy coins & view history", Icons.Default.AccountBalanceWallet) { onNavigate(Screen.Wallet.route) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)
            SettingRow("About VORA Voice", "App version, mission & terms", Icons.Default.Info) { onNavigate(Screen.About.route) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)
            SettingRow("Help & Support", "FAQ, contact us & reports", Icons.Default.Help) { onNavigate(Screen.HelpSupport.route) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Box
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = VoraRose.copy(alpha = 0.5f),
            onClick = { viewModel.logout { onNavigate(Screen.SignIn.route) } }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = VoraRose)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Log Out of VORA Voice", fontWeight = FontWeight.Bold, color = VoraRose, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = VoraPurpleLight, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
            Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
    }
}
