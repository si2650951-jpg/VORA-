package com.example.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AvatarWithStatus
import com.example.ui.components.GlassCard
import com.example.ui.components.RoleBadge
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun DiscoverScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredUsers = allUsers.filter { user ->
        (searchQuery.isEmpty() || user.name.contains(searchQuery, ignoreCase = true) || user.username.contains(searchQuery, ignoreCase = true)) &&
                when (selectedFilter) {
                    "ONLINE" -> user.isOnline
                    "VERIFIED" -> user.isVerified
                    "ADMINS" -> user.role.contains("ADMIN")
                    else -> true
                }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        Text("Discover Community", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Find popular hosts, trending users and online friends", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search users by name, @username...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VoraPurpleLight) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", "ONLINE", "VERIFIED", "ADMINS").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filteredUsers) { user ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigate(Screen.Profile.route) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarWithStatus(
                                imageUrl = user.photoUrl,
                                size = 48.dp,
                                isOnline = user.isOnline,
                                showCrown = user.role.contains("ADMIN")
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = user.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                                Text(text = "@${user.username} • ${user.country}", color = TextSecondary, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                RoleBadge(role = user.role)
                            }
                        }

                        Row {
                            IconButton(onClick = { viewModel.startVoiceCall(user); onNavigate(Screen.VoiceCall.route) }) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = VoraPurpleLight)
                            }
                            IconButton(onClick = { viewModel.startVideoCall(user); onNavigate(Screen.VideoCall.route) }) {
                                Icon(Icons.Default.Videocam, contentDescription = "Video", tint = VoraCyan)
                            }
                        }
                    }
                }
            }
        }
    }
}
