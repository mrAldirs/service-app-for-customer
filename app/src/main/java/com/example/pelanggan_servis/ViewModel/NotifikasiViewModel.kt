package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.NotifikasiModel
import com.example.pelanggan_servis.Repository.NotifikasiRepository

class NotifikasiViewModel : AndroidViewModel {

    private val notifikasiRepository : NotifikasiRepository

    constructor(application: Application) : super(application) {
        notifikasiRepository = NotifikasiRepository(application)
    }

    fun loadDataPesan(kd: String) : LiveData<List<NotifikasiModel>> {
        return notifikasiRepository.loadDataPesan(kd)
    }

    fun loadDataInformasi() : LiveData<List<NotifikasiModel>> {
        return notifikasiRepository.loadDataInformasi()
    }

    fun delete(kd:String) : LiveData<Boolean> {
        return notifikasiRepository.delete(kd)
    }
}