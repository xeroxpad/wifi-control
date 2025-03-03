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

    private val _activeChatId = MutableStateFlow<String?>(null)

    private val _isChatScreenActive = MutableStateFlow(false)


    init {
        checkOrCreateChat()
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
                listenForMessages()
            }
    }

    fun sendMessage(content: String) {
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

        chatCollection.document(_chatId.value)
            .collection("messages")
            .document(message.msgId)
            .set(message)
            .addOnSuccessListener { documentRef ->
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
    }

    fun setChatScreenActive(isActive: Boolean = false) {
        _isChatScreenActive.value = isActive
    }

    fun markMessagesAsRead() {
        Log.d("ChatSupport", "markMessagesAsRead вызван, isChatScreenActive: ${_isChatScreenActive.value}")
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


    fun listenForMessages() {
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
                        if (_isChatScreenActive.value) {
                            markMessagesAsRead()
                        }
                    }
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


