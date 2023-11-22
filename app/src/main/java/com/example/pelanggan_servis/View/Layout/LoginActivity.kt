package com.example.pelanggan_servis.View.Layout

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.pelanggan_servis.Helper.IntentHelper
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity(), IntentHelper {
    lateinit var b: LoginActivityBinding
    lateinit var loginFragment: LoginFragment
    lateinit var ft : FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = LoginActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        b.btnLogin.setOnClickListener {
            LoginFragment().show(supportFragmentManager, "LoginFragment")
        }

        b.btnRegisLogin.setOnClickListener {
            start(registrasi())
        }
        loginFragment = LoginFragment()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}