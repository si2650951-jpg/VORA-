package com.example.ui.screens.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.data.model.GiftEntity
import com.example.data.model.UserEntity
import com.example.ui.components.*
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun VoiceRoomsScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val allRooms by viewModel.allRooms.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

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
                Text("Voice Chat Rooms", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Join interactive live audio stages", fontSize = 12.sp, color = TextSecondary)
            }

            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Create Room", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(allRooms) { room ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.joinVoiceRoom(room.id)
                        onNavigate(Screen.VoiceRoomDetail.createRoute(room.id))
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VoiceWaveAnimation()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = room.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                        }
                        if (room.isPrivate) {
                            Icon(Icons.Default.Lock, contentDescription = "Private", tint = VoraGold, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Topic: ${room.topic}", color = TextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarWithStatus(imageUrl = room.hostAvatar, size = 32.dp, showCrown = true)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Host: ${room.hostName}", fontSize = 12.sp, color = TextPrimary)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VoraPurple.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "👥 ${room.listenersCount} Listening", color = VoraPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateRoomDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { title, topic, isPrivate, pass ->
                viewModel.createVoiceRoom(title, topic, isPrivate, pass) {
                    showCreateDialog = false
                    onNavigate(Screen.VoiceRooms.route)
                }
            }
        )
    }
}

@Composable
fun VoiceRoomDetailScreen(
    roomId: String,
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val activeRoom by viewModel.activeVoiceRoom.collectAsStateWithLifecycle()
    val participants by viewModel.activeRoomParticipants.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allGifts by viewModel.allGifts.collectAsStateWithLifecycle()

    var isMicMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var showGiftSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
    ) {
        // Room Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.leaveVoiceRoom(); onNavigate(Screen.VoiceRooms.route) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = activeRoom?.title ?: "Voice Room Stage", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "🎙️ Live Stage (${participants.size} participants)", fontSize = 11.sp, color = VoraCyan)
            }
            IconButton(onClick = { showGiftSheet = true }) {
                Text("🎁", fontSize = 22.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Voice Waves Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = VoraPurpleDark.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VoiceWaveAnimation(waveColor = VoraCyan)
                Text(text = "AUDIO BROADCAST ACTIVE", color = VoraCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                VoiceWaveAnimation(waveColor = VoraCyan)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Mic Seats Stage (8 Slots)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        // 8 Mic Seats Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(8) { index ->
                val participant = participants.getOrNull(index)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    if (participant != null) {
                        AvatarWithStatus(
                            imageUrl = participant.userAvatar,
                            size = 46.dp,
                            showCrown = participant.role == "HOST"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = participant.userName.split(" ").firstOrNull() ?: "",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = if (participant.isMuted) "🔇 Muted" else "🎤 Speaking",
                            fontSize = 9.sp,
                            color = if (participant.isMuted) TextMuted else VoraEmerald
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(DarkNavySurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Take Seat", tint = TextMuted)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Seat #${index + 1}", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }
        }

        // Room Controls Bar
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isMicMuted = !isMicMuted }) {
                    Icon(
                        imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (isMicMuted) VoraRose else VoraEmerald,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { isSpeakerOn = !isSpeakerOn }) {
                    Icon(
                        imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Speaker",
                        tint = VoraCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { showGiftSheet = true }) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(VoraGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎁", fontSize = 20.sp)
                    }
                }

                Button(
                    onClick = { viewModel.leaveVoiceRoom(); onNavigate(Screen.VoiceRooms.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = VoraRose)
                ) {
                    Text("Leave Stage", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    if (showGiftSheet) {
        GiftSendBottomSheet(
            gifts = allGifts,
            userCoins = currentUser?.coins ?: 0,
            onDismiss = { showGiftSheet = false },
            onSendGift = { gift ->
                val host = UserEntity(
                    id = activeRoom?.hostUserId ?: "VORA-1002",
                    name = activeRoom?.hostName ?: "Host",
                    username = "host", email = "host@vora.com", mobile = "", password = "", photoUrl = "", coverUrl = "", dob = "", country = "", gender = "", bio = ""
                )
                viewModel.sendGiftToUser(host, gift, activeRoom?.id ?: "")
                showGiftSheet = false
            }
        )
    }
}

@Composable
fun VoiceCallScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val activeCall by viewModel.activeCall.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("1-on-1 Voice Call", color = TextSecondary, fontSize = 14.sp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AvatarWithStatus(
                imageUrl = activeCall?.otherUser?.photoUrl ?: "",
                size = 110.dp,
                isOnline = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = activeCall?.otherUser?.name ?: "Calling User...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            VoiceWaveAnimation(waveColor = VoraPurpleLight)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Connected • 00:42", color = VoraCyan, fontSize = 13.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.toggleCallMic() }) {
                Icon(
                    imageVector = if (activeCall?.isMicMuted == true) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Mic",
                    tint = if (activeCall?.isMicMuted == true) VoraRose else VoraEmerald,
                    modifier = Modifier.size(36.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(VoraRose)
                    .clickable { viewModel.endCall(); onNavigate(Screen.Home.route) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CallEnd, contentDescription = "End", tint = Color.White, modifier = Modifier.size(32.dp))
            }

            IconButton(onClick = { viewModel.toggleCallSpeaker() }) {
                Icon(
                    imageVector = if (activeCall?.isSpeakerOn == true) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = "Speaker",
                    tint = VoraCyan,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
private fun CreateRoomDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, topic: String, isPrivate: Boolean, pass: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("Music & Lounge") }
    var isPrivate by remember { mutableStateOf(false) }
    var passcode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkNavySurface,
        title = { Text("Create Voice Room", color = TextPrimary) },
        text = {
            Column {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Room Title") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = topic, onValueChange = { topic = it },
                    label = { Text("Topic / Category") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Private Room with Lock", color = TextSecondary)
                    Switch(checked = isPrivate, onCheckedChange = { isPrivate = it })
                }
                if (isPrivate) {
                    OutlinedTextField(
                        value = passcode, onValueChange = { passcode = it },
                        label = { Text("Room Passcode") }, modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotEmpty()) onCreate(title, topic, isPrivate, passcode) },
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Text("Start Stage", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftSendBottomSheet(
    gifts: List<GiftEntity>,
    userCoins: Int,
    onDismiss: () -> Unit,
    onSendGift: (GiftEntity) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkNavySurface,
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Send Virtual Gift 🎁", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Your Balance: 🪙 $userCoins", color = VoraGold, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(gifts) { gift ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSendGift(gift) }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(gift.icon, fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(gift.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("🪙 ${gift.coinPrice}", fontSize = 11.sp, color = VoraGold)
                        }
                    }
                }
            }
        }
    }
}
