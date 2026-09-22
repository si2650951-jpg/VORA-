package com.example.ui.screens.photos

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
import com.example.ui.theme.*
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun PhotosScreen(
    viewModel: VoraViewModel,
    onNavigate: (String) -> Unit
) {
    val photos by viewModel.allPhotos.collectAsStateWithLifecycle()
    var showUploadDialog by remember { mutableStateOf(false) }

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
                Text("Community Photo Feed", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Share & discover moments from voice room hosts", fontSize = 12.sp, color = TextSecondary)
            }

            Button(
                onClick = { showUploadDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(photos) { photo ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarWithStatus(imageUrl = photo.userAvatar, size = 42.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = photo.userName, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                                Text(text = "Posted recently", color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        IconButton(onClick = { viewModel.submitReport(photo.id, photo.userName, "PHOTO", "Inappropriate content") }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = photo.caption, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photo.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = photo.caption,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.toggleLikePhoto(photo) }) {
                                Icon(
                                    imageVector = if (photo.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (photo.isLikedByMe) VoraRose else TextSecondary
                                )
                            }
                            Text(text = "${photo.likesCount}", fontSize = 13.sp, color = TextPrimary)

                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment", tint = TextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${photo.commentsCount}", fontSize = 13.sp, color = TextPrimary)
                        }

                        IconButton(onClick = { viewModel.showToast("Photo link copied!") }) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = VoraCyan)
                        }
                    }
                }
            }
        }
    }

    if (showUploadDialog) {
        UploadPhotoDialog(
            onDismiss = { showUploadDialog = false },
            onUpload = { url, caption ->
                viewModel.uploadPhoto(url, caption) {
                    showUploadDialog = false
                }
            }
        )
    }
}

@Composable
private fun UploadPhotoDialog(
    onDismiss: () -> Unit,
    onUpload: (url: String, caption: String) -> Unit
) {
    var photoUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800") }
    var caption by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkNavySurface,
        title = { Text("Upload Photo", color = TextPrimary) },
        text = {
            Column {
                OutlinedTextField(
                    value = photoUrl, onValueChange = { photoUrl = it },
                    label = { Text("Photo Image URL") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = caption, onValueChange = { caption = it },
                    label = { Text("Caption / Story") }, modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (caption.isNotEmpty()) onUpload(photoUrl, caption) },
                colors = ButtonDefaults.buttonColors(containerColor = VoraPurple)
            ) {
                Text("Publish Photo", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
