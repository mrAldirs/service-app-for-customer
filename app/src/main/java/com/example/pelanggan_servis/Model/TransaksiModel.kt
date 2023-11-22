package com.example.pelanggan_servis.Model

data class TransaksiModel(
    val kd_user : String,
    val nama : String,
    val alamat : String,
    val email : String,
    val no_hp : String,
    val kd_barang : String,
    val nama_barang : String,
    val jenis_barang : String,
    val img_barang : String,
    val ket_barang : String,
    val kd_transaksi : String,
    val jenis_transaksi : String,
    val tgl_transaksi : String,
    val status_transaksi : String,
    val catatan_transaksi : String
)
