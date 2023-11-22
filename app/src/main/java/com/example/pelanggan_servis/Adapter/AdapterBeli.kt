package com.example.pelanggan_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Beli.BeliBayarFragment
import com.example.pelanggan_servis.Beli.BeliMainActivity
import com.example.pelanggan_servis.Helper.CurrencyHelper
import com.example.pelanggan_servis.Katalog.KatalogDetailActivity
import com.example.pelanggan_servis.R
import com.squareup.picasso.Picasso

class AdapterBeli(val dataBeli: List<HashMap<String,String>>, val parent: BeliMainActivity) :
    RecyclerView.Adapter<AdapterBeli.HolderDataBeli>(){
    class HolderDataBeli(v: View) : RecyclerView.ViewHolder(v){
        val cd = v.findViewById<CardView>(R.id.card)
        val nmB = v.findViewById<TextView>(R.id.beliNama)
        val tgl = v.findViewById<TextView>(R.id.beliTanggal)
        val jns = v.findViewById<TextView>(R.id.beliJenis)
        val sts = v.findViewById<TextView>(R.id.beliStatus)
        val hrg = v.findViewById<TextView>(R.id.beliHarga)
        val img = v.findViewById<ImageView>(R.id.beliPhoto)
        val sl = v.findViewById<TextView>(R.id.btnSelengkapnya)
        val bl = v.findViewById<Button>(R.id.beliBtnBeli)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataBeli {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_beli, parent, false)
        return HolderDataBeli(v)
    }

    override fun getItemCount(): Int {
        return dataBeli.size
    }

    override fun onBindViewHolder(holder: HolderDataBeli, position: Int) {
        val data = dataBeli.get(position)

        holder.nmB.setText(data.get("nama_barang"))
        holder.tgl.setText(data.get("tgl_upload"))
        holder.jns.setText("Jenis :"+data.get("jenis_barang"))
        holder.hrg.text = CurrencyHelper.formatCurrency(data.get("harga_katalog")!!.toInt())
        Picasso.get().load(data.get("img_barang")).into(holder.img)

        val stat = data.get("status_katalog")
        if (stat.toString().equals("Ready")) {
            holder.sts.setText("Barang Tersedia")
            holder.sts.setTextColor(Color.BLUE)
        } else if (stat.toString().equals("Sold")) {
            holder.sts.setText("Barang Telah Terjual")
            holder.sts.setTextColor(Color.RED)
        }

        holder.sl.setOnClickListener { v: View ->
            val intent = Intent(v.context, KatalogDetailActivity::class.java)
            intent.putExtra("kdB", data.get("kd_barang").toString())
            v.context.startActivity(intent)
            parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        holder.bl.setOnClickListener {
            var frag = BeliBayarFragment()
            var paket : Bundle? = parent.intent.extras
            var kode = paket?.getString("kode")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", data.get("kd_barang").toString())
            frag.arguments = bundle

            frag.show(parent.supportFragmentManager, "BeliBayarFragment")
        }
    }
}