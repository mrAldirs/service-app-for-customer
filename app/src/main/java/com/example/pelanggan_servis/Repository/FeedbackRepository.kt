package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.FeedbackModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(): LiveData<List<FeedbackModel>> {
        val data = MutableLiveData<List<FeedbackModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlFeedback,
            Response.Listener { response ->
                val dataList = mutableListOf<FeedbackModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = FeedbackModel(
                        jsonObject.getString("kd_feedback"),
                        jsonObject.getString("teks_feedback"),
                        jsonObject.getString("nama"),
                        jsonObject.getString("nama_reply"),
                        jsonObject.getString("teks_reply"),
                        jsonObject.getString("level"),
                        jsonObject.getString("jamtanggal_feedback"),
                        jsonObject.getString("profil")
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "show_data_feedback")

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun delete(kd: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlFeedback,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "delete")
                hm.put("kd_feedback", kd)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }

    fun insert(mode: String, feedback: FeedbackModel): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlFeedback,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                when(mode) {
                    "insert" -> {
                        hm.put("mode", "insert")
                        hm.put("teks_feedback", feedback.teks_feedback)
                        hm.put("kd_user", feedback.nama)
                    }
                    "insert_reply" -> {
                        hm.put("mode", "insert_reply")
                        hm.put("teks_feedback", feedback.teks_feedback)
                        hm.put("kd_user", feedback.nama)
                        hm.put("nama_reply", feedback.nama_reply)
                        hm.put("teks_reply", feedback.teks_reply)
                    }
                }

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }
}