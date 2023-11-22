package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.ChatModel
import com.example.pelanggan_servis.Repository.ChatRepository

class ChatViewModel : AndroidViewModel {

    private val chatRepository : ChatRepository

    constructor(application: Application) : super(application) {
        chatRepository = ChatRepository(application)
    }

    fun loadData(topik: String, kdParent: String): LiveData<List<ChatModel>> {
        return chatRepository.loadData(topik, kdParent)
    }

    fun insert(chat: ChatModel, nmFile: String): LiveData<Boolean> {
        return chatRepository.insert(chat, nmFile)
    }

    fun delete(kd: String): LiveData<Boolean> {
        return chatRepository.delete(kd)
    }
}