package com.example.wificontrol.screens.support

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wificontrol.components.CHAT_COLLECTIONS
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class ChatSupportViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val chatCollection = firestore.collection(CHAT_COLLECTIONS)
    private var messagesListener: ListenerRegistration? = null
    private val _messages = MutableStateFlow<List<MessageData>>(emptyList())
    val messages: StateFlow<List<MessageData>> = _messages
    private var isListening = false
    private var isChatChecked = false

    private val _chatId = MutableStateFlow("")
    val chatId: StateFlow<String> = _chatId

    private val _chatTitle = MutableStateFlow("Чат с поддержкой")
    val chatTitle: StateFlow<String> = _chatTitle

    private val _activeChatId = MutableStateFlow<String?>(null)

    private val _isChatScreenActive = MutableStateFlow(false)

    private val _chats = MutableStateFlow<List<ChatData>>(emptyList())
    val chats: StateFlow<List<ChatData>> = _chats

    init {
        checkOrCreateChat()
        loadUserChats()
        viewModelScope.launch {
            chatId.collect { id ->
                if (id.isNotEmpty()) {
                    listenForMessages()
                }
            }
        }
    }

    private fun checkOrCreateChat() {
        if (isChatChecked) return
        isChatChecked = true
        val userEmail = auth.currentUser?.email ?: return
        val hostEmail = "support@mail.ru"
        if (_chatId.value.isNotEmpty()) return
        chatCollection
            .whereArrayContains("participants", userEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val chat = querySnapshot.documents.find { doc ->
                    val participants = doc.get("participants") as? List<*>
                    participants?.contains(hostEmail) == true
                }
                if (chat != null) {
                    _chatId.value = chat.id
                    val participants = chat.get("participants") as? List<String>
                    val chatEmail = participants?.getOrNull(0) ?: "Тех. поддержка"
                    _chatTitle.value = chatEmail
                    listenForMessages()
                } else {
                    createChat(userEmail, hostEmail)
                }
            }
    }

    private fun createChat(userEmailRaw: String, hostEmailRaw: String) {
        val userEmail = userEmailRaw.replace(" ", "").replace("\n", "")
        val hostEmail = hostEmailRaw.replace(" ", "").replace("\n", "")
        val chatId = "$userEmail-$hostEmail"

        val newChat = hashMapOf(
            "chatId" to chatId,
            "participants" to listOf(userEmail, hostEmail),
            "lastMessage" to null
        )

        chatCollection.document(chatId).set(newChat)
            .addOnSuccessListener {
                _chatId.value = chatId
                _chatTitle.value = hostEmail
                listenForMessages()
            }
    }

    fun sendMessage(content: String) {
        val chatIdToSend = _activeChatId.value ?: _chatId.value
        if (_chatId.value.isEmpty()) {
            Log.e("ChatSupport", "Chat ID пуст. Нельзя отправить сообщение.")
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val message = MessageData(
            msgId = UUID.randomUUID().toString(),
            senderId = userId,
            content = content,
            time = Timestamp.now(),
            status = MessageStatus.DELIVERED
        )

        Log.d("ChatSupport", "Отправка сообщения: $content в чат ${_chatId.value}")

        chatCollection.document(chatIdToSend)
            .collection("messages")
            .document(message.msgId)
            .set(message)
            .addOnSuccessListener {
                chatCollection.document(_chatId.value)
                    .collection("messages")
                    .document(message.msgId)
                    .update("status", MessageStatus.DELIVERED.name)
            }
            .addOnFailureListener { e ->
                Log.e("ChatSupport", "Ошибка отправки сообщения", e)
            }
    }

    fun deleteMessages(messageIds: List<String>) {
        if (_chatId.value.isEmpty()) {
            Log.e("ChatSupport", "Chat ID пуст. Нельзя удалить сообщения.")
            return
        }

        val batch = firestore.batch()
        messageIds.forEach { msgId ->
            val messageRef = chatCollection.document(_chatId.value)
                .collection("messages").document(msgId)
            batch.delete(messageRef)
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d("ChatSupport", "Сообщения удалены: $messageIds")
            }
            .addOnFailureListener { e ->
                Log.e("ChatSupport", "Ошибка удаления сообщений", e)
            }
    }

    fun setChatId(id: String) {
        if (id.isEmpty()) {
            Log.e("ChatSupport", "Некорректный chatId: $id")
            return
        }
        if (_chatId.value == id) return
        Log.d("ChatSupport", "Старый chatId: ${_chatId.value}, Новый chatId: $id")
        _chatId.value = id
        _activeChatId.value = id
        Log.d("ChatSupport", "Chat ID установлен: $id")
        isListening = false
        listenForMessages()

        val chat = _chats.value.find { it.chatId == id }
        if (chat != null) {
            updateChatTitle(chat)
        } else {
            _chatTitle.value = "Загрузка..."
        }
    }

    fun setChatScreenActive(isActive: Boolean = false) {
        _isChatScreenActive.value = isActive
    }

    private fun updateChatTitle(chat: ChatData) {
        val userEmail = auth.currentUser?.email
        val interlocutor = chat.participants.find { it != userEmail }
        _chatTitle.value = interlocutor ?: "Чат"
    }

    fun markMessagesAsRead() {
        Log.d(
            "ChatSupport",
            "markMessagesAsRead вызван, isChatScreenActive: ${_isChatScreenActive.value}"
        )
        if (!_isChatScreenActive.value) {
            Log.d("ChatSupport", "Экран не активен, пропускаем обновление")
            return
        }
        if (_chatId.value.isEmpty()) {
            Log.e("ChatSupport", "Chat ID пуст. Нельзя обновить статус сообщений.")
            return
        }

        chatCollection.document(_chatId.value)
            .collection("messages")
            .whereEqualTo("status", MessageStatus.DELIVERED.name)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    doc.reference.update("status", MessageStatus.READ.name)
                        .addOnSuccessListener {
                            Log.d("ChatSupport", "Статус сообщения обновлён на READ")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatSupport", "Ошибка обновления статуса на READ", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ChatSupport", "Ошибка при получении сообщений для обновления", e)
            }
    }


    private fun listenForMessages() {
        if (isListening) return
        isListening = true
        viewModelScope.launch {
            val chatIdValue = chatId.first { it.isNotEmpty() }
            messagesListener?.remove()
            Log.d("ChatSupport", "Начинаем слушать сообщения для чата: $chatIdValue")
            messagesListener = chatCollection.document(chatIdValue).collection("messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("ChatSupport", "Ошибка прослушивания сообщений", error)
                        return@addSnapshotListener
                    }
                    snapshot?.let {
                        val messagesList = it.documents.mapNotNull { doc ->
                            doc.toObject(MessageData::class.java)
                        }
                        _messages.value = messagesList
                        Log.d("ChatSupport", "Messages updated: ${messagesList.map { it.content }}")
                    }
                }
        }
    }

    fun loadUserChats() {
        val userEmail = auth.currentUser?.email ?: return
        val currentUserId = auth.currentUser?.uid ?: return
        chatCollection
            .whereArrayContains("participants", userEmail)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatSupport", "Ошибка загрузки чатов", error)
                    return@addSnapshotListener
                }

                snapshot?.let { chatSnapshot ->
                    val chatList = mutableListOf<ChatData>()

                    chatSnapshot.documents.forEach { chatDoc ->
                        val chatId = chatDoc.id
                        val participants =
                            chatDoc.get("participants") as? List<String> ?: emptyList()
                        chatCollection.document(chatId).collection("messages")
                            .whereEqualTo("status", MessageStatus.DELIVERED.name)
                            .addSnapshotListener { messagesSnapshot, messagesError ->
                                if (messagesError != null) {
                                    Log.e("ChatSupport", "Ошибка загрузки сообщений", messagesError)
                                    return@addSnapshotListener
                                }
                                val unreadMessages = messagesSnapshot?.documents?.filter { doc ->
                                    val message = doc.toObject(MessageData::class.java)
                                    message?.senderId != currentUserId
                                }
                                val unreadCount = unreadMessages?.size ?: 0

                                val existingChat = chatList.find { it.chatId == chatId }

                                if (existingChat != null) {
                                    chatList[chatList.indexOf(existingChat)] =
                                        existingChat.copy(unreadCount = unreadCount)
                                } else {
                                    chatList.add(ChatData(chatId, participants, unreadCount))
                                }
                                _chats.value = chatList.toList()
                            }
                    }
                }
            }
    }

    fun markSpecificMessagesAsRead(messageIds: List<String>) {
        messageIds.forEach { msgId ->
            chatCollection.document(_chatId.value)
                .collection("messages")
                .document(msgId)
                .update("status", "READ")
                .addOnSuccessListener {
                    Log.d("ChatSupport", "Сообщение $msgId отмечено как прочитанное")
                }
                .addOnFailureListener { e ->
                    Log.e("ChatSupport", "Ошибка: $e")
                }
        }
    }

    fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    override fun onCleared() {
        super.onCleared()
        messagesListener?.remove()
        isListening = false
    }
}


