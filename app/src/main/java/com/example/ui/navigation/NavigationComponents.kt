package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserEntity
import com.example.ui.components.AvatarWithStatus
import com.example.ui.components.RoleBadge
import com.example.ui.theme.*

@Composable
fun VoraBottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationItem("Home", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("Discover", Screen.Discover.route, Icons.Filled.Explore, Icons.Outlined.Explore),
        NavigationItem("Rooms", Screen.VoiceRooms.route, Icons.Filled.Mic, Icons.Outlined.Mic),
        NavigationItem("Messages", Screen.Chat.route, Icons.Filled.Chat, Icons.Outlined.Chat),
        NavigationItem("Profile", Screen.Profile.route, Icons.Filled.Person, Icons.Outlined.Person)
    )

    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(72.dp),
        containerColor = DarkNavySurface,
        contentColor = TextPrimary
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Box(
                        modifier = if (isSelected) {
                            Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VoraPurple.copy(alpha = 0.25f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        } else Modifier
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (isSelected) VoraPurpleLight else TextSecondary
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) VoraPurpleLight else TextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

private data class NavigationItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun VoraDrawerContent(
    user: UserEntity?,
    onNavigate: (String) -> Unit,
    onRoleSwitch: (String) -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        drawerContainerColor = DarkNavyBackground,
        drawerContentColor = TextPrimary,
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            // App Branding Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(VoraPurple, VoraCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Logo",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "VORA VOICE",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Social & Audio Hub",
                            fontSize = 11.sp,
                            color = VoraCyan
                        )
                    }
                }
                IconButton(onClick = onCloseDrawer) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // User Info Box
            if (user != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarWithStatus(
                            imageUrl = user.photoUrl,
                            size = 46.dp,
                            isOnline = user.isOnline,
                            showCrown = user.role.contains("ADMIN")
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "@${user.username} (${user.id})",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            RoleBadge(role = user.role)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Role Quick Switcher for Testing
                Text(
                    text = "Quick Role Switch (Dev Testing)",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("SUPER_ADMIN", "ADMIN", "USER").forEach { roleName ->
                        val isSelected = user.role == roleName
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) VoraPurple else DarkNavySurface)
                                .clickable { onRoleSwitch(roleName) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (roleName == "SUPER_ADMIN") "S-Admin" else roleName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = GlassBorder)

            // Main Menu Navigation Items
            DrawerMenuItem(Icons.Default.Dashboard, "Dashboard", Screen.UserDashboard.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.Person, "My Profile", Screen.Profile.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.PhotoLibrary, "Photos", Screen.Photos.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.CardGiftcard, "Gifts", Screen.Gifts.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.AccountBalanceWallet, "Wallet & Coins", Screen.Wallet.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.Notifications, "Notifications", Screen.Notifications.route, onNavigate, onCloseDrawer)

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)

            // Admin Section (Highlight Admin Access)
            if (user?.role?.contains("ADMIN") == true || user?.role == "MODERATOR") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.horizontalGradient(listOf(VoraPurpleDark, VoraIndigo)))
                        .clickable {
                            onNavigate(Screen.AdminDashboard.route)
                            onCloseDrawer()
                        }
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = VoraGold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Admin Panel", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text(text = "System moderation & management", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            DrawerMenuItem(Icons.Default.Settings, "Settings", Screen.Settings.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.Info, "About VORA", Screen.About.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.ContactSupport, "Contact Us", Screen.Contact.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.Security, "Privacy Policy", Screen.PrivacyPolicy.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.Description, "Terms & Conditions", Screen.Terms.route, onNavigate, onCloseDrawer)
            DrawerMenuItem(Icons.Default.Help, "Help & Support", Screen.HelpSupport.route, onNavigate, onCloseDrawer)

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GlassBorder)

            // Logout Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onLogout()
                        onCloseDrawer()
                    }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = VoraRose)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Logout", color = VoraRose, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    route: String,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onNavigate(route)
                onCloseDrawer()
            }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = VoraPurpleLight, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
