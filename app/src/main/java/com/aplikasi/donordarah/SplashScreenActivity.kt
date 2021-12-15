package com.aplikasi.donordarah

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        checkUserLogin()
    }

    private fun checkUserLogin() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                val thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(2500)
                        } catch (ex: InterruptedException) {
                            ex.printStackTrace()
                        } finally {
                            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
                thread.start()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)
    }
}