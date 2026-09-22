package com.example.data.repository

import com.example.data.dao.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class VoraRepository(
    private val userDao: UserDao,
    private val voiceRoomDao: VoiceRoomDao,
    private val callHistoryDao: CallHistoryDao,
    private val messageDao: MessageDao,
    private val photoDao: PhotoDao,
    private val giftDao: GiftDao,
    private val walletDao: WalletDao,
    private val notificationDao: NotificationDao,
    private val reportDao: ReportDao,
    private val blockedUserDao: BlockedUserDao,
    private val rolePermissionDao: RolePermissionDao
) {
    // User / Auth
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    val onlineUsers: Flow<List<UserEntity>> = userDao.getOnlineUsers()
    val totalUsersCount: Flow<Int> = userDao.getUsersCount()
    val onlineUsersCount: Flow<Int> = userDao.getOnlineUsersCount()

    suspend fun getUserById(userId: String) = userDao.getUserById(userId)
    fun observeUserById(userId: String) = userDao.observeUserById(userId)
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    suspend fun getUserByMobile(mobile: String) = userDao.getUserByMobile(mobile)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    suspend fun deleteUser(userId: String) = userDao.deleteUser(userId)

    // Voice Rooms
    val allRooms: Flow<List<VoiceRoomEntity>> = voiceRoomDao.getAllRooms()
    val totalRoomsCount: Flow<Int> = voiceRoomDao.getRoomsCount()

    suspend fun getRoomById(roomId: String) = voiceRoomDao.getRoomById(roomId)
    fun observeRoomById(roomId: String) = voiceRoomDao.observeRoomById(roomId)
    suspend fun insertRoom(room: VoiceRoomEntity) = voiceRoomDao.insertRoom(room)
    suspend fun deleteRoom(roomId: String) = voiceRoomDao.deleteRoom(roomId)
    fun getRoomParticipants(roomId: String) = voiceRoomDao.getRoomParticipants(roomId)
    suspend fun insertParticipant(participant: RoomParticipantEntity) = voiceRoomDao.insertParticipant(participant)
    suspend fun removeParticipant(roomId: String, userId: String) = voiceRoomDao.removeParticipant(roomId, userId)

    // Calls
    fun getCallsForUser(userId: String) = callHistoryDao.getCallsForUser(userId)
    suspend fun insertCall(call: CallHistoryEntity) = callHistoryDao.insertCall(call)
    val totalCallsCount: Flow<Int> = callHistoryDao.getTotalCallsCount()

    // Messages
    fun getMessagesForConversation(conversationId: String) = messageDao.getMessagesForConversation(conversationId)
    fun getAllMessagesForUser(userId: String) = messageDao.getAllMessagesForUser(userId)
    suspend fun insertMessage(message: MessageEntity) = messageDao.insertMessage(message)
    suspend fun deleteMessage(messageId: String) = messageDao.deleteMessage(messageId)
    val totalMessagesCount: Flow<Int> = messageDao.getTotalMessagesCount()

    // Photos
    val allPhotos: Flow<List<PhotoEntity>> = photoDao.getAllPhotos()
    fun getPhotosByUser(userId: String) = photoDao.getPhotosByUser(userId)
    suspend fun insertPhoto(photo: PhotoEntity) = photoDao.insertPhoto(photo)
    suspend fun deletePhoto(photoId: String) = photoDao.deletePhoto(photoId)
    val totalPhotosCount: Flow<Int> = photoDao.getTotalPhotosCount()

    // Gifts
    val allGifts: Flow<List<GiftEntity>> = giftDao.getAllGifts()
    val allGiftTransactions: Flow<List<GiftTransactionEntity>> = giftDao.getAllGiftTransactions()
    val totalGiftsSentCount: Flow<Int> = giftDao.getTotalGiftsSentCount()

    fun getGiftsReceivedByUser(userId: String) = giftDao.getGiftsReceivedByUser(userId)
    suspend fun insertGift(gift: GiftEntity) = giftDao.insertGift(gift)

    suspend fun sendGift(
        sender: UserEntity,
        receiverId: String,
        receiverName: String,
        gift: GiftEntity,
        roomId: String = ""
    ): Boolean {
        if (sender.coins < gift.coinPrice) return false

        // Deduct coins from sender
        val updatedSender = sender.copy(coins = sender.coins - gift.coinPrice)
        userDao.updateUser(updatedSender)

        // Add coins or gifts to receiver if exists
        val receiver = userDao.getUserById(receiverId)
        if (receiver != null) {
            val updatedReceiver = receiver.copy(coins = receiver.coins + (gift.coinPrice * 8 / 10))
            userDao.updateUser(updatedReceiver)
        }

        // Record Gift Transaction
        val txn = GiftTransactionEntity(
            id = "GFT-TXN-${System.currentTimeMillis()}",
            giftId = gift.id,
            giftName = gift.name,
            giftIcon = gift.icon,
            coinPrice = gift.coinPrice,
            senderUserId = sender.id,
            senderName = sender.name,
            receiverUserId = receiverId,
            receiverName = receiverName,
            roomId = roomId
        )
        giftDao.insertGiftTransaction(txn)

        // Wallet Transaction Log
        val walletTxn = WalletTransactionEntity(
            id = "WLT-${System.currentTimeMillis()}",
            userId = sender.id,
            transactionType = "GIFT_SENT",
            coinsAmount = -gift.coinPrice,
            description = "Sent ${gift.name} ${gift.icon} to $receiverName"
        )
        walletDao.insertWalletTransaction(walletTxn)

        // Notification for receiver
        notificationDao.insertNotification(
            NotificationEntity(
                id = "NOTIF-${System.currentTimeMillis()}",
                userId = receiverId,
                title = "Gift Received! ${gift.icon}",
                message = "${sender.name} sent you a ${gift.name}!",
                type = "GIFT"
            )
        )
        return true
    }

    // Wallet
    fun getWalletTransactionsForUser(userId: String) = walletDao.getTransactionsForUser(userId)
    val allWalletTransactions: Flow<List<WalletTransactionEntity>> = walletDao.getAllWalletTransactions()

    suspend fun buyCoins(user: UserEntity, coinsAmount: Int, priceLabel: String) {
        val updated = user.copy(coins = user.coins + coinsAmount)
        userDao.updateUser(updated)

        walletDao.insertWalletTransaction(
            WalletTransactionEntity(
                id = "WLT-${System.currentTimeMillis()}",
                userId = user.id,
                transactionType = "PURCHASE",
                coinsAmount = coinsAmount,
                description = "Purchased $coinsAmount Coins ($priceLabel)"
            )
        )
    }

    // Notifications
    fun getNotificationsForUser(userId: String) = notificationDao.getNotificationsForUser(userId)
    suspend fun markNotificationsRead(userId: String) = notificationDao.markAllAsRead(userId)
    suspend fun insertNotification(notification: NotificationEntity) = notificationDao.insertNotification(notification)

    // Reports
    val allReports: Flow<List<ReportEntity>> = reportDao.getAllReports()
    val pendingReportsCount: Flow<Int> = reportDao.getPendingReportsCount()
    suspend fun insertReport(report: ReportEntity) = reportDao.insertReport(report)
    suspend fun updateReportStatus(reportId: String, status: String) = reportDao.updateReportStatus(reportId, status)

    // Blocked Users
    fun getBlockedUsersForUser(userId: String) = blockedUserDao.getBlockedUsersForUser(userId)
    suspend fun blockUser(userId: String, blockedUserId: String, blockedUserName: String) {
        blockedUserDao.blockUser(BlockedUserEntity(userId, blockedUserId, blockedUserName))
    }
    suspend fun unblockUser(userId: String, blockedUserId: String) {
        blockedUserDao.unblockUser(userId, blockedUserId)
    }

    // Roles & Permissions
    val allRolePermissions: Flow<List<RolePermissionEntity>> = rolePermissionDao.getAllRolePermissions()
    suspend fun insertRolePermission(rp: RolePermissionEntity) = rolePermissionDao.insertRolePermission(rp)
}
