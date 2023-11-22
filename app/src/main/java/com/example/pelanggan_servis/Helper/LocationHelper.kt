package com.example.pelanggan_servis.Helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

class LocationHelper(private val context: Context) {

    private var locationManager: LocationManager? = null

    private var latA: Double = 0.0
    private var lonA: Double = 0.0

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Pemanggilan ini akan dijalankan ketika lokasi berubah
            val latitude = location.latitude
            val longitude = location.longitude

            // Gunakan latitude dan longitude untuk keperluan Anda
            // Misalnya, panggil fungsi lain dengan parameter latitude dan longitude
            handleLocationUpdate(latitude, longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // Tidak perlu diimplementasikan untuk keperluan mendapatkan lokasi saat ini
        }

        override fun onProviderEnabled(provider: String) {
            // Tidak perlu diimplementasikan untuk keperluan mendapatkan lokasi saat ini
        }

        override fun onProviderDisabled(provider: String) {
            // Tidak perlu diimplementasikan untuk keperluan mendapatkan lokasi saat ini
        }
    }

    fun getLatA(): Double {
        return latA
    }

    fun getLonA(): Double {
        return lonA
    }

    fun requestLocationUpdates() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Izin lokasi tidak diberikan, lakukan penanganan sesuai kebutuhan aplikasi Anda
            return
        }

        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BETWEEN_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener
        )
    }

    fun stopLocationUpdates() {
        locationManager?.removeUpdates(locationListener)
        locationManager = null
    }

    private fun handleLocationUpdate(latitude: Double, longitude: Double) {
        latA = latitude
        lonA = longitude
    }

    companion object {
        private const val MIN_TIME_BETWEEN_UPDATES: Long = 1000 // Dalam milidetik (1 detik)
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // Dalam meter
    }
}
