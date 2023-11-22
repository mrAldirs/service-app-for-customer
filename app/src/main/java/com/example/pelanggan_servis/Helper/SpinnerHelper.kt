package com.example.pelanggan_servis.Helper

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner

class SpinnerHelper(private val context: Context) {

    private val itemList: List<String> = listOf("--Pilih Metode Pembayaran--","COD","BCA","BRI","LinkAja","OVO","ShopeePay")

    fun pembayaran(spinner: Spinner) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, itemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun getSelectedItem(spinner: Spinner): String {
        return spinner.selectedItem.toString()
    }
}
