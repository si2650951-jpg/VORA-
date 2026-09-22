package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val username: String,
    val email: String,
    val mobile: String,
    val password: String,
    val photoUrl: String,
    val coverUrl: String,
    val dob: String,
    val country: String,
    val gender: String,
    val bio: String,
    val role: String = "USER", // SUPER_ADMIN, ADMIN, MODERATOR, USER
    val coins: Int = 1200,
    val followersCount: Int = 128,
    val followingCount: Int = 45,
    val isOnline: Boolean = true,
    val isVerified: Boolean = true,
    val isSuspended: Boolean = false,
    val isBanned: Boolean = false,
    val allowCallsFrom: String = "EVERYONE",
    val allowMessagesFrom: String = "EVERYONE",
    val allowGiftsFrom: String = "EVERYONE",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "voice_rooms")
data class VoiceRoomEntity(
    @PrimaryKey val id: String,
    val title: String,
    val topic: String,
    val hostUserId: String,
    val hostName: String,
    val hostAvatar: String,
    val activeSpeakersCount: Int = 4,
    val listenersCount: Int = 28,
    val isPrivate: Boolean = false,
    val passcode: String = "",
    val tags: String = "Music, Talk, Tech",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "room_participants", primaryKeys = ["roomId", "userId"])
data class RoomParticipantEntity(
    val roomId: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val role: String = "SPEAKER", // HOST, CO_HOST, SPEAKER, LISTENER
    val isMuted: Boolean = false,
    val micSeatIndex: Int = -1
)

@Entity(tableName = "call_history")
data class CallHistoryEntity(
    @PrimaryKey val id: String,
    val callType: String, // VOICE, VIDEO
    val callerUserId: String,
    val callerName: String,
    val callerAvatar: String,
    val receiverUserId: String,
    val receiverName: String,
    val receiverAvatar: String,
    val status: String, // COMPLETED, MISSED, REJECTED
    val durationSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderUserId: String,
    val receiverUserId: String,
    val messageType: String, // TEXT, PHOTO, VOICE, GIFT
    val text: String,
    val mediaUrl: String = "",
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val photoUrl: String,
    val caption: String,
    val likesCount: Int = 12,
    val commentsCount: Int = 3,
    val isLikedByMe: Boolean = false,
    val isModerated: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "gifts")
data class GiftEntity(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val coinPrice: Int,
    val category: String = "POPULAR",
    val isActive: Boolean = true
)

@Entity(tableName = "gift_transactions")
data class GiftTransactionEntity(
    @PrimaryKey val id: String,
    val giftId: String,
    val giftName: String,
    val giftIcon: String,
    val coinPrice: Int,
    val senderUserId: String,
    val senderName: String,
    val receiverUserId: String,
    val receiverName: String,
    val roomId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val transactionType: String, // PURCHASE, GIFT_SENT, GIFT_RECEIVED, REWARD
    val coinsAmount: Int,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String, // FOLLOWER, MESSAGE, VOICE_CALL, VIDEO_CALL, GIFT, ROOM_INVITE, LIKE, SYSTEM
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val reporterUserId: String,
    val reporterName: String,
    val reportedTargetId: String,
    val reportedTargetName: String,
    val targetType: String, // USER, PHOTO, ROOM, MESSAGE
    val reason: String,
    val status: String = "PENDING", // PENDING, RESOLVED, DISMISSED
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "blocked_users", primaryKeys = ["userId", "blockedUserId"])
data class BlockedUserEntity(
    val userId: String,
    val blockedUserId: String,
    val blockedUserName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "roles_permissions")
data class RolePermissionEntity(
    @PrimaryKey val role: String,
    val permissions: String
)
