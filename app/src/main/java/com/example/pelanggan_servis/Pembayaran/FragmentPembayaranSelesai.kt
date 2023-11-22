package com.example.pelanggan_servis.Pembayaran

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Adapter.AdapterPembayaran
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.UrlClass
import kotlinx.android.synthetic.main.pembayaran_status_fragment.view.*
import org.json.JSONArray

class FragmentPembayaranSelesai : Fragment() {
    lateinit var urlClass: UrlClass
    lateinit var v: View

    lateinit var preferences: SharedPreferencesHelper

    val daftarBayar = mutableListOf<HashMap<String,String>>()
    lateinit var bayarAdapter: AdapterPembayaran

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.pembayaran_status_fragment, container, false)

        v.textJudul.setText("Pembayaran Telah Selesai")

        preferences = SharedPreferencesHelper(v.context)
        urlClass = UrlClass()

        bayarAdapter = AdapterPembayaran(daftarBayar)
        v.rvPembayaranStatus.layoutManager = LinearLayoutManager(this.context)
        v.rvPembayaranStatus.adapter = bayarAdapter

        showDataPembayaran("show_data_pembayaran_selesai")

        return v
    }

    private fun showDataPembayaran(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPembayaran,
            Response.Listener { response ->
                daftarBayar.clear()
                if (response.equals("0")) {
                    v.notfound.visibility = View.VISIBLE
                    v.rvPembayaranStatus.visibility = View.GONE
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("total_bayar",jsonObject.getString("total_bayar"))
                        frm.put("tgl_pembayaran",jsonObject.getString("tgl_pembayaran"))
                        frm.put("nama_barang",jsonObject.getString("nama_barang"))
                        frm.put("jenis_transaksi",jsonObject.getString("jenis_transaksi"))
                        frm.put("status_pembayaran",jsonObject.getString("status_pembayaran"))
                        frm.put("tgl_transaksi",jsonObject.getString("tgl_transaksi"))
                        frm.put("kd_pembayaran",jsonObject.getString("kd_pembayaran"))
                        frm.put("kd_transaksi",jsonObject.getString("kd_transaksi"))

                        daftarBayar.add(frm)
                    }
                    bayarAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(v.context)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_pembayaran_selesai" -> {
                        hm.put("mode","show_data_pembayaran_selesai")
                        hm.put("kd_user", preferences.getString("user", ""))
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}