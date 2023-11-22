package com.example.pelanggan_servis.View.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.View.Layout.MainActivity
import com.example.pelanggan_servis.databinding.LoginDialogFragmentBinding
import kotlinx.android.synthetic.main.main_activity.*

class DialogLogin2 : DialogFragment(), IntentHelper {
    private lateinit var b: LoginDialogFragmentBinding
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = LoginDialogFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.btnCloseDialogLogin.setOnClickListener {
            dismiss()
        }

        b.btnLoginDahulu.setOnClickListener{
            requireActivity().start(requireActivity().loginMain())
        }

        return v
    }
}