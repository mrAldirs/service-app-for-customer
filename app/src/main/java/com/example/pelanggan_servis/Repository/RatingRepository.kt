package com.example.pelanggan_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.GlobalData.BaseUrl
import com.example.pelanggan_servis.Model.RatingModel
import org.json.JSONObject

class RatingRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun insert(ratingModel: RatingModel): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMekanik,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("mode", "insert_rating")
                hm.put("kd_mekanik", ratingModel.kd_mekanik)
                hm.put("kd_user", ratingModel.kd_user)
                hm.put("rating", ratingModel.rating)
                hm.put("komen", ratingModel.komen)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }
}