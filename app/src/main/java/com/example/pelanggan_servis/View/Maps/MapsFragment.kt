package com.example.pelanggan_servis.View.Maps

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pelanggan_servis.R
import com.example.pelanggan_servis.View.Layout.RegistrasiProfilActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.maps_fragment.view.*
import mumayank.com.airlocationlibrary.AirLocation
import java.util.Locale

class MapsFragment : BottomSheetDialogFragment(), OnMapReadyCallback {
    var airLoc : AirLocation? = null
    var gMap : GoogleMap? = null
    lateinit var mapFragment : SupportMapFragment

    val  RC_HASIL_SUKSES : Int=100

    lateinit var v: View
    lateinit var parent: RegistrasiProfilActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.maps_fragment, container, false)
        parent = activity as RegistrasiProfilActivity

        v.tvKota.setText("Kediri")
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        v.fab.setOnClickListener{
            airLoc = AirLocation(parent,true,true,
                object : AirLocation.Callbacks{
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                        Toast.makeText(parent, "Gagal mendapatkan posisi saat ini",
                            Toast.LENGTH_SHORT).show()
                        v.edtMaps.setText("Gagal mendapatkan posisi saat ini")
                    }

                    override fun onSuccess(location: Location) {
                        val ll = LatLng(location.latitude,location.longitude)
                        gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16.0f))
                        v.edtMaps.setText("${location.latitude}, " + "${location.longitude}")
                        v.tvJln.setText(getJlnName(location.latitude,location.longitude))
                        v.tvDesa.setText(getDesaName(location.latitude,location.longitude))
                        v.tvKec.setText(getKecName(location.latitude,location.longitude))
                        v.tvNegara.setText(getCountryName(location.latitude,location.longitude))
                    }
                })
        }

        v.btnNext.setOnClickListener {
            val maps =  v.edtMaps.text.toString()
            val jln = v.tvJln.text.toString()
            val desa = v.tvDesa.text.toString()
            val kec = v.tvKec.text.toString()
            val kota = v.tvKota.text.toString()
            val provinsi = v.tvNegara.text.toString()

            parent.binding.regisProfilAlamat.setText(jln)
            parent.binding.regisProfilDesa.setText(desa)
            parent.binding.regisProfilKecamatan.setText(kec)
            parent.binding.regisProfilKota.setText(kota)
            parent.binding.regisProfilProvinsi.setText(provinsi)
            parent.koordinat = maps
            Toast.makeText(v.context, maps, Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return v
    }

    //method onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLoc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    //method onRequestPermissionsResult
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        airLoc?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        if (gMap!=null) {
            airLoc = AirLocation(parent, true, true,
                object : AirLocation.Callbacks {
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                        Toast.makeText(
                            v.context, "Gagal mendapatkan posisi saat ini",
                            Toast.LENGTH_SHORT
                        ).show()
                        v.edtMaps.setText("Gagal mendapatkan posisi saat ini")
                    }

                    override fun onSuccess(location: Location) {
                        val ll = LatLng(location.latitude, location.longitude)
                        gMap!!.addMarker(MarkerOptions().position(ll).title("Posisi saya"))
                        gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16.0f))
                        v.edtMaps.setText("${location.latitude}, " + "${location.longitude}")
                        v.tvJln.setText(getJlnName(location.latitude,location.longitude))
                        v.tvDesa.setText(getDesaName(location.latitude,location.longitude))
                        v.tvKec.setText(getKecName(location.latitude,location.longitude))
                        v.tvNegara.setText(getCountryName(location.latitude,location.longitude))
                    }
                })
        }
    }

    private fun getDesaName(lat: Double, long: Double): String {
        var desaName = ""
        val geoCoder = Geocoder(v.context, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, long, 1)

        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            desaName = address.subLocality ?: ""
        }

        return desaName
    }

    private fun getKecName(lat: Double, long: Double): String {
        var kecName = ""
        val geoCoder = Geocoder(v.context, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, long, 1)

        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            kecName = address.locality ?: ""
        }

        return kecName
    }

    private fun getJlnName(lat: Double, long: Double): String {
        var jlnName = ""
        val geoCoder = Geocoder(v.context, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, long, 1)

        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            jlnName = address.thoroughfare ?: ""
        }

        return jlnName
    }

    private fun getCountryName(lat: Double, long: Double): String {
        var countryName = ""
        val geoCoder = Geocoder(v.context, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, long, 1)

        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            countryName = address.countryName ?: ""
        }

        return countryName
    }

    private fun getProvinces(lat: Double, long: Double): List<String> {
        val geoCoder = Geocoder(v.context, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, long, 1)
        val provinces = mutableListOf<String>()
        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            address.adminArea?.let { provinces.add(it) }
            address.subAdminArea?.let { provinces.add(it) }
            address.locality?.let { provinces.add(it) }
        }
        return provinces
    }
}