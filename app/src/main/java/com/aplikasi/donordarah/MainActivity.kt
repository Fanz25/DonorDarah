package com.aplikasi.donordarah

import android.Manifest
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aplikasi.donordarah.databinding.ActivityMainBinding
import com.aplikasi.donordarah.fragment.BerandaFragment
import com.aplikasi.donordarah.fragment.CariPendonorFragment
import com.aplikasi.donordarah.fragment.ProfilFragment
import com.aplikasi.donordarah.fragment.RequestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var pageContent: Fragment? = BerandaFragment()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        init(savedInstanceState)
        pageContent?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_pager, it)
                .commit()
        }
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.berand -> pageContent = BerandaFragment()
            R.id.request_blood -> pageContent = RequestFragment()
            R.id.blood_search -> pageContent = CariPendonorFragment()
            R.id.profile -> pageContent = ProfilFragment()
        }
        pageContent?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_pager, it)
                .commit()
        }
        return true
    }

    private fun init(savedInstanceState: Bundle?){
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            pageContent?.let {
                supportFragmentManager.beginTransaction().replace(R.id.view_pager, it).commit()
            }
        }else{
            pageContent = supportFragmentManager.getFragment(savedInstanceState, "1")
            pageContent?.let {
                supportFragmentManager.beginTransaction().replace(R.id.view_pager, it).commit()
            }
        }
    }
}