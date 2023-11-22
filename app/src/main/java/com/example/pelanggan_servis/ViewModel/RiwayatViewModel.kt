package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pelanggan_servis.Model.DetailTransaksiModel
import com.example.pelanggan_servis.Model.RiwayatModel
import com.example.pelanggan_servis.Repository.RiwayatRepository

class RiwayatViewModel : AndroidViewModel {

    private val riwayatRepository: RiwayatRepository

    constructor(application: Application) : super(application) {
        riwayatRepository = RiwayatRepository(application)
    }

    fun loadData(mode: String, kd_u: String) : LiveData<List<RiwayatModel>> {
        return riwayatRepository.loadData(mode, kd_u)
    }

    fun detail(kd_t: String, kd_u: String): LiveData<RiwayatModel> {
        return riwayatRepository.detail(kd_t, kd_u)
    }

    fun batalkan(kd: String): LiveData<Boolean> {
        return riwayatRepository.batalkan(kd)
    }

    fun detailTransaksi(kd:String): LiveData<DetailTransaksiModel> {
        return riwayatRepository.detailTransaksi(kd)
    }
}