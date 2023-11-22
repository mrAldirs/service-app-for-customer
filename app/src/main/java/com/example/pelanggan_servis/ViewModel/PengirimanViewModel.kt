package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.PengirimanModel
import com.example.pelanggan_servis.Repository.PengirimanRepository

class PengirimanViewModel : AndroidViewModel {

    private val pengirimanRepository: PengirimanRepository

    constructor(application: Application) : super(application) {
        pengirimanRepository = PengirimanRepository(application)
    }

    fun detail(kd: String): LiveData<PengirimanModel> {
        return pengirimanRepository.detail(kd)
    }
}