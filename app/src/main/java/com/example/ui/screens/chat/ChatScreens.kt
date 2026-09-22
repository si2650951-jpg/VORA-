package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.MessageEntity
import com.example.ui.components.AvatarWithStatus
import com.example.ui.components.GlassCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun ChatListScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val chatUsers = allUsers.filter { it.id != currentUser?.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        Text("Messages & Chats", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Direct 1-on-1 messages with voice notes & photos", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(chatUsers) { user ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigate(Screen.ChatDetail.createRoute(user.id)) }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarWithStatus(
                            imageUrl = user.photoUrl,
                            size = 48.dp,
                            isOnline = user.isOnline,
                            showCrown = user.role.contains("ADMIN")
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                Text(text = "10:42 AM", fontSize = 10.sp, color = TextMuted)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Hey! Join my voice room stage later 🎙️",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatDetailScreen(
    userId: String,
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val targetUser = allUsers.find { it.id == userId }

    var textInput by remember { mutableStateOf("") }
    val messagesList = remember {
        mutableStateListOf(
            MessageEntity("1", "conv1", userId, currentUser?.id ?: "", "TEXT", "Hello! Welcome to VORA Voice."),
            MessageEntity("2", "conv1", currentUser?.id ?: "", userId, "TEXT", "Thanks! Super excited to listen to acoustic streams.")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
    ) {
        // Chat Header
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onNavigate(Screen.Chat.route) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    AvatarWithStatus(imageUrl = targetUser?.photoUrl ?: "", size = 40.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = targetUser?.name ?: "Chat User", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                        Text(text = if (targetUser?.isOnline == true) "🟢 Online Now" else "Offline", fontSize = 11.sp, color = VoraEmerald)
                    }
                }

                Row {
                    IconButton(onClick = { targetUser?.let { viewModel.startVoiceCall(it); onNavigate(Screen.VoiceCall.route) } }) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = VoraPurpleLight)
                    }
                    IconButton(onClick = { targetUser?.let { viewModel.startVideoCall(it); onNavigate(Screen.VideoCall.route) } }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = VoraCyan)
                    }
                }
            }
        }

        // Message Thread List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messagesList) { msg ->
                val isMe = msg.senderUserId == currentUser?.id
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp, topEnd = 16.dp,
                                    bottomStart = if (isMe) 16.dp else 4.dp,
                                    bottomEnd = if (isMe) 4.dp else 16.dp
                                )
                            )
                            .background(if (isMe) VoraPurple else DarkNavySurface)
                            .padding(12.dp)
                    ) {
                        Text(text = msg.text, color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }

        // Bottom Input Row
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    textInput += " 🌹"
                }) {
                    Text("😊", fontSize = 20.sp)
                }

                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Type a message...", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textInput.isNotEmpty()) {
                            messagesList.add(
                                MessageEntity(
                                    id = System.currentTimeMillis().toString(),
                                    conversationId = "conv1",
                                    senderUserId = currentUser?.id ?: "",
                                    receiverUserId = userId,
                                    messageType = "TEXT",
                                    text = textInput
                                )
                            )
                            textInput = ""
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(VoraPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
