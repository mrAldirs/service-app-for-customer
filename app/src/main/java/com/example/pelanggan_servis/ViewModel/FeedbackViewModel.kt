package com.example.pelanggan_servis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pelanggan_servis.Model.FeedbackModel
import com.example.pelanggan_servis.Repository.FeedbackRepository

class FeedbackViewModel : AndroidViewModel {

    private val feedbackRepository : FeedbackRepository

    constructor(application: Application) : super(application) {
        feedbackRepository = FeedbackRepository(application)
    }

    fun loadData() : LiveData<List<FeedbackModel>> {
        return feedbackRepository.loadData()
    }

    fun delete(kd: String) : LiveData<Boolean> {
        return feedbackRepository.delete(kd)
    }

    fun insert(mode: String, feedback: FeedbackModel) : LiveData<Boolean> {
        return feedbackRepository.insert(mode, feedback)
    }
}