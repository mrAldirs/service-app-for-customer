package com.example.pelanggan_servis.View.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.View.Layout.MainActivity
import com.example.pelanggan_servis.databinding.LoginDialogFragmentBinding
import kotlinx.android.synthetic.main.main_activity.*

class DialogLogin : Fragment(), IntentHelper {
    private lateinit var b: LoginDialogFragmentBinding
    lateinit var thisParent: MainActivity
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = LoginDialogFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as MainActivity

        b.btnCloseDialogLogin.visibility = View.GONE

        if (thisParent.namaAkunMain.text.toString().equals("Admin")) {
            b.teksPesanLogin.setText("Silahkan Login Sebagai Admin Terlebih Dahulu!")
        }else {
            b.teksPesanLogin.setText("Silahkan Login Terlebih Dahulu!")
        }

        b.btnLoginDahulu.setOnClickListener{
            requireActivity().start(requireActivity().loginMain())
        }

        return v
    }
}