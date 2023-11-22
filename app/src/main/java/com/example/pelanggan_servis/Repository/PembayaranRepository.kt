package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.PembayaranModel
import org.json.JSONObject

class PembayaranRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun detail(kd_t: String): LiveData<PembayaranModel> {
        val data = MutableLiveData<PembayaranModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlPembayaran,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_transaksi")
                val st2 = jsonObject.getString("rek_payment")
                val st3 = jsonObject.getString("tgl_pembayaran")
                val st4 = jsonObject.getString("bayar")
                val st5 = jsonObject.getString("biaya_admin")
                val st6 = jsonObject.getString("ongkir")
                val st7 = jsonObject.getString("total_bayar")
                val st8 = jsonObject.getString("bukti_pembayaran")
                val st9 = jsonObject.getString("status_pembayaran")

                val dataList = PembayaranModel(st1,st2,st3,st4,st5,st6,st7,st8,st9)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_transaksi", kd_t)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }
}