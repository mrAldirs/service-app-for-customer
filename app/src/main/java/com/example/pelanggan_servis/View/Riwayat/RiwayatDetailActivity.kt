package com.example.pelanggan_servis.View.Riwayat

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.Pembayaran.FragmentPembayaranKonfirmasi
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.PembayaranViewModel
import com.example.pelanggan_servis.ViewModel.PengirimanViewModel
import com.example.pelanggan_servis.ViewModel.RiwayatViewModel
import com.example.pelanggan_servis.databinding.RiwayatDetailActivityBinding
import com.squareup.picasso.Picasso

class RiwayatDetailActivity : AppCompatActivity(), IntentHelper {
    private lateinit var b: RiwayatDetailActivityBinding
    private lateinit var rVM: RiwayatViewModel
    private lateinit var pVM: PembayaranViewModel
    private lateinit var kVM: PengirimanViewModel
    lateinit var preferences: SharedPreferencesHelper
    var jns = ""
    var kode = ""
    var imgUrl = ""
    var imgKirim = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = RiwayatDetailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = SharedPreferencesHelper(this)

        kVM = ViewModelProvider(this).get(PengirimanViewModel::class.java)
        rVM = ViewModelProvider(this).get(RiwayatViewModel::class.java)
        pVM = ViewModelProvider(this).get(PembayaranViewModel::class.java)

        b.btnBuktiPembayaran.setOnClickListener {
            start(imageDetail(imgUrl))
        }

