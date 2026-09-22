package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
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
fun HomeScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allRooms by viewModel.allRooms.collectAsStateWithLifecycle()
    val onlineUsers by viewModel.onlineUsers.collectAsStateWithLifecycle()
    val allPhotos by viewModel.allPhotos.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Welcome Banner Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = VoraPurpleDark.copy(alpha = 0.4f),
            borderColor = VoraPurpleLight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hello, ${currentUser?.name ?: "Member"} 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ID: ${currentUser?.id ?: "VORA-1001"}",
                        fontSize = 12.sp,
                        color = VoraCyan
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkNavySurface)
                        .clickable { onNavigate(Screen.Wallet.route) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "🪙", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${currentUser?.coins ?: 0}",
                        fontWeight = FontWeight.Bold,
                        color = VoraGold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickActionItem("Create Room", Icons.Default.AddCircle, VoraPurple) { onNavigate(Screen.VoiceRooms.route) }
            QuickActionItem("Discover", Icons.Default.Explore, VoraCyan) { onNavigate(Screen.Discover.route) }
            QuickActionItem("Wallet", Icons.Default.AccountBalanceWallet, VoraGold) { onNavigate(Screen.Wallet.route) }
            QuickActionItem("Gifts", Icons.Default.CardGiftcard, VoraRose) { onNavigate(Screen.Gifts.route) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Live Voice Rooms Carousel
        SectionHeader("Live Voice Rooms", "View All (${allRooms.size})") {
            onNavigate(Screen.VoiceRooms.route)
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(allRooms) { room ->
                GlassCard(
                    modifier = Modifier.width(240.dp),
                    onClick = { viewModel.joinVoiceRoom(room.id); onNavigate(Screen.VoiceRoomDetail.createRoute(room.id)) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VoiceWaveAnimation()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(VoraRose.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("LIVE", color = VoraRose, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = room.title,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        maxLines = 1
                    )
                    Text(
                        text = room.topic,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarWithStatus(imageUrl = room.hostAvatar, size = 28.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = room.hostName, fontSize = 12.sp, color = TextPrimary)
                        }
                        Text(text = "👥 ${room.listenersCount}", fontSize = 11.sp, color = VoraCyan)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Online Community Members
        SectionHeader("Online Now", "Discover") {
            onNavigate(Screen.Discover.route)
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(onlineUsers) { user ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigate(Screen.Profile.route) }
                ) {
                    AvatarWithStatus(
                        imageUrl = user.photoUrl,
                        size = 56.dp,
                        isOnline = user.isOnline,
                        showCrown = user.role.contains("ADMIN")
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = user.name.split(" ").firstOrNull() ?: user.name,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Community Photos Preview
        SectionHeader("Community Photos", "Upload Photo") {
            onNavigate(Screen.Photos.route)
        }

        allPhotos.take(2).forEach { photo ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                onClick = { onNavigate(Screen.Photos.route) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarWithStatus(imageUrl = photo.userAvatar, size = 36.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = photo.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text(text = photo.caption, fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
