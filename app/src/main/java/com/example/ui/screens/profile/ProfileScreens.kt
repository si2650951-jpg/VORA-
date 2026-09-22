package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
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
import com.example.ui.components.RoleBadge
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun ProfileScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Cover Photo Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentUser?.coverUrl?.ifEmpty { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800" })
                    .crossfade(true)
                    .build(),
                contentDescription = "Cover Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { onNavigate(Screen.EditProfile.route) },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(DarkNavySurface)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = TextPrimary)
            }
        }

        // Profile Details Card
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = (-40).dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                AvatarWithStatus(
                    imageUrl = currentUser?.photoUrl ?: "",
                    size = 80.dp,
                    isOnline = currentUser?.isOnline ?: true,
                    showCrown = currentUser?.role?.contains("ADMIN") == true
                )

                Button(
                    onClick = { onNavigate(Screen.UserControl.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Controls", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = currentUser?.name ?: "Member Name", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = "@${currentUser?.username ?: "user"} • ID: ${currentUser?.id ?: "VORA-000"}", color = VoraCyan, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(8.dp))
            RoleBadge(role = currentUser?.role ?: "USER")

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = currentUser?.bio ?: "No bio set yet.", color = TextSecondary, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // Followers / Following / Coins Cards
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${currentUser?.followersCount ?: 0}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text("Followers", fontSize = 12.sp, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${currentUser?.followingCount ?: 0}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text("Following", fontSize = 12.sp, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🪙 ${currentUser?.coins ?: 0}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VoraGold)
                        Text("Coins", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun EditProfileScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var photoUrl by remember { mutableStateOf(currentUser?.photoUrl ?: "") }
    var coverUrl by remember { mutableStateOf(currentUser?.coverUrl ?: "") }
    var bio by remember { mutableStateOf(currentUser?.bio ?: "") }
    var country by remember { mutableStateOf(currentUser?.country ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavyBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Edit Profile Details", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Update avatar, cover image & personal bio", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Display Name") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = photoUrl, onValueChange = { photoUrl = it },
                label = { Text("Profile Photo URL") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = coverUrl, onValueChange = { coverUrl = it },
                label = { Text("Cover Banner Photo URL") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = country, onValueChange = { country = it },
                label = { Text("Country") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = bio, onValueChange = { bio = it },
                label = { Text("Bio / Status") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.updateUserProfile(name, photoUrl, coverUrl, bio, country) {
                        onNavigate(Screen.Profile.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Text("Save Profile Changes", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
