package com.example.pelanggan_servis.View.Notifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pelanggan_servis.Adapter.AdapterNotifikasi
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.ViewModel.NotifikasiViewModel
import com.example.pelanggan_servis.databinding.NotifikasiJenisFragmentBinding

class FragmentNotifikasiPesan : Fragment(), AdapterNotifikasi.NotifikasiListener {
    private lateinit var b: NotifikasiJenisFragmentBinding
    lateinit var v : View
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferencesHelper
    lateinit var adapter: AdapterNotifikasi
    private lateinit var nVM: NotifikasiViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = NotifikasiJenisFragmentBinding.inflate(layoutInflater)
        v = b.root

        preferences = SharedPreferencesHelper(v.context)
        urlClass = UrlClass()

        nVM = ViewModelProvider(this).get(NotifikasiViewModel::class.java)

        adapter = AdapterNotifikasi(ArrayList())
        adapter.setListener(this)
        b.rvNotifikasi.layoutManager = LinearLayoutManager(this.context)
        b.rvNotifikasi.adapter = adapter

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        var kd = preferences.getString("user", "")
        nVM.loadDataPesan(kd).observe(requireActivity(), Observer { dataList ->
            adapter.setData(dataList)
        })
    }

    override fun delete(kd:String) {
        nVM.delete(kd).observe(requireActivity(), Observer { result ->
            loadData()
            Toast.makeText(v.context, "Berhasil menghapus pesan!", Toast.LENGTH_SHORT).show()
        })
    }
}