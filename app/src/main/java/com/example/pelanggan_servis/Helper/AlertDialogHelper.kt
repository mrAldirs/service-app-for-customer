package com.example.pelanggan_servis.Helper

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertDialogHelper(private val context: Context) {

    fun showConfirmationDialog(
        title: String,
        message: String,
        positiveButtonTitle: String,
        negativeButtonTitle: String,
        positiveButtonListener: DialogInterface.OnClickListener,
        negativeButtonListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_input_get)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonTitle, positiveButtonListener)
            .setNegativeButton(negativeButtonTitle, negativeButtonListener)
            .show()
    }
}