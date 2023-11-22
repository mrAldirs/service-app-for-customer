package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.PengirimanModel
import org.json.JSONObject

class PengirimanRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun detail(kd: String): LiveData<PengirimanModel> {
        val data = MutableLiveData<PengirimanModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlKirim,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_jadwal")
                val st2 = jsonObject.getString("tglkirim_jadwal")
                val st3 = jsonObject.getString("nama_pengirim")
                val st4 = jsonObject.getString("bukti_kirim")
                val st5 = jsonObject.getString("tglsampai_jadwal")

                val dataList = PengirimanModel(st1,st2,st3,st4,st5)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_transaksi", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }
}