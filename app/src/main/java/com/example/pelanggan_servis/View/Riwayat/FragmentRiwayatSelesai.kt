package com.example.pelanggan_servis.View.Riwayat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pelanggan_servis.Adapter.AdapterRiwayatSelesai
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.ViewModel.RiwayatViewModel
import com.example.pelanggan_servis.databinding.RiwayatProsesFragmentBinding
import kotlinx.android.synthetic.main.riwayat_proses_fragment.view.*

class FragmentRiwayatSelesai : Fragment() {
    lateinit var b: RiwayatProsesFragmentBinding
    lateinit var urlClass: UrlClass
    lateinit var v: View
    private lateinit var rVM: RiwayatViewModel

    lateinit var preferences: SharedPreferencesHelper

    private lateinit var adapter: AdapterRiwayatSelesai

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = RiwayatProsesFragmentBinding.inflate(layoutInflater)
        v = b.root

        preferences = SharedPreferencesHelper(this.requireContext())
        urlClass = UrlClass()
        rVM = ViewModelProvider(this).get(RiwayatViewModel::class.java)

        adapter = AdapterRiwayatSelesai(ArrayList())
        v.rvRiwayat.layoutManager = LinearLayoutManager(this.context)
        v.rvRiwayat.adapter = adapter

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        var mode = "read_transaksi_selesai"
        rVM.loadData(mode, preferences.getString("user", "").toString())
            .observe(this, Observer { dataList ->
                adapter.setData(dataList)
            })
    }
}