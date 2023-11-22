package com.example.pelanggan_servis

import com.example.pelanggan_servis.GlobalData.BaseUrl

class UrlClass {

    // url local
    var local = "http://192.168.137.1/api_service/"
//    var local = "https://rukinservice.000webhostapp.com/"

    // url crud_user
    var urlUser = local+"pelanggan/pelanggan_crud_user.php"

    // url crud_user
    var urlAkun = local+"pelanggan/pelanggan_crud_akun.php"

    // url validasi
    var validasi = local+"login.php"

    // url crud_barang
    var urlJualBeli = local+"pelanggan/pelanggan_crud_jualbeli.php"

    // url crud_servis
    var urlServis = local+"pelanggan/pelanggan_crud_servis.php"

    // url crud_notifikasi
    var urlNotifikasi = local+"pelanggan/pelanggan_crud_notifikasi.php"

    // url crud_riwayat
    var urlRiwayat = local+"pelanggan/pelanggan_crud_riwayat.php"

    // url crud_feedback
    var urlFeedback = local+"pelanggan/pelanggan_crud_feedback.php"

    // url crud_katalog
    var urlKatalog = local+"pelanggan/pelanggan_crud_katalog.php"

    // url crud_pembayaran
    var urlPembayaran = local+"pelanggan/pelanggan_crud_pembayaran.php"

    // url ongkir
    var urlOngkir = local+"pelanggan/ongkir_beli.php"

    // url crud_mekanik
    var urlMekanik = local +"pelanggan/pelanggan_crud_mekanik.php"

}