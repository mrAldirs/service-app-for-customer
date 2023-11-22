package com.example.pelanggan_servis.Beli

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.databinding.BeliBayarFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class BeliBayarFragment : BottomSheetDialogFragment() {
    private lateinit var b: BeliBayarFragmentBinding
    lateinit var v : View

    lateinit var urlClass : UrlClass
    var kd = ""
    var bHarga : Int = 0
    var bAdmin : Int = 0
    var bOngkir : Int = 0
    var bTotal : Int = 0

    val pembayaranSp = arrayOf("--Pilih Metode Pembayaran--","BCA","BRI","LinkAja","OVO","ShopeePay")
    lateinit var adapterPembayaran: ArrayAdapter<String>
    lateinit var preferences: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BeliBayarFragmentBinding.inflate(layoutInflater)
        v = b.root

        preferences = SharedPreferencesHelper(v.context)
        urlClass = UrlClass()

        val bundle = arguments
        kd = bundle?.get("kode").toString()

        adapterPembayaran = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,pembayaranSp)
        b.beliBayarPembayaran.adapter = adapterPembayaran

        b.btnBatalkan.setOnClickListener {dismiss()}

        b.btnTutup.setOnClickListener {dismiss()}


        b.btnKirimBayarBeli.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Informasi!!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda Ingin melakukan Chekout barang ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    dismiss()
                    insert("bayar_beli")
                    Toast.makeText(this.context,"Berhasil melakukan pemesanan barang!", Toast.LENGTH_LONG).show()
                    requireActivity().recreate()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this.context,"Anda Membatalkan Pengiriman Pembelian", Toast.LENGTH_LONG).show()
                })
                .show()
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        detailKatalog("detail_katalog")
    }

    fun ongkir(kdB: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlOngkir,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val ongkir = jsonObject.getString("ongkir").toInt()
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("jenis_barang")
                val st5 = jsonObject.getString("harga_katalog").toInt()

                b.beliBayarOngkir.text = CurrencyHelper.formatCurrency(ongkir)
                bOngkir = ongkir

                if (st4.equals("Televisi")) {
                    bAdmin = 12500
                    b.beliBayarBiayaAdmin.text = CurrencyHelper.formatCurrency(bAdmin)
                } else if (st4.equals("AC")) {
                    bAdmin = 15000
                    b.beliBayarBiayaAdmin.text = CurrencyHelper.formatCurrency(bAdmin)
                } else if (st4.equals("Handphone")) {
                    bAdmin = 5000
                    b.beliBayarBiayaAdmin.text = CurrencyHelper.formatCurrency(bAdmin)
                } else if (st4.equals("Laptop")) {
                    bAdmin = 12000
                    b.beliBayarBiayaAdmin.text = CurrencyHelper.formatCurrency(bAdmin)
                }

                val harga = st5
                val admin = bAdmin
                val ongk = ongkir
                val result = harga + admin + ongk
                b.beliBayarTotalBarang.text = CurrencyHelper.formatCurrency(result)
                bTotal = result

                b.beliBayarAlamat.setText(st3)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()

                hm.put("kd_user", preferences.getString("user", ""))
                hm.put("kd_barang", kdB)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun detailKatalog(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKatalog,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st3 = jsonObject.getString("nama_barang")
                val st4 = jsonObject.getString("jenis_barang")
                val st5 = jsonObject.getString("harga_katalog")
                val st8 = jsonObject.getString("img_barang")
                val st9 = jsonObject.getString("kd_barang")

                ongkir(st9)

                bHarga = st5.toInt()

                b.beliBayarNamaBarang.setText(st3)
                b.beliBayarJenisBarang.setText(st4)
                b.beliBayarHarga.setText(CurrencyHelper.formatCurrency(st5.toInt()))
                b.beliBayarHargaBarang.setText(CurrencyHelper.formatCurrency(st5.toInt()))
                Picasso.get().load(st8).into(b.beliBayarFoto)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.requireContext(),"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val bundle = arguments
                when(mode) {
                    "detail_katalog" -> {
                        hm.put("mode", "detail_katalog")
                        hm.put("kd_barang", bundle?.get("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    private fun insert(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlJualBeli,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()

                when(mode) {
                    "bayar_beli" -> {
                        hm.put("mode", "bayar_beli")
                        hm.put("kd_user", preferences.getString("user", ""))
                        hm.put("kd_barang", kd)
                        hm.put("catatan_transaksi", b.beliBayarCatatan.text.toString())
                        hm.put("rek_payment", b.beliBayarPembayaran.selectedItem.toString())
                        hm.put("bayar", bHarga.toString())
                        hm.put("ongkir", bOngkir.toString())
                        hm.put("biaya_admin", bAdmin.toString())
                        hm.put("total_bayar", bTotal.toString())
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}