package com.example.pelanggan_servis.Model

data class DetailTransaksiModel (
    val nama_barang: String,
    val jenis_barang: String,
    val img_barang: String,
    val kd_transaksi: String,
    val jenis_transaksi: String,
    val status_bayar: String,
    val catatan_transaksi: String,
    val bayar: String,
    val biaya_admin: String,
    val ongkir: String,
    val total_bayar: String
)