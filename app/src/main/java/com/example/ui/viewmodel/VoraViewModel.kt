package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.*
import com.example.data.repository.VoraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class ActiveCallState(
    val callId: String,
    val callType: String, // VOICE or VIDEO
    val otherUser: UserEntity,
    val isMicMuted: Boolean = false,
    val isCameraOn: Boolean = true,
    val isSpeakerOn: Boolean = true,
    val durationSeconds: Int = 0,
    val status: String = "CONNECTED"
)

class VoraViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = VoraRepository(
        db.userDao(),
        db.voiceRoomDao(),
        db.callHistoryDao(),
        db.messageDao(),
        db.photoDao(),
        db.giftDao(),
        db.walletDao(),
        db.notificationDao(),
        db.reportDao(),
        db.blockedUserDao(),
        db.rolePermissionDao()
    )

    // Current Auth User
    private val _currentUserId = MutableStateFlow<String?>("VORA-1001") // Default logged in as Super Admin
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.observeUserById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Lists & Feeds
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val onlineUsers: StateFlow<List<UserEntity>> = repository.onlineUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRooms: StateFlow<List<VoiceRoomEntity>> = repository.allRooms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPhotos: StateFlow<List<PhotoEntity>> = repository.allPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGifts: StateFlow<List<GiftEntity>> = repository.allGifts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getNotificationsForUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val callLogs: StateFlow<List<CallHistoryEntity>> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getCallsForUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userWalletTxns: StateFlow<List<WalletTransactionEntity>> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getWalletTransactionsForUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRolePermissions: StateFlow<List<RolePermissionEntity>> = repository.allRolePermissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Counts
    val totalUsersCount = repository.totalUsersCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val onlineUsersCount = repository.onlineUsersCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalRoomsCount = repository.totalRoomsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalCallsCount = repository.totalCallsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalMessagesCount = repository.totalMessagesCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalPhotosCount = repository.totalPhotosCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalGiftsSentCount = repository.totalGiftsSentCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val pendingReportsCount = repository.pendingReportsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Active Call State
    private val _activeCall = MutableStateFlow<ActiveCallState?>(null)
    val activeCall: StateFlow<ActiveCallState?> = _activeCall.asStateFlow()

    // Active Voice Room State
    private val _activeRoomId = MutableStateFlow<String?>(null)
    val activeRoomId: StateFlow<String?> = _activeRoomId.asStateFlow()

    val activeVoiceRoom: StateFlow<VoiceRoomEntity?> = _activeRoomId
        .flatMapLatest { roomId ->
            if (roomId == null) flowOf(null) else repository.observeRoomById(roomId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeRoomParticipants: StateFlow<List<RoomParticipantEntity>> = _activeRoomId
        .flatMapLatest { roomId ->
            if (roomId == null) flowOf(emptyList()) else repository.getRoomParticipants(roomId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Toast/Feedback Message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun clearToast() { _toastMessage.value = null }
    fun showToast(msg: String) { _toastMessage.value = msg }

    // Authentication Actions
    fun login(emailOrUser: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val userByEmail = repository.getUserByEmail(emailOrUser)
            val userByUsername = repository.getUserByUsername(emailOrUser)
            val user = userByEmail ?: userByUsername

            if (user != null && user.password == pass) {
                if (user.isBanned) {
                    showToast("Account is banned. Contact administration.")
                    return@launch
                }
                _currentUserId.value = user.id
                repository.updateUser(user.copy(isOnline = true))
                showToast("Welcome back, ${user.name}!")
                onSuccess()
            } else {
                showToast("Invalid credentials. Try superadmin / admin")
            }
        }
    }

    fun loginWithMobile(mobile: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            var user = repository.getUserByMobile(mobile)
            if (user == null) {
                user = UserEntity(
                    id = "VORA-${(1000..9999).random()}",
                    name = "User $mobile",
                    username = "user_${mobile.takeLast(4)}",
                    email = "user_${mobile.takeLast(4)}@vora.com",
                    mobile = mobile,
                    password = "otp_login",
                    photoUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400",
                    coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800",
                    dob = "2000-01-01",
                    country = "Global",
                    gender = "Unspecified",
                    bio = "VORA Voice Member 🎙️"
                )
                repository.insertUser(user)
            }
            _currentUserId.value = user.id
            repository.updateUser(user.copy(isOnline = true))
            showToast("Mobile verified! Logged in as ${user.name}")
            onSuccess()
        }
    }

    fun register(
        fullName: String,
        username: String,
        email: String,
        mobile: String,
        pass: String,
        country: String,
        gender: String,
        bio: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val newId = "VORA-${(1000..9999).random()}"
            val newUser = UserEntity(
                id = newId,
                name = fullName,
                username = username,
                email = email,
                mobile = mobile,
                password = pass,
                photoUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400",
                coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800",
                dob = "2000-01-01",
                country = country.ifEmpty { "Global" },
                gender = gender.ifEmpty { "Other" },
                bio = bio.ifEmpty { "Hello, I am on VORA Voice! 🌟" }
            )
            repository.insertUser(newUser)
            _currentUserId.value = newId
            showToast("Registration successful! Your User ID is $newId")
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value
            if (user != null) {
                repository.updateUser(user.copy(isOnline = false))
            }
            _currentUserId.value = null
            _activeCall.value = null
            _activeRoomId.value = null
            showToast("Logged out successfully.")
            onSuccess()
        }
    }

    // Role Quick Switcher for testing Admin & Super Admin & User features
    fun switchUserRole(newRole: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updated = user.copy(role = newRole)
            repository.updateUser(updated)
            showToast("User role updated to $newRole")
        }
    }

    // Voice Room Actions
    fun createVoiceRoom(title: String, topic: String, isPrivate: Boolean, passcode: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val roomId = "ROOM-${(100..999).random()}"
            val room = VoiceRoomEntity(
                id = roomId,
                title = title,
                topic = topic,
                hostUserId = user.id,
                hostName = user.name,
                hostAvatar = user.photoUrl,
                isPrivate = isPrivate,
                passcode = passcode
            )
            repository.insertRoom(room)

            // Add host as participant
            repository.insertParticipant(
                RoomParticipantEntity(
                    roomId = roomId,
                    userId = user.id,
                    userName = user.name,
                    userAvatar = user.photoUrl,
                    role = "HOST",
                    micSeatIndex = 0
                )
            )

            _activeRoomId.value = roomId
            showToast("Voice Room '$title' created!")
            onSuccess()
        }
    }

    fun joinVoiceRoom(roomId: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val room = repository.getRoomById(roomId) ?: return@launch

            _activeRoomId.value = roomId
            repository.insertParticipant(
                RoomParticipantEntity(
                    roomId = roomId,
                    userId = user.id,
                    userName = user.name,
                    userAvatar = user.photoUrl,
                    role = if (room.hostUserId == user.id) "HOST" else "LISTENER"
                )
            )
            showToast("Joined ${room.title}")
        }
    }

    fun leaveVoiceRoom() {
        viewModelScope.launch {
            val roomId = _activeRoomId.value ?: return@launch
            val userId = _currentUserId.value ?: return@launch
            repository.removeParticipant(roomId, userId)
            _activeRoomId.value = null
            showToast("Left voice room")
        }
    }

    // Calling Actions
    fun startVoiceCall(targetUser: UserEntity) {
        val callId = "CALL-${System.currentTimeMillis()}"
        _activeCall.value = ActiveCallState(
            callId = callId,
            callType = "VOICE",
            otherUser = targetUser
        )
        // Log Call
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            repository.insertCall(
                CallHistoryEntity(
                    id = callId,
                    callType = "VOICE",
                    callerUserId = me.id,
                    callerName = me.name,
                    callerAvatar = me.photoUrl,
                    receiverUserId = targetUser.id,
                    receiverName = targetUser.name,
                    receiverAvatar = targetUser.photoUrl,
                    status = "COMPLETED"
                )
            )
        }
    }

    fun startVideoCall(targetUser: UserEntity) {
        val callId = "CALL-${System.currentTimeMillis()}"
        _activeCall.value = ActiveCallState(
            callId = callId,
            callType = "VIDEO",
            otherUser = targetUser
        )
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            repository.insertCall(
                CallHistoryEntity(
                    id = callId,
                    callType = "VIDEO",
                    callerUserId = me.id,
                    callerName = me.name,
                    callerAvatar = me.photoUrl,
                    receiverUserId = targetUser.id,
                    receiverName = targetUser.name,
                    receiverAvatar = targetUser.photoUrl,
                    status = "COMPLETED"
                )
            )
        }
    }

    fun toggleCallMic() {
        _activeCall.value = _activeCall.value?.let { it.copy(isMicMuted = !it.isMicMuted) }
    }

    fun toggleCallCamera() {
        _activeCall.value = _activeCall.value?.let { it.copy(isCameraOn = !it.isCameraOn) }
    }

    fun toggleCallSpeaker() {
        _activeCall.value = _activeCall.value?.let { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    fun endCall() {
        _activeCall.value = null
        showToast("Call ended")
    }

    // Gift Actions
    fun sendGiftToUser(targetUser: UserEntity, gift: GiftEntity, roomId: String = "") {
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            val success = repository.sendGift(me, targetUser.id, targetUser.name, gift, roomId)
            if (success) {
                showToast("Sent ${gift.name} ${gift.icon} to ${targetUser.name}!")
            } else {
                showToast("Insufficient coins! Buy more coins in Wallet.")
            }
        }
    }

    // Wallet Actions
    fun buyCoinPackage(amount: Int, priceLabel: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.buyCoins(user, amount, priceLabel)
            showToast("Successfully added $amount Coins!")
        }
    }

    // Photo Actions
    fun uploadPhoto(photoUrl: String, caption: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            val newPhoto = PhotoEntity(
                id = "PHOTO-${System.currentTimeMillis()}",
                userId = me.id,
                userName = me.name,
                userAvatar = me.photoUrl,
                photoUrl = photoUrl.ifEmpty { "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800" },
                caption = caption
            )
            repository.insertPhoto(newPhoto)
            showToast("Photo uploaded!")
            onSuccess()
        }
    }

    fun deletePhoto(photoId: String) {
        viewModelScope.launch {
            repository.deletePhoto(photoId)
            showToast("Photo deleted")
        }
    }

    fun toggleLikePhoto(photo: PhotoEntity) {
        viewModelScope.launch {
            val updated = photo.copy(
                likesCount = if (photo.isLikedByMe) photo.likesCount - 1 else photo.likesCount + 1,
                isLikedByMe = !photo.isLikedByMe
            )
            repository.insertPhoto(updated)
        }
    }

    fun updateUserProfile(name: String, photoUrl: String, coverUrl: String, bio: String, country: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUser(
                user.copy(
                    name = name,
                    photoUrl = photoUrl,
                    coverUrl = coverUrl,
                    bio = bio,
                    country = country
                )
            )
            showToast("Profile updated successfully!")
            onSuccess()
        }
    }

    // Admin Actions
    fun adminBanUser(userId: String) {
        viewModelScope.launch {
            val target = repository.getUserById(userId) ?: return@launch
            repository.updateUser(target.copy(isBanned = true, isOnline = false))
            showToast("User ${target.name} banned.")
        }
    }

    fun adminUnbanUser(userId: String) {
        viewModelScope.launch {
            val target = repository.getUserById(userId) ?: return@launch
            repository.updateUser(target.copy(isBanned = false))
            showToast("User ${target.name} unbanned.")
        }
    }

    fun adminVerifyUser(userId: String) {
        viewModelScope.launch {
            val target = repository.getUserById(userId) ?: return@launch
            repository.updateUser(target.copy(isVerified = !target.isVerified))
            showToast("User verification updated.")
        }
    }

    fun adminDeleteUser(userId: String) {
        viewModelScope.launch {
            repository.deleteUser(userId)
            showToast("User account deleted.")
        }
    }

    fun adminResolveReport(reportId: String, status: String) {
        viewModelScope.launch {
            repository.updateReportStatus(reportId, status)
            showToast("Report status updated to $status.")
        }
    }

    fun submitReport(targetId: String, targetName: String, targetType: String, reason: String) {
        viewModelScope.launch {
            val me = currentUser.value ?: return@launch
            repository.insertReport(
                ReportEntity(
                    id = "REP-${System.currentTimeMillis()}",
                    reporterUserId = me.id,
                    reporterName = me.name,
                    reportedTargetId = targetId,
                    reportedTargetName = targetName,
                    targetType = targetType,
                    reason = reason
                )
            )
            showToast("Report submitted to Admins. Thank you!")
        }
    }
}
