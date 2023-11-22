package com.example.pelanggan_servis.Pembayaran

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pembayaran_main_fragment.view.*
import org.json.JSONArray
import org.json.JSONObject

class PembayaranMainFragment : Fragment() {

    lateinit var preferences: SharedPreferencesHelper

    lateinit var urlClass : UrlClass

    lateinit var fragmentPembayaranBelum: FragmentPembayaranBelum
    lateinit var fragmentPembayaranSelesai: FragmentPembayaranSelesai
    lateinit var ft : FragmentTransaction
    lateinit var v : View

    var kdT = ""
    var hrg : Int = 0
    var jns = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.pembayaran_main_fragment, container, false)

        preferences = SharedPreferencesHelper(requireContext())
        urlClass = UrlClass()

        v.btnBelumBayar.setOnClickListener {
            ft = childFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayoutPembayaran,fragmentPembayaranBelum).commit()
            v.frameLayoutPembayaran.visibility = View.VISIBLE
            v.frameLayoutPembayaran.setBackgroundColor(Color.argb(255,255,255,255))
            v.btnBayarSekarang.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(),
                R.color.red_200)))
            v.btnBelumBayar.setCardBackgroundColor(Color.parseColor("#EFEFEF"))
            v.btnSelesaiBayar.setCardBackgroundColor(Color.WHITE)
        }

        v.btnSelesaiBayar.setOnClickListener {
            ft = childFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayoutPembayaran,fragmentPembayaranSelesai).commit()
            v.frameLayoutPembayaran.visibility = View.VISIBLE
            v.frameLayoutPembayaran.setBackgroundColor(Color.argb(255,255,255,255))
            v.btnBayarSekarang.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(),
                R.color.red_200)))
            v.btnSelesaiBayar.setCardBackgroundColor(Color.parseColor("#EFEFEF"))
            v.btnBelumBayar.setCardBackgroundColor(Color.WHITE)
        }

        v.btnBayarSekarang.setOnClickListener {
            v.frameLayoutPembayaran.visibility = View.GONE
            v.btnBayarSekarang.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(),
                R.color.red_500)))
            v.btnSelesaiBayar.setCardBackgroundColor(Color.WHITE)
            v.btnBelumBayar.setCardBackgroundColor(Color.WHITE)
        }

        v.btnKonfiramasiBayarSekaran.setOnClickListener {
            var frag = FragmentPembayaranKonfirmasi()
            var paket : Bundle? = requireActivity().intent.extras
            var kode = paket?.getString("kode")
            var harga = paket?.getString("hrg")
            var jenis = paket?.getString("jns")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", kdT)
            bundle.putString("hrg", harga)
            bundle.putString("hrg", hrg.toString())
            bundle.putString("jns", jenis)
            bundle.putString("jns", jns)
            frag.arguments = bundle

            frag.show(childFragmentManager, "FragmentPembayaranKonfirmasi")
        }

        fragmentPembayaranBelum = FragmentPembayaranBelum()
        fragmentPembayaranSelesai = FragmentPembayaranSelesai()

        showData("pembayaran_terakhir")

        v.refresh.setOnRefreshListener {
            showData("pembayaran_terakhir")

            // Hentikan animasi refresh setelah selesai
            v.refresh.isRefreshing = false
            refreshData()
        }
        return v
    }

    fun refreshData() {
        // Simulasi penundaan sebelum menghentikan animasi refresh
        Handler().postDelayed({
            showData("pembayaran_terakhir")
            // Hentikan animasi refresh setelah selesai
            v.refresh.isRefreshing = false
        }, 2000) // Atur waktu penundaan dalam milidetik
    }

    fun showData(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPembayaran,
            Response.Listener { response ->
                if (response.equals("0")) {
                    v.notfound.visibility = View.VISIBLE
                    v.cd1.visibility = View.GONE
                    v.cd2.visibility = View.GONE
                    v.cd3.visibility = View.GONE
                    v.cd4.visibility = View.GONE
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        val st1 = jsonObject.getString("kd_transaksi")
                        val st2 = jsonObject.getString("nama_barang")
                        val st3 = jsonObject.getString("jenis_barang")
                        val st4 = jsonObject.getString("img_barang")
                        val st5 = jsonObject.getString("rek_payment")
                        val st7 = jsonObject.getString("tgl_transaksi")
                        val st8 = jsonObject.getString("bayar").toInt()
                        val st9 = jsonObject.getString("ongkir").toInt()
                        val st10 = jsonObject.getString("biaya_admin").toInt()
                        val st11 = jsonObject.getString("total_bayar").toInt()
                        val st12 = jsonObject.getString("jenis_transaksi")

                        kdT = st1
                        hrg = st11
                        jns = st12
                        v.bayarMainKdTransaksi.setText(st1)
                        v.bayarMainNamaBarang.setText(st2)
                        v.bayarMainJenisBarang.setText(st3)
                        Picasso.get().load(st4).into(v.bayarMainImage)
                        v.bayarMainTglTransaksi.setText(st7)
                        v.bayarMainHargaBarang.text = CurrencyHelper.formatCurrency(st8)
                        v.bayarMainBiayaAdmin.text = CurrencyHelper.formatCurrency(st10)
                        v.bayarMainTotalPembayaran.text = CurrencyHelper.formatCurrency(st11)

                        if (st12.equals("Beli")) {
                            v.bayarMainJenisTransaksi.setText("Jual Barang")
                        } else if (st12.equals("Jual")) {
                            v.bayarMainJenisTransaksi.setText("Pembelian Barang")
                        } else {
                            v.bayarMainJenisTransaksi.setText(st12)
                        }

                        if (st5.equals("null")){
                            v.cd2.visibility = View.GONE
                        } else {
                            pembayaranMetode("metode_bayar")
                        }
                        if (st9.equals("null")) {
                            v.cardOngkir.visibility = View.GONE
                        } else {
                            v.bayarMainOngkir.text = CurrencyHelper.formatCurrency(st9)
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "pembayaran_terakhir" -> {
                        hm.put("mode","pembayaran_terakhir")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun pembayaranMetode(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPembayaran,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("rek_payment")
                val st2 = jsonObject.getString("no_payment")

                if (st1 == "COD") {
                    v.notfound.visibility = View.VISIBLE
                    v.cd1.visibility = View.GONE
                    v.cd2.visibility = View.GONE
                    v.cd3.visibility = View.GONE
                    v.cd4.visibility = View.GONE
                }

                v.bayarMainNoPayment.setText("No. Rek " + st2)
                v.bayarMainPembayaran.setText("Transfer " + st1)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "metode_bayar" -> {
                        hm.put("mode", "metode_bayar")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}