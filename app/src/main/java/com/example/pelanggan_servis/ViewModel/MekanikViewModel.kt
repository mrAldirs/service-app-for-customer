package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pelanggan_servis.Model.MekanikModel
import com.example.pelanggan_servis.Repository.MekanikRepository

class MekanikViewModel  : AndroidViewModel {

    private val mekanikRepository : MekanikRepository

    constructor(application: Application) : super(application) {
        mekanikRepository = MekanikRepository(application)
    }

    fun loadData(nm: String) : LiveData<List<MekanikModel>> {
        return mekanikRepository.loadData(nm)
    }
}