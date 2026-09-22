package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.screens.admin.*
import com.example.ui.screens.auth.*
import com.example.ui.screens.chat.*
import com.example.ui.screens.dashboard.*
import com.example.ui.screens.discover.*
import com.example.ui.screens.gifts.*
import com.example.ui.screens.home.*
import com.example.ui.screens.notifications.*
import com.example.ui.screens.photos.*
import com.example.ui.screens.profile.*
import com.example.ui.screens.settings.*
import com.example.ui.screens.staticpages.*
import com.example.ui.screens.video.*
import com.example.ui.screens.voice.*
import com.example.ui.screens.wallet.*
import com.example.ui.theme.VoraBlue
import com.example.ui.theme.VoraRose
import com.example.ui.viewmodel.VoraViewModel

@Composable
fun VoraNavGraph(
    navController: NavHostController,
    viewModel: VoraViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // Auth Routes
        composable(Screen.SignIn.route) {
            SignInScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.GmailLogin.route) {
            SocialLoginScreen("Gmail", Icons.Default.Email, VoraRose, viewModel) { navController.navigate(it) }
        }
        composable(Screen.FacebookLogin.route) {
            SocialLoginScreen("Facebook", Icons.Default.ThumbUp, VoraBlue, viewModel) { navController.navigate(it) }
        }
        composable(Screen.MobileLogin.route) {
            MobileLoginScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.OtpVerification.route) {
            OtpScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // User Dashboard & Controls
        composable(Screen.UserDashboard.route) {
            UserDashboardScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.UserControl.route) {
            UserControlScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // Voice & Video Communication
        composable(Screen.VoiceRooms.route) {
            VoiceRoomsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(
            route = Screen.VoiceRoomDetail.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStack ->
            val roomId = backStack.arguments?.getString("roomId") ?: ""
            VoiceRoomDetailScreen(roomId = roomId, viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.VoiceCall.route) {
            VoiceCallScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.VideoCall.route) {
            VideoCallScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // Messaging
        composable(Screen.Chat.route) {
            ChatListScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStack ->
            val userId = backStack.arguments?.getString("userId") ?: ""
            ChatDetailScreen(userId = userId, viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // Social Feeds & Stores
        composable(Screen.Photos.route) {
            PhotosScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Gifts.route) {
            GiftsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Wallet.route) {
            WalletScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Discover.route) {
            DiscoverScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // Profile & Edit Profile
        composable(Screen.Profile.route) {
            ProfileScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }

        // Settings & Information Pages
        composable(Screen.Settings.route) {
            SettingsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.About.route) {
            AboutScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Contact.route) {
            ContactScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Terms.route) {
            TermsScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.HelpSupport.route) {
            HelpSupportScreen(onNavigate = { navController.navigate(it) })
        }

        // Admin Panel Subpages
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.UserManagement.route) {
            AdminUserManagementScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.RoomManagement.route) {
            AdminRoomManagementScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.GiftManagement.route) {
            GiftsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.CoinManagement.route) {
            WalletScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.PhotoManagement.route) {
            PhotosScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Reports.route) {
            AdminReportsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.DatabaseManagement.route) {
            AdminDatabaseScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
        composable(Screen.RolesPermissions.route) {
            AdminRolesPermissionsScreen(viewModel = viewModel, onNavigate = { navController.navigate(it) })
        }
    }
}
