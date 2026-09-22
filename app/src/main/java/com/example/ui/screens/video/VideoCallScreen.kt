package com.example.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.components.AvatarWithStatus
import com.example.ui.components.GlassCard
import com.example.ui.navigation.Screen
import com.example.ui.screens.voice.GiftSendBottomSheet
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun VideoCallScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val activeCall by viewModel.activeCall.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allGifts by viewModel.allGifts.collectAsStateWithLifecycle()

    var showGiftSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
    ) {
        // Video Stream Canvas / Placeholder
        if (activeCall?.isCameraOn == true) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(activeCall?.otherUser?.coverUrl?.ifEmpty { "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?w=800" })
                    .crossfade(true)
                    .build(),
                contentDescription = "Video Feed",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AvatarWithStatus(
                    imageUrl = activeCall?.otherUser?.photoUrl ?: "",
                    size = 120.dp,
                    isOnline = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = activeCall?.otherUser?.name ?: "User Video Muted",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(text = "📹 Camera Disabled", color = TextSecondary, fontSize = 13.sp)
            }
        }

        // Small Self PIP Video Corner
        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(width = 100.dp, height = 150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkNavySurface)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentUser?.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Self Camera",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Top Header Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.endCall(); onNavigate(Screen.Home.route) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column {
                Text(
                    text = activeCall?.otherUser?.name ?: "HD Video Call",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(text = "🔴 01:15 • 1080p HD", color = VoraCyan, fontSize = 12.sp)
            }
        }

        // Bottom Controls Overlay Bar
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.toggleCallMic() }) {
                    Icon(
                        imageVector = if (activeCall?.isMicMuted == true) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = if (activeCall?.isMicMuted == true) VoraRose else Color.White
                    )
                }

                IconButton(onClick = { viewModel.toggleCallCamera() }) {
                    Icon(
                        imageVector = if (activeCall?.isCameraOn == true) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Camera",
                        tint = if (activeCall?.isCameraOn == true) VoraCyan else VoraRose
                    )
                }

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(VoraRose)
                        .clickable { viewModel.endCall(); onNavigate(Screen.Home.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
                }

                IconButton(onClick = { showGiftSheet = true }) {
                    Text("🎁", fontSize = 22.sp)
                }

                IconButton(onClick = { viewModel.toggleCallSpeaker() }) {
                    Icon(Icons.Default.SwitchCamera, contentDescription = "Switch Camera", tint = Color.White)
                }
            }
        }
    }

    if (showGiftSheet && activeCall?.otherUser != null) {
        GiftSendBottomSheet(
            gifts = allGifts,
            userCoins = currentUser?.coins ?: 0,
            onDismiss = { showGiftSheet = false },
            onSendGift = { gift ->
                viewModel.sendGiftToUser(activeCall!!.otherUser, gift)
                showGiftSheet = false
            }
        )
    }
}
