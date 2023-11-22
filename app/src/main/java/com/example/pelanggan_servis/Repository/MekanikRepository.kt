package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.MekanikModel
import org.json.JSONArray

class MekanikRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(nm: String): LiveData<List<MekanikModel>> {
        val data = MutableLiveData<List<MekanikModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMekanik,
            Response.Listener { response ->
                val dataList = mutableListOf<MekanikModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = MekanikModel(jsonObject.getString("kd_mekanik"),
                        jsonObject.getString("nama_mekanik"),
                        jsonObject.getString("foto_mekanik"),
                        jsonObject.getString("status_mekanik"),
                        jsonObject.getString("average_rating").toFloat()
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "load_data")
                hm.put("nama_mekanik", nm)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }
}