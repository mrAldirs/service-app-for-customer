package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.ServisDetailModel
import com.example.pelanggan_servis.Model.ServisModel
import com.example.pelanggan_servis.Repository.ServisRepository

class ServisViewModel : AndroidViewModel {

    private val servisRepository: ServisRepository

    constructor(application: Application) : super(application) {
        servisRepository = ServisRepository(application)
    }

    fun insert(servis: ServisModel, nmFile: String) : LiveData<Boolean> {
        return servisRepository.insert(servis, nmFile)
    }

    fun detail(kd: String) : LiveData<ServisDetailModel> {
        return servisRepository.detail(kd)
    }
}