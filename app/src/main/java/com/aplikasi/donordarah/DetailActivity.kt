package com.aplikasi.donordarah

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aplikasi.donordarah.databinding.ActivityDetailBinding
import com.aplikasi.donordarah.databinding.ActivityLoginBinding
import com.aplikasi.donordarah.databinding.ActivityMainBinding
import com.aplikasi.donordarah.fragment.BerandaFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var firestore: FirebaseFirestore
    private var getLat: Double? = null
    private var getLong: Double? = null
    private var getTitle: String? = null
    private var placesId: String? = null
    private var getType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        iv_backToHome.setOnClickListener {
            finish()
        }

        FirebaseApp.initializeApp(this)

        // val mapFragment =
        // supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        //mapFragment!!.getMapAsync(this)

        placesId = intent.extras?.getString(KEY_PLACE_ID) ?: throw IllegalArgumentException("Must pass extra $KEY_PLACE_ID")

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        getType = intent.getStringExtra(PLACE_TYPE)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    companion object {
        const val KEY_PLACE_ID = "key_place_id"
        const val PLACE_TYPE = "place_type"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Get reference to the restaurant
        val db = firestore.collection("post")
        db.document(placesId.toString())
            .get()
            .addOnSuccessListener { data ->
                tvNamaLokasi.text = data.getString("nama")
                tvGoldar.text = data.getString("goldar")
                tvAlamat.text = data.getString("kota")
                tvnoTelp.text = data.getString("noHp")
                tvdesk.text = data.getString("deskripsi")
                getLat = data.getString("lat")?.toDouble()
                getLong = data.getString("lng")?.toDouble()
                getTitle = data.getString("alamat")

                mMap = googleMap
                // Add a marker in Sydney and move the camera
                val location = LatLng(getLat ?: 0.0, getLong ?: 0.0)
                mMap.addMarker(MarkerOptions().position(location).title(getTitle.toString()))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))

                btnRute.setOnClickListener {
                    try {
                        val gmmIntentUri: Uri = Uri.parse("google.navigation:q=$getLat,$getLong")
                        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        intent.setPackage("com.google.android.apps.maps")
                        startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(
                            this,
                            "Please install a maps application",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}