package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.UserModel
import com.example.pelanggan_servis.Repository.UserRepository

class UserViewModel : AndroidViewModel {

    private val userRepository: UserRepository

    constructor(application: Application) : super(application) {
        userRepository = UserRepository(application)
    }

    fun getProfil(mode: String, kd:String): LiveData<UserModel> {
        return userRepository.getProfil(mode, kd)
    }

    fun edit(userModel: UserModel): LiveData<Boolean> {
        return userRepository.edit(userModel)
    }
}