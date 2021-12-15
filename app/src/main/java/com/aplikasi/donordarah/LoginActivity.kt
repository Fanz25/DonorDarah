package com.aplikasi.donordarah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplikasi.donordarah.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient : GoogleSignInClient

    private var auth: FirebaseAuth? = null
    private val getEmail: String? = null
    private var getPassword: kotlin.String? = null
    private var gso: GoogleSignInOptions? = null
    private val RC_SIGN_IN = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        inisialisasi()
//        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
 //           .build()

 //       googleSignInClient = gso?.let { it1 -> GoogleSignIn.getClient(this@LoginActivity, it1) }!!
    }

 //   private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
 //       startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

  //  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
 //       super.onActivityResult(requestCode, resultCode, data)

  //      if (requestCode == RC_SIGN_IN){
  //          val task = GoogleSignIn.getSignedInAccountFromIntent(data)
 //           try {
 //               val account = task.getResult(ApiException :: class.java)!!
  //              firebaseAuthWithGoogle(account.idToken!!)
 //           }catch (e: ApiException){
  //              Toast.makeText(
 //                   baseContext, "Terjadi Kesalahan.",
 //                   Toast.LENGTH_SHORT
  //              ).show()
  //          }
  //      }
 //   }

//   private fun firebaseAuthWithGoogle(idToken: String) {
  //      val credential = GoogleAuthProvider.getCredential(idToken, null)
   //     auth?.signInWithCredential(credential)
   //         ?.addOnCompleteListener(this) { task ->
   //             if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
  //                  startActivity(Intent(this@LoginActivity, MainActivity::class.java))
 //                   finish()
                    //val user = auth!!.currentUser
  //              } else {
 //                   Toast.makeText(
 //                       baseContext, "Terjadi Kesalahan, Silahkan Coba Lagi.",
  //                      Toast.LENGTH_SHORT
  //                  ).show()
 //               }
 //           }
 //   }

    private fun inisialisasi () = with (binding) {
        binding.btnLogin.setOnClickListener {

                if(binding.inputEmail.text.toString().isEmpty() || binding.inputPass.text.toString().isEmpty()) {

                    Toast.makeText(
                        baseContext, "Harap Isi Email atau Password.",
                        Toast.LENGTH_SHORT
                    ).show()


                }else {
                    auth?.signInWithEmailAndPassword(
                        binding.inputEmail.text.toString(),
                        binding.inputPass.text.toString()
                    )
                        ?.addOnCompleteListener(this@LoginActivity) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

      //  binding.btnGoogle.setOnClickListener{
     //       signIn()
   //     }
    }
}