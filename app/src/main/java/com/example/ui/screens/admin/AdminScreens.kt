package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
fun AdminDashboardScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val totalUsers by viewModel.totalUsersCount.collectAsStateWithLifecycle()
    val onlineUsers by viewModel.onlineUsersCount.collectAsStateWithLifecycle()
    val totalRooms by viewModel.totalRoomsCount.collectAsStateWithLifecycle()
    val totalCalls by viewModel.totalCallsCount.collectAsStateWithLifecycle()
    val totalGifts by viewModel.totalGiftsSentCount.collectAsStateWithLifecycle()
    val pendingReports by viewModel.pendingReportsCount.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("VORA Admin Control Center", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("System analytics & platform management", fontSize = 12.sp, color = VoraGold)
            }
            Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = VoraGold, modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // System Health Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = VoraPurpleDark.copy(alpha = 0.4f),
            borderColor = VoraGold
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(VoraEmerald)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("All Platform Services Operational (Room DB, Voice Server, Wallet API)", color = VoraEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Platform Metrics", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard("Total Users", "$totalUsers", Icons.Default.Group, VoraPurpleLight, Modifier.weight(1f))
            StatCard("Online Users", "$onlineUsers", Icons.Default.Circle, VoraEmerald, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard("Voice Rooms", "$totalRooms", Icons.Default.Mic, VoraCyan, Modifier.weight(1f))
            StatCard("Total Calls", "$totalCalls", Icons.Default.PhoneCallback, VoraGold, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Admin Management Modules", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(10.dp))

        AdminNavTile("User Management", "Ban, verify, assign roles & search users", Icons.Default.Group) { onNavigate(Screen.UserManagement.route) }
        AdminNavTile("Voice Rooms Control", "Monitor active audio stages & hosts", Icons.Default.Mic) { onNavigate(Screen.RoomManagement.route) }
        AdminNavTile("Virtual Gift Store Admin", "Configure gift pricing & catalog", Icons.Default.CardGiftcard) { onNavigate(Screen.GiftManagement.route) }
        AdminNavTile("Coin Wallet & Top-up Admin", "Manage coin packages & reward coins", Icons.Default.AccountBalanceWallet) { onNavigate(Screen.CoinManagement.route) }
        AdminNavTile("Photo Moderation", "Review community photos & remove flags", Icons.Default.PhotoLibrary) { onNavigate(Screen.PhotoManagement.route) }
        AdminNavTile("Reports & Complaints ($pendingReports)", "Review user violation reports", Icons.Default.Report) { onNavigate(Screen.Reports.route) }
        AdminNavTile("Room Database Inspector", "View SQLite tables & entity counts", Icons.Default.Storage) { onNavigate(Screen.DatabaseManagement.route) }
        AdminNavTile("Roles & Permissions Matrix", "Access control hierarchy (RBAC)", Icons.Default.Lock) { onNavigate(Screen.RolesPermissions.route) }
    }
}

@Composable
private fun AdminNavTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = VoraGold, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
        }
    }
}

@Composable
fun AdminUserManagementScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val users by viewModel.allUsers.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        Text("User Management", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Ban/Unban, verify checkmarks & assign roles", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(users) { user ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarWithStatus(imageUrl = user.photoUrl, size = 44.dp, showCrown = user.role.contains("ADMIN"))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = user.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                                    if (user.isVerified) {
                                        Text(" ✔️", color = VoraCyan, fontSize = 12.sp)
                                    }
                                }
                                Text(text = "@${user.username} (${user.id})", color = TextSecondary, fontSize = 11.sp)
                                RoleBadge(role = user.role)
                            }
                        }

                        Row {
                            IconButton(onClick = { viewModel.adminVerifyUser(user.id) }) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = "Verify", tint = if (user.isVerified) VoraCyan else TextMuted)
                            }
                            IconButton(onClick = { if (user.isBanned) viewModel.adminUnbanUser(user.id) else viewModel.adminBanUser(user.id) }) {
                                Icon(Icons.Default.Block, contentDescription = "Ban", tint = if (user.isBanned) VoraRose else TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRoomManagementScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val rooms by viewModel.allRooms.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        Text("Voice Rooms Control", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Active audio broadcasts monitoring", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(rooms) { room ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = room.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                            Text(text = "Host: ${room.hostName} • ID: ${room.id}", color = TextSecondary, fontSize = 12.sp)
                            Text(text = "Listeners: ${room.listenersCount}", color = VoraCyan, fontSize = 11.sp)
                        }

                        Button(
                            onClick = { viewModel.showToast("Room ${room.id} closed by Admin") },
                            colors = ButtonDefaults.buttonColors(containerColor = VoraRose)
                        ) {
                            Text("Force Close", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminReportsScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val reports by viewModel.allReports.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        Text("Reports & Complaints", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("User violation reports requiring moderation", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        if (reports.isEmpty()) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("No pending moderation reports.", color = TextSecondary, fontSize = 13.sp)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(reports) { rep ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Target: ${rep.reportedTargetName} (${rep.targetType})", fontWeight = FontWeight.Bold, color = VoraRose, fontSize = 14.sp)
                                Text(text = rep.status, color = VoraGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Reason: ${rep.reason}", color = TextPrimary, fontSize = 12.sp)
                            Text(text = "Reported by: ${rep.reporterName}", color = TextSecondary, fontSize = 11.sp)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.adminResolveReport(rep.id, "RESOLVED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = VoraEmerald),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Resolve", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { viewModel.adminBanUser(rep.reportedTargetId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = VoraRose),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Ban Target", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDatabaseScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val totalUsers by viewModel.totalUsersCount.collectAsStateWithLifecycle()
    val totalRooms by viewModel.totalRoomsCount.collectAsStateWithLifecycle()
    val totalCalls by viewModel.totalCallsCount.collectAsStateWithLifecycle()
    val totalMessages by viewModel.totalMessagesCount.collectAsStateWithLifecycle()
    val totalPhotos by viewModel.totalPhotosCount.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("SQLite Room Database Inspector", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Live record counts from AppDatabase", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            DbTableRow("Table: users", "$totalUsers rows")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GlassBorder)
            DbTableRow("Table: voice_rooms", "$totalRooms rows")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GlassBorder)
            DbTableRow("Table: call_history", "$totalCalls rows")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GlassBorder)
            DbTableRow("Table: messages", "$totalMessages rows")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GlassBorder)
            DbTableRow("Table: photos", "$totalPhotos rows")
        }
    }
}

@Composable
private fun DbTableRow(table: String, count: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = table, fontWeight = FontWeight.Bold, color = VoraCyan, fontSize = 14.sp)
        Text(text = count, color = TextPrimary, fontSize = 14.sp)
    }
}

@Composable
fun AdminRolesPermissionsScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val roles by viewModel.allRolePermissions.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Roles & Permissions Matrix", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Role-based access control rules (RBAC)", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        roles.forEach { role ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(text = "Role: ${role.role}", fontWeight = FontWeight.Bold, color = VoraGold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Permissions: ${role.permissions}", color = TextPrimary, fontSize = 12.sp)
            }
        }
    }
}
