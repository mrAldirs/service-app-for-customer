package com.example.pelanggan_servis.View.Layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pelanggan_servis.databinding.MainDetailFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentMainDetail : BottomSheetDialogFragment() {
    private lateinit var b: MainDetailFragmentBinding
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = MainDetailFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.btnTutup.setOnClickListener { dismiss() }
        b.btnBatalkan.setOnClickListener { dismiss() }

        val ob = arguments?.get("ob").toString().toInt()
        val s = arguments?.get("s").toString().toInt()
        val j = arguments?.get("j").toString().toInt()

        b.orderBeli.text = "$ob Kali"
        b.servis.text = "$s Kali"
        b.jual.text = "$j Kali"

        val total = ob + s + j
        b.total.text = "$total Kali"

        return v
    }
}