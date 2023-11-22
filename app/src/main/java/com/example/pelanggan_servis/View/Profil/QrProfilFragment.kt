package com.example.pelanggan_servis.View.Profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pelanggan_servis.databinding.ProfilQrFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class QrProfilFragment : BottomSheetDialogFragment() {
    private lateinit var b: ProfilQrFragmentBinding
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ProfilQrFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.btnBatalkan.setOnClickListener { dismiss() }
        b.btnTutup.setOnClickListener { dismiss() }

        val prof = "https://www.google.com/maps/place/Rukin+Servis+TV/@-7.9096488,112.1270951,16.89z/data=!4m15!1m8!3m7!1s0x2e78f661707e125d:0x1dcad186c48efdd2!2sJajar,+Kec.+Wates,+Kabupaten+Kediri,+Jawa+Timur!3b1!8m2!3d-7.9046877!4d112.1236468!16s%2Fg%2F121d1srj!3m5!1s0x2e78f78d380489ef:0xe2b60dfb3e98f112!8m2!3d-7.9102667!4d112.1278103!16s%2Fg%2F11hyj19fsx?entry=ttu"
        val barCodeEncoder = BarcodeEncoder()
        val bitmap = barCodeEncoder.encodeBitmap(prof,
            BarcodeFormat.QR_CODE,400,400)
        b.imV.setImageBitmap(bitmap)

        return v
    }
}