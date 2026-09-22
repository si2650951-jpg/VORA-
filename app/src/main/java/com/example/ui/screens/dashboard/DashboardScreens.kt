package com.example.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun UserDashboardScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val callLogs by viewModel.callLogs.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val photos by viewModel.allPhotos.collectAsStateWithLifecycle()

    val userPhotosCount = photos.count { it.userId == currentUser?.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Banner / Header
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarWithStatus(
                    imageUrl = currentUser?.photoUrl ?: "",
                    size = 64.dp,
                    isOnline = currentUser?.isOnline ?: true,
                    showCrown = currentUser?.role?.contains("ADMIN") == true
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentUser?.name ?: "User",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "@${currentUser?.username ?: "username"} • ID: ${currentUser?.id ?: "VORA-000"}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    RoleBadge(role = currentUser?.role ?: "USER")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Followers / Following / Coins Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DashboardMetric("Followers", "${currentUser?.followersCount ?: 0}")
                DashboardMetric("Following", "${currentUser?.followingCount ?: 0}")
                DashboardMetric("Coins", "🪙 ${currentUser?.coins ?: 0}")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Stats Grid
        Text("Account Overview & Activity", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                title = "Call History",
                value = "${callLogs.size}",
                icon = Icons.Default.PhoneCallback,
                accentColor = VoraCyan,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Photos",
                value = "$userPhotosCount",
                icon = Icons.Default.PhotoLibrary,
                accentColor = VoraPurpleLight,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                title = "Notifications",
                value = "${notifications.size}",
                icon = Icons.Default.Notifications,
                accentColor = VoraRose,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Gifts Balance",
                value = "🪙 ${currentUser?.coins ?: 0}",
                icon = Icons.Default.CardGiftcard,
                accentColor = VoraGold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Management Options
        Text("Account Management & Controls", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        ManagementItem("Edit Profile & Photos", "Update bio, avatar & cover photo", Icons.Default.Edit) {
            onNavigate(Screen.EditProfile.route)
        }
        ManagementItem("User Control & Privacy", "Calls, gifts & message permissions", Icons.Default.Security) {
            onNavigate(Screen.UserControl.route)
        }
        ManagementItem("Wallet & Top-up Coins", "View coin transactions & buy packages", Icons.Default.AccountBalanceWallet) {
            onNavigate(Screen.Wallet.route)
        }
        ManagementItem("Notifications Center", "View social activity alerts", Icons.Default.NotificationsActive) {
            onNavigate(Screen.Notifications.route)
        }
    }
}

@Composable
private fun DashboardMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun ManagementItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VoraPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = VoraPurpleLight)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Go", tint = TextSecondary)
        }
    }
}

@Composable
fun UserControlScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var allowCalls by remember { mutableStateOf(currentUser?.allowCallsFrom ?: "EVERYONE") }
    var allowMessages by remember { mutableStateOf(currentUser?.allowMessagesFrom ?: "EVERYONE") }
    var allowGifts by remember { mutableStateOf(currentUser?.allowGiftsFrom ?: "EVERYONE") }
    var isOnlineVisible by remember { mutableStateOf(currentUser?.isOnline ?: true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("User Controls & Privacy", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Manage permissions, calls, messages & blocklist", fontSize = 13.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("Online Status Visibility", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show active online indicator", color = TextSecondary, fontSize = 13.sp)
                Switch(
                    checked = isOnlineVisible,
                    onCheckedChange = {
                        isOnlineVisible = it
                        viewModel.showToast("Online visibility updated")
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = VoraPurple)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)

            Text("Who Can Call Me", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("EVERYONE", "FOLLOWERS", "NOBODY").forEach { opt ->
                    FilterChip(
                        selected = allowCalls == opt,
                        onClick = { allowCalls = opt; viewModel.showToast("Call permissions set to $opt") },
                        label = { Text(opt, fontSize = 11.sp) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)

            Text("Who Can Message Me", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("EVERYONE", "FOLLOWERS", "NOBODY").forEach { opt ->
                    FilterChip(
                        selected = allowMessages == opt,
                        onClick = { allowMessages = opt; viewModel.showToast("Message permissions set to $opt") },
                        label = { Text(opt, fontSize = 11.sp) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)

            Text("Who Can Send Gifts", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("EVERYONE", "FOLLOWERS", "NOBODY").forEach { opt ->
                    FilterChip(
                        selected = allowGifts == opt,
                        onClick = { allowGifts = opt; viewModel.showToast("Gift permissions set to $opt") },
                        label = { Text(opt, fontSize = 11.sp) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Danger Zone / Account Deletion
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = VoraRose.copy(alpha = 0.5f)
        ) {
            Text("Danger Zone", fontWeight = FontWeight.Bold, color = VoraRose, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Permanently delete account and erase all associated room data, photos, and coin wallet.", fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = { viewModel.showToast("Account deletion requested") },
                colors = ButtonDefaults.buttonColors(containerColor = VoraRose),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete My Account", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
