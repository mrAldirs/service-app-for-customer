package com.example.pelanggan_servis.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.Model.NotifikasiModel
import com.example.pelanggan_servis.View.Notifikasi.NotifikasiDetailActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Notifikasi.FragmentNotifikasiPesan

class AdapterNotifikasi(private var dataList: List<NotifikasiModel>)
: RecyclerView.Adapter<AdapterNotifikasi.HolderDataNotifikasi>(){
    private var listener: NotifikasiListener? = null

    interface NotifikasiListener {
        fun delete(kd: String)
    }

    fun setListener(listener: NotifikasiListener) {
        this.listener = listener
    }

    class HolderDataNotifikasi(v: View) : RecyclerView.ViewHolder(v) {
        val jamTanggal = v.findViewById<TextView>(R.id.adpJamTanggalNotif)
        val jenisNotif = v.findViewById<TextView>(R.id.adpJenisNotif)
        val teks = v.findViewById<TextView>(R.id.adpTeksNotif)
        val send = v.findViewById<TextView>(R.id.adpNamaUserNotif)
        val card = v.findViewById<CardView>(R.id.cardNotif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataNotifikasi {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_notif,parent,false)
        return HolderDataNotifikasi(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataNotifikasi, position: Int) {
        val data = dataList.get(position)
        holder.jamTanggal.setText(data.jamtanggal_notif)
        holder.jenisNotif.setText(data.jenis_notif + "!")
        holder.teks.setText(data.teks_notif)

        val sendNotif = data.nama
        if (sendNotif.equals("All")) {
            holder.send.setText("Dikirim ke: Semua")
        } else {
            holder.send.setText("Dikirim ke: "+data.nama)
        }

        val status = data.jenis_notif
        if (status.equals("Informasi")) {
            holder.card.setOnClickListener {
                null
            }
        } else {
            holder.card.setOnClickListener {
                val options = ActivityOptionsCompat.makeCustomAnimation(it.context, R.anim.slide_in_right, R.anim.slide_out_left)
                val intent = Intent(it.context, NotifikasiDetailActivity::class.java)
                intent.putExtra("jenis", data.jenis_notif)
                intent.putExtra("jam", data.jamtanggal_notif)
                intent.putExtra("teks", data.teks_notif)
                ActivityCompat.startActivity(it.context, intent, options.toBundle())
            }

            holder.card.setOnLongClickListener { v: View ->
                val contextMenu = PopupMenu(v.context, v)
                contextMenu.menuInflater.inflate(R.menu.context_notif, contextMenu.menu)

                contextMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.notifHapus -> {
                            AlertDialog.Builder(v.context)
                                .setIcon(R.drawable.warning)
                                .setTitle("Peringatan!")
                                .setMessage("Apakah Anda ingin menghapus pesan ini?")
                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                                    listener?.delete(data.kd_notif)
                                })
                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                                })
                                .show()
                        }
                    }
                    false
                }
                contextMenu.show()
                true
            }
        }
    }

    fun setData(newDataList: List<NotifikasiModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}