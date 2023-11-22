package com.example.pelanggan_servis.Model

data class PembayaranModel(
    val kd_transaksi : String,
    val rek_payment : String,
    val tgl_pembayaran : String,
    val bayar : String,
    val biaya_admin : String,
    val ongkir : String,
    val total_bayar : String,
    val bukti_pembayaran : String,
    val status_pembayaran : String
)
