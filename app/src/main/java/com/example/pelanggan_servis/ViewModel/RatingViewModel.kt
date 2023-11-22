package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.RatingModel
import com.example.pelanggan_servis.Repository.RatingRepository

class RatingViewModel : AndroidViewModel {

    private val ratingRepository: RatingRepository

    constructor(application: Application) : super(application) {
        ratingRepository = RatingRepository(application)
    }

    fun insert(ratingModel: RatingModel) : LiveData<Boolean> {
        return ratingRepository.insert(ratingModel)
    }
}