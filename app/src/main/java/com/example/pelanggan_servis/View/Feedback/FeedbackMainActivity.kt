package com.example.pelanggan_servis.View.Feedback

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pelanggan_servis.Adapter.AdapterFeedback
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.Model.FeedbackModel
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.ViewModel.FeedbackViewModel
import com.example.pelanggan_servis.databinding.FeedbackMainActivityBinding
import org.json.JSONArray
import org.json.JSONObject

class FeedbackMainActivity : AppCompatActivity() {
    lateinit var b: FeedbackMainActivityBinding
    lateinit var preferences: SharedPreferencesHelper
    private lateinit var fVM : FeedbackViewModel
    private lateinit var adapter: AdapterFeedback
    var namaF = ""
    var teks = ""
    var idF = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = FeedbackMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        fVM = ViewModelProvider(this).get(FeedbackViewModel::class.java)

        preferences = SharedPreferencesHelper(this)
        namaF = preferences.getString("nama", "")

        adapter = AdapterFeedback(ArrayList(), this)
        b.rvFeedback.layoutManager = LinearLayoutManager(this)
        b.rvFeedback.adapter = adapter
        b.rvFeedback.post {
            b.rvFeedback.scrollToPosition(adapter.itemCount - 1)
        }

        b.btnKirimFeedback.setOnClickListener {
            if (b.teksReplyMain.text.toString().equals("") && b.namaReplyMain.text.toString().equals("")) {
                teks = b.txPesanFeedback.text.toString()
                insert("insert")
            } else {
                teks = b.txPesanFeedback.text.toString()
                insert("insert_reply")
            }
            b.txPesanFeedback.setText("")
            restartActivity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun restartActivity() {
        recreate()
        b.txPesanFeedback.clearFocus()
    }

    override fun onStart() {
        super.onStart()
        loadData()
        b.rvFeedback.scrollToPosition(adapter.itemCount - 1)
    }

    fun loadData() {
        fVM.loadData().observe(this, Observer { dataList ->
            adapter.setData(dataList)
        })
    }

    fun delete(kd: String) {
        fVM.delete(kd).observe(this, Observer { success ->
            if (success) {
                loadData()
                Toast.makeText(this, "Berhasil menghapus feedback!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun insert(mode: String) {
        val data = FeedbackModel(
            "",
            teks_feedback = teks,
            nama = preferences.getString("user", ""),
            nama_reply = b.namaReplyMain.text.toString(),
            teks_reply = b.teksReplyMain.text.toString(),
            "",
            jamtanggal_feedback = "",
            ""
        )

        fVM.insert(mode, data).observe(this, Observer {
            loadData()
        })
    }
}