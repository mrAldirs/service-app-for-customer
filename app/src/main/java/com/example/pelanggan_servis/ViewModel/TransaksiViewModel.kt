package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pelanggan_servis.Model.JualModel
import com.example.pelanggan_servis.Model.OrderModel
import com.example.pelanggan_servis.Model.TransaksiModel
import com.example.pelanggan_servis.Repository.TransaksiRepository

class TransaksiViewModel : AndroidViewModel {

    private val transaksiRepository: TransaksiRepository

    constructor(application: Application) : super(application) {
        transaksiRepository = TransaksiRepository(application)
    }

    fun insertJual(jual: JualModel, nmFile: String) : LiveData<Boolean> {
        return transaksiRepository.insertJual(jual, nmFile)
    }

    fun insertOrder(order: OrderModel) : LiveData<Boolean> {
        return transaksiRepository.insertOrder(order)
    }

    fun getDetailOrder(mode: String, kdUser: String): LiveData<Pair<TransaksiModel?, String?>> {
        val result = MutableLiveData<Pair<TransaksiModel?, String?>>()
        transaksiRepository.getDetailOrder(mode, kdUser) { detailOrder, error ->
            result.value = Pair(detailOrder, error)
        }
        return result
    }

    fun deleteOrder(kdB: String, kdT: String): LiveData<Boolean> {
        return transaksiRepository.deleteOrder(kdB, kdT)
    }
}