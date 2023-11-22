package com.example.pelanggan_servis.View.Notifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pelanggan_servis.Adapter.AdapterNotifikasi
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.ViewModel.NotifikasiViewModel
import com.example.pelanggan_servis.databinding.NotifikasiJenisFragmentBinding

class FragmentNotifikasiInformasi : Fragment() {
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
        b.rvNotifikasi.layoutManager = LinearLayoutManager(this.context)
        b.rvNotifikasi.adapter = adapter

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        nVM.loadDataInformasi().observe(requireActivity(), Observer { dataList ->
            adapter.setData(dataList)
        })
    }
}