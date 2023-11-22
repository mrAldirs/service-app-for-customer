package com.example.pelanggan_servis.View.Riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pelanggan_servis.Adapter.AdapterRiwayatProses
import com.example.pelanggan_servis.Helper.SharedPreferencesHelper
import com.example.pelanggan_servis.UrlClass
import com.example.pelanggan_servis.ViewModel.RiwayatViewModel
import com.example.pelanggan_servis.databinding.RiwayatProsesFragmentBinding

class FragmentRiwayatProses : Fragment() {
    lateinit var b: RiwayatProsesFragmentBinding
    lateinit var urlClass: UrlClass
    lateinit var v: View
    private lateinit var rVM: RiwayatViewModel

    lateinit var preferences: SharedPreferencesHelper

    private lateinit var adapter: AdapterRiwayatProses

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = RiwayatProsesFragmentBinding.inflate(layoutInflater)
        v = b.root

        preferences = SharedPreferencesHelper(v.context)
        urlClass = UrlClass()

        rVM = ViewModelProvider(this).get(RiwayatViewModel::class.java)

        adapter = AdapterRiwayatProses(ArrayList())
        b.rvRiwayat.layoutManager = LinearLayoutManager(this.context)
        b.rvRiwayat.adapter = adapter

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        var mode = "read_all_transaksi"
        rVM.loadData(mode, preferences.getString("user", ""))
            .observe(this, Observer { dataList ->
            adapter.setData(dataList)
        })
    }
}