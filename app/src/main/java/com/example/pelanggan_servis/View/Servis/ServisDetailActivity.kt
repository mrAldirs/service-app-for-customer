package com.example.pelanggan_servis.View.Servis

import android.content.DialogInterface
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
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Riwayat.DetailTransaksiFragment
import com.example.pelanggan_servis.ViewModel.PembayaranViewModel
import com.example.pelanggan_servis.ViewModel.RiwayatViewModel
import com.example.pelanggan_servis.ViewModel.ServisViewModel
import com.example.pelanggan_servis.databinding.ServisDetailActivityBinding
import com.squareup.picasso.Picasso

class ServisDetailActivity : AppCompatActivity(), IntentHelper {
    private lateinit var binding: ServisDetailActivityBinding
    lateinit var servisViewModel: ServisViewModel
    private lateinit var rVM: RiwayatViewModel
    private lateinit var pVM: PembayaranViewModel
    var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ServisDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        servisViewModel = ViewModelProvider(this).get(ServisViewModel::class.java)
        rVM = ViewModelProvider(this).get(RiwayatViewModel::class.java)
        pVM = ViewModelProvider(this).get(PembayaranViewModel::class.java)

        binding.btnBatalkan.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda membatalkan transaksi ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    batalkan()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        detail(intent?.getStringExtra("kode").toString())
    }

    fun detail(kode: String) {
        servisViewModel.detail(kode).observe(this, Observer { detail ->
            binding.detailBarang.setText(detail.nama_barang)
            binding.detailJenisBarang.setText(detail.jenis_barang)
            binding.detailKode.setText(detail.kd_transaksi)
            binding.detailTglTransaksi.setText(detail.tgl_transaksi)
            binding.detailJenisTransaksi.setText(detail.jenis_transaksi)
            binding.detailKerusakan.setText(detail.kerusakan)
            Picasso.get().load(detail.img_barang).into(binding.detailImage)

            binding.btnChat.setOnClickListener {
                start(chatAdmin(detail.kd_transaksi, "Servis"))
            }

            if (detail.catatan.equals("null")) {
                binding.detailCatatan.setText("")
            } else {
                binding.detailCatatan.setText(detail.catatan)
            }

            if (detail.tgl_servis.equals("null")) {
                binding.detailTanggalServis.setText("Belum dikonfirmasi oleh admin")
            } else {
                binding.detailTanggalServis.setText(detail.tgl_servis)
            }

            binding.btnBeriRating.setOnClickListener {
                var frag = ServisRatingFragment()

                val bundle = Bundle()
                bundle.putString("kode", kode)
                bundle.putString("kode_mk", detail.kd_mekanik)
                frag.arguments = bundle

                frag.show(supportFragmentManager, "")
            }

            if (detail.status_transaksi.equals("Selesai")) {
                binding.detailStatusTransaksi.setText(detail.status_transaksi)
                detailBayar(detail.kd_transaksi)
                binding.btnBeriRating.visibility = View.VISIBLE
                binding.btnBatalkan.visibility = View.GONE
                binding.detailNamaMekanik.setText(detail.nama_mekanik)
                Picasso.get().load(detail.img_mekanik).into(binding.detailFotoMekanik)
                binding.ratingBar.rating = detail.rating.toFloat()
                binding.tvRating.setText(detail.rating)
            } else if (detail.status_transaksi.equals("Proses")) {
                binding.detailStatusTransaksi.setText("Telah Dikonfirmasi")
                binding.btnBeriRating.visibility = View.GONE
                binding.cdPembayaran.visibility = View.GONE
                binding.btnBatalkan.visibility = View.GONE
                binding.detailNamaMekanik.setText(detail.nama_mekanik)
                binding.ratingBar.rating = detail.rating.toFloat()
                binding.tvRating.setText(detail.rating)
                Picasso.get().load(detail.img_mekanik).into(binding.detailFotoMekanik)
            } else if (detail.status_transaksi.equals("Belum")) {
                binding.detailStatusTransaksi.setText("Belum Dikonfirmasi oleh admin")
                binding.btnBeriRating.visibility = View.GONE
                binding.cdPembayaran.visibility = View.GONE
                binding.cdMekanik.visibility = View.GONE
            } else if (detail.status_transaksi.equals("Tolak")) {
                binding.detailStatusTransaksi.setText("Ditolak oleh Admin")
                binding.btnBeriRating.visibility = View.GONE
                binding.cdPembayaran.visibility = View.GONE
                binding.cdMekanik.visibility = View.GONE
                binding.btnBatalkan.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun detailBayar(kode: String) {
        rVM.detailTransaksi(kode).observe(this@ServisDetailActivity, Observer { detail ->
            binding.detailTotal.text = CurrencyHelper.formatCurrency(detail.total_bayar.toInt())
            binding.detailHarga.text = CurrencyHelper.formatCurrency(detail.bayar.toInt())
            binding.detailAdmin.text = CurrencyHelper.formatCurrency(detail.biaya_admin.toInt())
            binding.detailOngkir.text = CurrencyHelper.formatCurrency(detail.ongkir.toInt())
        })
    }

    private fun batalkan() {
        rVM.batalkan(intent?.getStringExtra("kode").toString()).observe(this, Observer { result ->
            Toast.makeText(this,"Berhasil membatalkan transaksi!", Toast.LENGTH_LONG).show()
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })
    }
}