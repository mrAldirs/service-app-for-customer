package com.example.pelanggan_servis.Model

data class ServisDetailModel(
    val nama_barang: String,
    val jenis_barang: String,
    val img_barang: String,
    val kd_mekanik: String,
    val nama_mekanik: String,
    val rating: String,
    val img_mekanik: String,
    val kd_transaksi: String,
    val tgl_transaksi: String,
    val jenis_transaksi: String,
    val status_transaksi: String,
    val catatan: String,
    val tgl_servis: String,
    val kerusakan: String
)