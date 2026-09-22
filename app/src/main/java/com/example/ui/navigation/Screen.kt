package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object SignUp : Screen("sign_up", "Sign Up")
    object SignIn : Screen("sign_in", "Sign In")
    object GmailLogin : Screen("gmail_login", "Gmail Login")
    object FacebookLogin : Screen("facebook_login", "Facebook Login")
    object MobileLogin : Screen("mobile_login", "Mobile Login")
    object OtpVerification : Screen("otp_verification", "OTP Verification")
    object UserDashboard : Screen("user_dashboard", "User Dashboard")
    object Profile : Screen("profile", "Profile")
    object EditProfile : Screen("edit_profile", "Edit Profile")
    object UserControl : Screen("user_control", "User Controls")
    object VoiceCall : Screen("voice_call", "Voice Call")
    object VideoCall : Screen("video_call", "Video Call")
    object VoiceRooms : Screen("voice_rooms", "Voice Rooms")
    object VoiceRoomDetail : Screen("voice_room_detail/{roomId}", "Voice Room") {
        fun createRoute(roomId: String) = "voice_room_detail/$roomId"
    }
    object Chat : Screen("chat", "Messages")
    object ChatDetail : Screen("chat_detail/{userId}", "Chat") {
        fun createRoute(userId: String) = "chat_detail/$userId"
    }
    object Photos : Screen("photos", "Photos")
    object Gifts : Screen("gifts", "Gifts Store")
    object Wallet : Screen("wallet", "Wallet & Coins")
    object Notifications : Screen("notifications", "Notifications")
    object Discover : Screen("discover", "Discover")
    object Settings : Screen("settings", "Settings")
    object About : Screen("about", "About VORA")
    object Contact : Screen("contact", "Contact Us")
    object PrivacyPolicy : Screen("privacy_policy", "Privacy Policy")
    object Terms : Screen("terms", "Terms & Conditions")
    object HelpSupport : Screen("help_support", "Help & Support")

    // Admin Panel
    object AdminLogin : Screen("admin_login", "Admin Login")
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard")
    object UserManagement : Screen("user_management", "User Management")
    object GiftManagement : Screen("gift_management", "Gift Management")
    object CoinManagement : Screen("coin_management", "Coin Management")
    object RoomManagement : Screen("room_management", "Room Management")
    object PhotoManagement : Screen("photo_management", "Photo Management")
    object Reports : Screen("reports", "Reports & Moderation")
    object DatabaseManagement : Screen("database_management", "Database Inspector")
    object RolesPermissions : Screen("roles_permissions", "Roles & Permissions")
}