        b.btnBatalkan.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda membatalkan transaksi ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    batalkan(kode)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }

        b.btnDetailTransaksi.setOnClickListener {
            var frag = DetailTransaksiFragment()

            val bundle = Bundle()
            bundle.putString("kode", kode)
            frag.arguments = bundle

            frag.show(supportFragmentManager, "FragmentPembayaranKonfirmasi")
        }

        b.btnDetailPengiriman.setOnClickListener {
            start(imageDetail(imgKirim))
        }
    }

    override fun onStart() {
        super.onStart()
        detail()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun detail() {
        var paket : Bundle? = intent.extras
        var kd_t = paket?.getString("kdT").toString()
        var kd_u = preferences.getString("user", "").toString()
        rVM.detail(kd_t, kd_u).observe(this, Observer { riwayat ->
            kode = riwayat.kd_transaksi
            b.detailKode.setText(riwayat.kd_transaksi)
            b.detailTgl.setText(riwayat.tgl_transaksi)
            b.detailNama.setText(riwayat.nama)
            b.detailAlamat.setText(riwayat.alamat)
            b.detailEmail.setText(riwayat.email)
            b.detailNoHp.setText(riwayat.no_hp)
            b.detailNamaBarang.setText(riwayat.nama_barang)
            b.detailJenisBarang.setText(riwayat.jenis_barang)
            Picasso.get().load(riwayat.img_barang).into(b.detailImage)
            jns = riwayat.jenis_transaksi

            var topik = ""
            b.btnChat.setOnClickListener {
                if (riwayat.jenis_transaksi.equals("Beli")) {
                    topik = "Tawar Menawar"
                } else if (riwayat.jenis_transaksi.equals("Jual")) {
                    topik = "Transaksi"
                }
                start(chatAdmin(riwayat.kd_transaksi, topik))
            }

            if (riwayat.jenis_transaksi.equals("Jual")) {
                b.detailJenisTransaksi.setText("Pembelian Barang")
            } else if (riwayat.jenis_transaksi.equals("Beli")) {
                b.detailJenisTransaksi.setText("Jual Barang")
                b.btnChat.setText("Tawar Menawar")
            } else {
                b.detailJenisTransaksi.setText(riwayat.jenis_transaksi)
            }

            if (riwayat.status_transaksi.equals("Belum")) {
                b.detailStatusTran.setText("Belum dikonfirmasi")
                b.detailStatusTran.setTextColor(Color.RED)
                if (riwayat.jenis_transaksi.equals("Jual") || riwayat.jenis_transaksi.equals("Beli")) {
                    b.cardPembayran.visibility = View.VISIBLE
                    b.btnDetailTransaksi.visibility = View.VISIBLE
                    detailBayar()
                } else {
                    if (riwayat.jenis_transaksi.equals("Order")) {
                        b.ft.visibility = View.GONE
                    }
                    b.btnDetailTransaksi.visibility = View.GONE
                    b.cardPembayran.visibility = View.GONE
                }
                b.cardPengiriman.visibility = View.GONE
            } else if (riwayat.status_transaksi.equals("Ditolak")) {
                b.detailStatusTran.setText("Dibatalkan")
                b.detailStatusTran.setTextColor(Color.RED)
                b.btnBatalkan.visibility = View.GONE
                b.cardPengiriman.visibility = View.GONE
                b.btnDetailTransaksi.visibility = View.GONE
            } else if (riwayat.status_transaksi.equals("Proses")) {
                b.detailStatusTran.setText("Proses")
                b.detailStatusTran.setTextColor(Color.BLUE)
                if (riwayat.jenis_transaksi.equals("Servis")) {
                    b.btnBatalkan.visibility = View.GONE
                    b.cardPembayran.visibility = View.GONE
                    b.btnDetailTransaksi.visibility = View.GONE
                } else if (riwayat.jenis_transaksi.equals("Order")) {
                    b.btnBatalkan.visibility = View.VISIBLE
                    b.cardPembayran.visibility = View.GONE
                } else {
                    b.btnBatalkan.visibility = View.GONE
                    detailBayar()
                }
                b.cardPengiriman.visibility = View.GONE
            } else if (riwayat.status_transaksi.equals("Selesai")) {
                b.detailStatusTran.setText(riwayat.status_transaksi)
                b.detailStatusTran.setTextColor(Color.GREEN)
                detailBayar()
                b.btnBatalkan.visibility = View.GONE
            }
        })
    }

    private fun detailBayar() {
        var paket : Bundle? = intent.extras
        var kd_t = paket?.getString("kdT").toString()
        pVM.detail(kd_t).observe(this, Observer { bayar ->
            imgUrl = bayar.bukti_pembayaran
            b.detailTotalBayar.text = CurrencyHelper.formatCurrency(bayar.total_bayar.toInt())
            if (bayar.rek_payment.equals("COD")) {
                b.detailPayment.setText(bayar.rek_payment)
            } else {
                b.detailPayment.setText("Transfer "+bayar.rek_payment)
            }

            if (bayar.status_pembayaran.equals("Belum")) {
                b.detailStatusBayar.setText("Belum melakukan pembayaran")
                b.cardPengiriman.visibility = View.GONE
            } else if (bayar.status_pembayaran.equals("Selesai")) {
                b.detailStatusBayar.setText("Sudah Membayar")
                b.btnBuktiPembayaran.visibility = View.VISIBLE
                if (jns.equals("Order") || jns.equals("Jual")) {
                    b.cardPengiriman.visibility = View.VISIBLE
                    detailKirim(bayar.kd_transaksi)
                } else {
                    b.cardPengiriman.visibility = View.GONE
                }
            }
        })
    }

    private fun detailKirim(kd: String) {
        kVM.detail(kd).observe(this, Observer { kirim ->
            if (kirim.nama_pengirim.equals("null") && kirim.tglkirim_jadwal.equals("null")) {
                b.detailNamaPengirim.setText("menunggu konfirmasi admin")
                b.detailTglKirimJadwal.setText("menunggu konfirmasi admin")
            } else {
                b.detailNamaPengirim.setText(kirim.nama_pengirim)
                b.detailTglKirimJadwal.setText(kirim.tglkirim_jadwal)
            }

            var sts = kirim.tglsampai_jadwal
            if (sts.equals("null") || sts.equals("")) {
                b.statusPengiriman.setText("Barang dalam proses pengiriman oleh Admin")
                b.btnDetailPengiriman.visibility = View.GONE
                imgKirim = kirim.bukti_kirim
            } else {
                b.statusPengiriman.setText("Barang telah dikirim ke lokasi oleh Admin")
            }
        })
    }

    fun batalkan(kd: String) {
        rVM.batalkan(kd).observe(this, Observer { result ->
            Toast.makeText(this,"Berhasil membatalkan transaksi!", Toast.LENGTH_LONG).show()
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}