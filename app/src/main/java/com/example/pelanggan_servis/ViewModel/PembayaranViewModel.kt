package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.PembayaranModel
import com.example.pelanggan_servis.Repository.PembayaranRepository

class PembayaranViewModel : AndroidViewModel {

    private val pembayaranRepository: PembayaranRepository

    constructor(application: Application) : super(application) {
        pembayaranRepository = PembayaranRepository(application)
    }

    fun detail(kd_t: String): LiveData<PembayaranModel> {
        return pembayaranRepository.detail(kd_t)
    }
}