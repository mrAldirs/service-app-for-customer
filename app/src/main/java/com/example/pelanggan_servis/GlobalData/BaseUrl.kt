package com.example.pelanggan_servis.GlobalData

object BaseUrl {

    // url local
    val local = "http://192.168.137.1/api_service"
//    var local = "https://rukinservice.000webhostapp.com/"

    // url crud_user
    val urlUser = "$local/pelanggan/pelanggan_crud_user.php"

    // url crud_user
    val urlAkun = "$local/pelanggan/pelanggan_crud_akun.php"

    // url validasi
    val validasi = "$local/login.php"

    // url crud_barang
    val urlJualBeli = "$local/pelanggan/pelanggan_crud_jualbeli.php"

    // url crud_servis
    val urlServis = "$local/pelanggan/pelanggan_crud_servis.php"

    // url crud_notifikasi
    val urlNotifikasi = "$local/pelanggan/pelanggan_crud_notifikasi.php"

    // url crud_riwayat
    val urlRiwayat = "$local/pelanggan/pelanggan_crud_riwayat.php"

    // url crud_feedback
    val urlFeedback = "$local/pelanggan/pelanggan_crud_feedback.php"

    // url crud_katalog
    val urlKatalog = "$local/pelanggan/pelanggan_crud_katalog.php"

    // url crud_pembayaran
    var urlPembayaran = "$local/pelanggan/pelanggan_crud_pembayaran.php"

    // url crud_pengiriman
    var urlKirim = "$local/pelanggan/pelanggan_crud_pengiriman.php"

    // url crud_mekanik
    var urlMekanik = "$local/pelanggan/pelanggan_crud_mekanik.php"

    // url crud_chat
    var urlChat = "$local/chat.php"
}