package com.example.pelanggan_servis.Adapter

import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pelanggan_servis.View.Feedback.FeedbackMainActivity
import com.example.pelanggan_servis.Model.FeedbackModel
import com.example.pelanggan_servis.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterFeedback(private var dataList: List<FeedbackModel>, val parent: FeedbackMainActivity) :
    RecyclerView.Adapter<AdapterFeedback.HolderDataFeedback>(){
    class HolderDataFeedback (v : View) : RecyclerView.ViewHolder(v) {
        val tx = v.findViewById<TextView>( R.id.teksFeedback)
        val nm = v.findViewById<TextView>(R.id.pengirimFeedback)
        val cd = v.findViewById<CardView>(R.id.cardFeedback)
        val lingkaranP = v.findViewById<CircleImageView>(R.id.lingkaranPelanggan)
        val lingkaranA = v.findViewById<CircleImageView>(R.id.lingkaranAdmin)
        val jtgl = v.findViewById<TextView>(R.id.jamtanggalFeedback)
        val txR = v.findViewById<TextView>(R.id.teksReply)
        val nmR = v.findViewById<TextView>(R.id.namaReply)
        val cdR = v.findViewById<CardView>(R.id.cardReply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataFeedback {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_feedback, parent, false)
        return HolderDataFeedback(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataFeedback, position: Int) {
        val data = dataList.get(position)
        holder.tx.setText(data.teks_feedback)
        holder.nm.setText(data.nama)
        holder.jtgl.setText(data.jamtanggal_feedback)

        val namaR = data.nama_reply
        val teksR = data.teks_reply

        if (namaR.equals("null") && teksR.equals("null")) {
            holder.cdR.visibility = View.GONE
        } else if (namaR.equals("") && teksR.equals("")) {
            holder.cdR.visibility = View.GONE
        } else {
            holder.cdR.visibility = View.VISIBLE
            holder.txR.setText(data.teks_reply)
            holder.nmR.setText(data.nama_reply)
        }

        holder.cd.setOnLongClickListener {
            parent.idF = data.kd_feedback

            val contextMenu = PopupMenu(parent, it)
            contextMenu.menuInflater.inflate(R.menu.context_feedback, contextMenu.menu)

            if (parent.namaF.equals(data.nama)) {
                contextMenu.menu.findItem(R.id.feedbackHapus)?.isVisible = true
            } else {
                contextMenu.menu.findItem(R.id.feedbackHapus)?.isVisible = false
            }

            contextMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.feedbackReply -> {
                        parent.b.cardReplyMain.visibility = View.VISIBLE
                        parent.b.namaReplyMain.text = data?.nama
                        parent.b.teksReplyMain.text = data?.teks_feedback
                        parent.b.txPesanFeedback.requestFocus()
                    }
                    R.id.feedbackHapus -> {
                        AlertDialog.Builder(parent)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan!")
                            .setMessage("Apakah Anda menghapus pesan ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                parent.delete(data.kd_feedback)
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                            })
                            .show()
                        true
                    }
                }
                false
            }
            contextMenu.show()
            true
        }

        if (parent.namaF.equals(data.nama)) {
            holder.nm.visibility = View.GONE
            holder.cd.setCardBackgroundColor(Color.parseColor("#CBFFAB"))
            Picasso.get().load(data.profil).into(holder.lingkaranA)
            holder.lingkaranP.visibility = View.GONE
        } else {
            holder.cd.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            Picasso.get().load(data.profil).into(holder.lingkaranP)
            holder.lingkaranA.visibility = View.GONE
        }
    }

    fun setData(newDataList: List<FeedbackModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}