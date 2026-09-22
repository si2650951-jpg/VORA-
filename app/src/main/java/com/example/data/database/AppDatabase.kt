package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        VoiceRoomEntity::class,
        RoomParticipantEntity::class,
        CallHistoryEntity::class,
        MessageEntity::class,
        PhotoEntity::class,
        GiftEntity::class,
        GiftTransactionEntity::class,
        WalletTransactionEntity::class,
        NotificationEntity::class,
        ReportEntity::class,
        BlockedUserEntity::class,
        RolePermissionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun voiceRoomDao(): VoiceRoomDao
    abstract fun callHistoryDao(): CallHistoryDao
    abstract fun messageDao(): MessageDao
    abstract fun photoDao(): PhotoDao
    abstract fun giftDao(): GiftDao
    abstract fun walletDao(): WalletDao
    abstract fun notificationDao(): NotificationDao
    abstract fun reportDao(): ReportDao
    abstract fun blockedUserDao(): BlockedUserDao
    abstract fun rolePermissionDao(): RolePermissionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vora_voice_database"
                )
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database)
                    }
                }
            }
        }

        private suspend fun seedDatabase(database: AppDatabase) {
            // Seed Default Users
            val defaultUsers = listOf(
                UserEntity(
                    id = "VORA-1001",
                    name = "Super Admin",
                    username = "superadmin",
                    email = "admin@vora.com",
                    mobile = "+1234567890",
                    password = "admin",
                    photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
                    coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800",
                    dob = "1995-05-15",
                    country = "United States",
                    gender = "Male",
                    bio = "VORA Voice Platform Chief Administrator 👑",
                    role = "SUPER_ADMIN",
                    coins = 50000,
                    followersCount = 1250,
                    followingCount = 12
                ),
                UserEntity(
                    id = "VORA-1002",
                    name = "Elena Rostova",
                    username = "elena_v",
                    email = "elena@vora.com",
                    mobile = "+1987654321",
                    password = "password123",
                    photoUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400",
                    coverUrl = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?w=800",
                    dob = "1998-08-20",
                    country = "United Kingdom",
                    gender = "Female",
                    bio = "Singer & Voice Room Host 🎤✨ Love music and midnight chats!",
                    role = "USER",
                    coins = 4800,
                    followersCount = 890,
                    followingCount = 112
                ),
                UserEntity(
                    id = "VORA-1003",
                    name = "Marcus Vance",
                    username = "marcus_dev",
                    email = "marcus@vora.com",
                    mobile = "+1555019283",
                    password = "password123",
                    photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                    coverUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800",
                    dob = "1996-12-01",
                    country = "Canada",
                    gender = "Male",
                    bio = "Tech Enthusiast | Podcast Host 🎧 Building future software",
                    role = "MODERATOR",
                    coins = 2300,
                    followersCount = 430,
                    followingCount = 95
                ),
                UserEntity(
                    id = "VORA-1004",
                    name = "Aria Chen",
                    username = "aria_music",
                    email = "aria@vora.com",
                    mobile = "+1555987123",
                    password = "password123",
                    photoUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400",
                    coverUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=800",
                    dob = "2000-03-10",
                    country = "Singapore",
                    gender = "Female",
                    bio = "Voice Artist & Gamer 🎮 Drop by my acoustic sessions!",
                    role = "USER",
                    coins = 3100,
                    followersCount = 670,
                    followingCount = 88
                )
            )
            defaultUsers.forEach { database.userDao().insertUser(it) }

            // Seed Voice Rooms
            val defaultRooms = listOf(
                VoiceRoomEntity(
                    id = "ROOM-01",
                    title = "Late Night Acoustic & Chill 🎸",
                    topic = "Music & Lounge",
                    hostUserId = "VORA-1002",
                    hostName = "Elena Rostova",
                    hostAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400",
                    activeSpeakersCount = 5,
                    listenersCount = 42,
                    tags = "Music, Acoustic, Chill"
                ),
                VoiceRoomEntity(
                    id = "ROOM-02",
                    title = "Tech Talk & AI Revolution 🤖",
                    topic = "Technology",
                    hostUserId = "VORA-1003",
                    hostName = "Marcus Vance",
                    hostAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                    activeSpeakersCount = 3,
                    listenersCount = 85,
                    tags = "Tech, AI, Software"
                ),
                VoiceRoomEntity(
                    id = "ROOM-03",
                    title = "Global English Conversation Lounge 🌍",
                    topic = "Education & Social",
                    hostUserId = "VORA-1004",
                    hostName = "Aria Chen",
                    hostAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400",
                    activeSpeakersCount = 6,
                    listenersCount = 120,
                    tags = "Languages, Social, Practice"
                )
            )
            defaultRooms.forEach { database.voiceRoomDao().insertRoom(it) }

            // Seed Gifts
            val defaultGifts = listOf(
                GiftEntity("GIFT-01", "Heart", "❤️", 10, "BASIC"),
                GiftEntity("GIFT-02", "Rose", "🌹", 25, "BASIC"),
                GiftEntity("GIFT-03", "Star", "⭐", 50, "POPULAR"),
                GiftEntity("GIFT-04", "Fire", "🔥", 100, "POPULAR"),
                GiftEntity("GIFT-05", "Crown", "👑", 250, "PREMIUM"),
                GiftEntity("GIFT-06", "Diamond", "💎", 500, "PREMIUM"),
                GiftEntity("GIFT-07", "Trophy", "🏆", 1000, "LUXURY")
            )
            defaultGifts.forEach { database.giftDao().insertGift(it) }

            // Seed Photos
            val defaultPhotos = listOf(
                PhotoEntity(
                    id = "PHOTO-01",
                    userId = "VORA-1002",
                    userName = "Elena Rostova",
                    userAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400",
                    photoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800",
                    caption = "Live studio session tonight! Join my room at 9 PM 🎙️✨",
                    likesCount = 245,
                    commentsCount = 18
                ),
                PhotoEntity(
                    id = "PHOTO-02",
                    userId = "VORA-1003",
                    userName = "Marcus Vance",
                    userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                    photoUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800",
                    caption = "Setup ready for tech podcast episode #42 💻🚀",
                    likesCount = 180,
                    commentsCount = 12
                )
            )
            defaultPhotos.forEach { database.photoDao().insertPhoto(it) }

            // Seed Notifications
            val defaultNotifs = listOf(
                NotificationEntity(
                    id = "NOTIF-01",
                    userId = "VORA-1001",
                    title = "Welcome to VORA Voice!",
                    message = "Your premium voice chat and social hub is now live.",
                    type = "SYSTEM"
                ),
                NotificationEntity(
                    id = "NOTIF-02",
                    userId = "VORA-1001",
                    title = "Gift Received! 👑",
                    message = "Elena Rostova sent you a Crown gift in Late Night Room.",
                    type = "GIFT"
                ),
                NotificationEntity(
                    id = "NOTIF-03",
                    userId = "VORA-1001",
                    title = "New Follower",
                    message = "Aria Chen started following you.",
                    type = "FOLLOWER"
                )
            )
            defaultNotifs.forEach { database.notificationDao().insertNotification(it) }

            // Seed Roles & Permissions
            val defaultRoles = listOf(
                RolePermissionEntity("SUPER_ADMIN", "ALL_PERMISSIONS,MANAGE_ADMINS,MANAGE_SYSTEM,MANAGE_USERS,MODERATE_ROOMS,BAN_USERS,MANAGE_GIFTS"),
                RolePermissionEntity("ADMIN", "MANAGE_USERS,MODERATE_ROOMS,BAN_USERS,MANAGE_GIFTS,VIEW_REPORTS"),
                RolePermissionEntity("MODERATOR", "MODERATE_ROOMS,REPORT_USERS,MUTE_PARTICIPANTS"),
                RolePermissionEntity("USER", "CREATE_ROOM,JOIN_CALL,SEND_MESSAGES,SEND_GIFTS,UPLOAD_PHOTOS")
            )
            defaultRoles.forEach { database.rolePermissionDao().insertRolePermission(it) }
        }
    }
}
