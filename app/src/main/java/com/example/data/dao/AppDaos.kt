package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun observeUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE isOnline = 1")
    fun getOnlineUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    @Query("SELECT COUNT(*) FROM users")
    fun getUsersCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM users WHERE isOnline = 1")
    fun getOnlineUsersCount(): Flow<Int>
}

@Dao
interface VoiceRoomDao {
    @Query("SELECT * FROM voice_rooms ORDER BY createdAt DESC")
    fun getAllRooms(): Flow<List<VoiceRoomEntity>>

    @Query("SELECT * FROM voice_rooms WHERE id = :roomId")
    suspend fun getRoomById(roomId: String): VoiceRoomEntity?

    @Query("SELECT * FROM voice_rooms WHERE id = :roomId")
    fun observeRoomById(roomId: String): Flow<VoiceRoomEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: VoiceRoomEntity)

    @Query("DELETE FROM voice_rooms WHERE id = :roomId")
    suspend fun deleteRoom(roomId: String)

    @Query("SELECT * FROM room_participants WHERE roomId = :roomId")
    fun getRoomParticipants(roomId: String): Flow<List<RoomParticipantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: RoomParticipantEntity)

    @Query("DELETE FROM room_participants WHERE roomId = :roomId AND userId = :userId")
    suspend fun removeParticipant(roomId: String, userId: String)

    @Query("SELECT COUNT(*) FROM voice_rooms")
    fun getRoomsCount(): Flow<Int>
}

@Dao
interface CallHistoryDao {
    @Query("SELECT * FROM call_history WHERE callerUserId = :userId OR receiverUserId = :userId ORDER BY timestamp DESC")
    fun getCallsForUser(userId: String): Flow<List<CallHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(call: CallHistoryEntity)

    @Query("SELECT COUNT(*) FROM call_history")
    fun getTotalCallsCount(): Flow<Int>
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE senderUserId = :userId OR receiverUserId = :userId ORDER BY timestamp DESC")
    fun getAllMessagesForUser(userId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Query("SELECT COUNT(*) FROM messages")
    fun getTotalMessagesCount(): Flow<Int>
}

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos ORDER BY timestamp DESC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE userId = :userId ORDER BY timestamp DESC")
    fun getPhotosByUser(userId: String): Flow<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoEntity)

    @Query("DELETE FROM photos WHERE id = :photoId")
    suspend fun deletePhoto(photoId: String)

    @Query("SELECT COUNT(*) FROM photos")
    fun getTotalPhotosCount(): Flow<Int>
}

@Dao
interface GiftDao {
    @Query("SELECT * FROM gifts WHERE isActive = 1")
    fun getAllGifts(): Flow<List<GiftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: GiftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGiftTransaction(transaction: GiftTransactionEntity)

    @Query("SELECT * FROM gift_transactions WHERE receiverUserId = :userId ORDER BY timestamp DESC")
    fun getGiftsReceivedByUser(userId: String): Flow<List<GiftTransactionEntity>>

    @Query("SELECT * FROM gift_transactions ORDER BY timestamp DESC")
    fun getAllGiftTransactions(): Flow<List<GiftTransactionEntity>>

    @Query("SELECT COUNT(*) FROM gift_transactions")
    fun getTotalGiftsSentCount(): Flow<Int>
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactionsForUser(userId: String): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalletTransaction(transaction: WalletTransactionEntity)

    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllWalletTransactions(): Flow<List<WalletTransactionEntity>>
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: String)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("UPDATE reports SET status = :status WHERE id = :reportId")
    suspend fun updateReportStatus(reportId: String, status: String)

    @Query("SELECT COUNT(*) FROM reports WHERE status = 'PENDING'")
    fun getPendingReportsCount(): Flow<Int>
}

@Dao
interface BlockedUserDao {
    @Query("SELECT * FROM blocked_users WHERE userId = :userId")
    fun getBlockedUsersForUser(userId: String): Flow<List<BlockedUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun blockUser(blockedUser: BlockedUserEntity)

    @Query("DELETE FROM blocked_users WHERE userId = :userId AND blockedUserId = :blockedUserId")
    suspend fun unblockUser(userId: String, blockedUserId: String)
}

@Dao
interface RolePermissionDao {
    @Query("SELECT * FROM roles_permissions")
    fun getAllRolePermissions(): Flow<List<RolePermissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRolePermission(rolePermission: RolePermissionEntity)
}
