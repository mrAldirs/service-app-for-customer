package com.example.pelanggan_servis.Helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.example.pelanggan_servis.Beli.BeliMainActivity
import com.example.pelanggan_servis.View.Feedback.FeedbackMainActivity
import com.example.pelanggan_servis.View.Jual.JualMainActivity
import com.example.pelanggan_servis.Katalog.KatalogMainActivity
import com.example.pelanggan_servis.View.Profil.EmailProfilActivity
import com.example.pelanggan_servis.View.Profil.PengaturanProfilActivity
import com.example.pelanggan_servis.View.Profil.StatusAkunProfilActivity
import com.example.pelanggan_servis.View.Profil.UsernameProfilActivity
import com.example.pelanggan_servis.View.Layout.LoginActivity
import com.example.pelanggan_servis.View.Layout.MainActivity
import com.example.pelanggan_servis.View.Order.OrderMainActivity
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Chat.ChatActivity
import com.example.pelanggan_servis.View.Layout.AktifkanAkunActivity
import com.example.pelanggan_servis.View.Layout.ImageDetailActivity
import com.example.pelanggan_servis.View.Layout.RegistrasiActivity
import com.example.pelanggan_servis.View.Mekanik.MekanikMainActivity
import com.example.pelanggan_servis.View.Profil.LayananProfilActivity
import com.example.pelanggan_servis.View.Profil.PaymentTokoActivity
import com.example.pelanggan_servis.View.Profil.PrivasiProfilActivity
import com.example.pelanggan_servis.View.Profil.ProfilEditActivity
import com.example.pelanggan_servis.View.Profil.TokoProfilActivity
import com.example.pelanggan_servis.View.Servis.ServisDetailActivity
import com.example.pelanggan_servis.View.Servis.ServisMainActivity

interface IntentHelper {
    fun Context.katalog(): Intent {
        return Intent(this, KatalogMainActivity::class.java)
    }

    fun Context.mekanik(): Intent {
        return Intent(this, MekanikMainActivity::class.java)
    }

    fun Context.servis(): Intent {
        return Intent(this, ServisMainActivity::class.java)
    }

    fun Context.order(): Intent {
        return Intent(this, OrderMainActivity::class.java)
    }

    fun Context.jual(): Intent {
        return Intent(this, JualMainActivity::class.java)
    }

    fun Context.beli(): Intent {
        return Intent(this, BeliMainActivity::class.java)
    }

    fun Context.feedback(): Intent {
        return Intent(this, FeedbackMainActivity::class.java)
    }

    fun Context.loginMain(): Intent {
        return Intent(this, LoginActivity::class.java)
    }

    fun Context.login(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    fun Context.logout(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    fun Context.registrasi(): Intent {
        return Intent(this, RegistrasiActivity::class.java)
    }

    fun Context.statusAkun(): Intent {
        return Intent(this, StatusAkunProfilActivity::class.java)
    }

    fun Context.pengaturanAkun(): Intent {
        return Intent(this, PengaturanProfilActivity::class.java)
    }

    fun Context.emailProfil(data: String): Intent {
        val intent = Intent(this, EmailProfilActivity::class.java)
        intent.putExtra("em", data)
        return intent
    }

    fun Context.chatAdmin(data1: String, data2: String): Intent {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("kode", data1)
        intent.putExtra("topik", data2)
        return intent
    }

    fun Context.imageDetail(data: String): Intent {
        val intent = Intent(this, ImageDetailActivity::class.java)
        intent.putExtra("img", data)
        return intent
    }

    fun Context.usernameProfil(): Intent {
        return Intent(this, UsernameProfilActivity::class.java)
    }

    fun Context.editProfil(): Intent {
        return Intent(this, ProfilEditActivity::class.java)
    }

    fun Context.privasiProfil(): Intent {
        return Intent(this, PrivasiProfilActivity::class.java)
    }

    fun Context.layananProfil(): Intent {
        return Intent(this, LayananProfilActivity::class.java)
    }

    fun Context.paymentToko(): Intent {
        return Intent(this, PaymentTokoActivity::class.java)
    }

    fun Context.profilToko(): Intent {
        return Intent(this, TokoProfilActivity::class.java)
    }

    fun Context.aktifkan(): Intent {
        return Intent(this, AktifkanAkunActivity::class.java)
    }

    fun Context.startMapsActivity() {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse("https://www.google.com/maps/place/Rukin+Servis+TV/@-7.9102537,112.1275764,20z/data=!4m6!3m5!1s0x2e78f78d380489ef:0xe2b60dfb3e98f112!8m2!3d-7.9102667!4d112.1278103!16s%2Fg%2F11hyj19fsx")
        ActivityCompat.startActivity(this, intent, options.toBundle())
    }

    fun Context.noFlag(intent: Intent) {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.stay
        )

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent, options.toBundle())
    }

    fun Context.start(intent: Intent) {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.stay
        )
        startActivity(intent, options.toBundle())
    }

    fun Context.back(intent: Intent) {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        startActivity(intent, options.toBundle())
    }

    fun Context.animationSend(intent: Intent) {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        startActivity(intent, options.toBundle())
    }
}
