package com.example.pelanggan_servis.Model

data class PengirimanModel(
    val kd_jadwal: String,
    val tglkirim_jadwal: String,
    val nama_pengirim: String,
    val tglsampai_jadwal: String,
    val bukti_kirim: String
)