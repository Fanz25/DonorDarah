package com.aplikasi.donordarah.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.aplikasi.donordarah.PostModel
import com.aplikasi.donordarah.R
import com.aplikasi.donordarah.databinding.FragmentRequestBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RequestFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRequestBinding? = null

    private val binding get() = _binding!!
    private var postcollection = Firebase.firestore.collection("post")

    private val kota = arrayOf("Cianjur", "Bandung", "Jakarta", "Depok")
    private val Goldar = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    private var cal: Calendar? = null

    private var request: LocationRequest? = null
    private var mLocation: Location? = null
    private var providerClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var lat: String? = null
    private var lng: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goldarAdapter: ArrayAdapter<String>? = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                Goldar
            )
        }
        binding.inputGoldar.setAdapter(goldarAdapter)

        val kotaAdapter: ArrayAdapter<String>? = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                kota
            )
        }
        binding.inputKota.setAdapter(kotaAdapter)
        binding.progressBar.visibility = View.GONE

        inisialiasai()
        startLocationUpdates()

    }

    private fun inisialiasai() {
        binding.btnPost.setOnClickListener {
            var mNama: String? = null
            var mNoHp: String? = null
            val mDes = binding.inputDes.text.toString()
            val mKota = binding.inputKota.text.toString()
            val mGoldar = binding.inputGoldar.text.toString()
            cal = Calendar.getInstance()
            val day = cal?.get(Calendar.DAY_OF_MONTH)
            var month = cal?.get(Calendar.MONTH)
            val year = cal?.get(Calendar.YEAR)
            val hour = cal?.get(Calendar.HOUR)
            val min = cal?.get(Calendar.MINUTE)
            month = month?.plus(1)


            if (mDes.isEmpty() || mKota.isEmpty() || mGoldar.isEmpty()) {
                Toast.makeText(
                    activity, "Harap isi kolom.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                val dblocation = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid.toString())

                dblocation.get()
                    .addOnSuccessListener {
                        mNama = it?.getString("namaLengkap").toString()
                        mNoHp = it?.getString("noHp").toString()
                        val dataModel = PostModel()
                        dataModel.nama = mNama
                        dataModel.noHp = mNoHp
                        dataModel.deskripsi = mDes
                        dataModel.kota = mKota
                        dataModel.goldar = mGoldar
                        dataModel.date = "$day/$month/$year"
                        dataModel.time = "$hour:$min"
                        dataModel.lat = lat
                        dataModel.lng = lng
                        dataModel.uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

                        val db = FirebaseFirestore.getInstance()
                        db.collection("post")
                            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                            .set(dataModel)
                            .addOnSuccessListener {
                                binding.progressBar.visibility = View.GONE
                                binding.inputKota.setText(" ")
                                binding.inputGoldar.setText(" ")
                                binding.inputDes.setText(" ")
                                Toast.makeText(
                                    activity,
                                    "Post Berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .addOnFailureListener {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            activity,
                            it.localizedMessage?.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }
    }

    override fun onMapReady(p0: GoogleMap) {

    }

    private fun startLocationUpdates() {
        buildLocationRequest()
        providerClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    Toast.makeText(
                        activity,
                        "Tidak dapat menemukan lokasi",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    for (location in locationResult.locations) {
                        mLocation = location
                    }
                    onLocationChanged(mLocation)
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(context as AppCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context as AppCompatActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        providerClient?.requestLocationUpdates(request, locationCallback, null)
    }

    private fun buildLocationRequest() {
        request = LocationRequest()
        request!!.smallestDisplacement = 10f
        request!!.fastestInterval = 3000
        request!!.interval = 5000
        request!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun onLocationChanged(location: Location?) {

        lat = location?.latitude.toString()
        lng = location?.longitude.toString()

    }
}