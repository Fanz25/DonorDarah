package com.aplikasi.donordarah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.aplikasi.donordarah.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var auth: FirebaseAuth? = null
    private var usercollection = Firebase.firestore.collection("user")
    private val getEmail: String? = null
    private var getPassword: kotlin.String? = null

    private val Goldar = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    private val Kelamin = arrayOf("Laki-laki", "Perempuan")
    private val kota = arrayOf("Cianjur", "Bandung", "Jakarta", "Depok")
    private var showProgress: MutableLiveData<Boolean> = MutableLiveData(false)
    private var errorMap: MutableLiveData<Map<String, Any>> =
        MutableLiveData(mutableMapOf("message" to "", "isError" to false))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val goldarAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            Goldar
        )
        binding.inputGoldar.setAdapter(goldarAdapter)

        val kelaminAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            Kelamin
        )
        binding.inputKelamin.setAdapter(kelaminAdapter)

        val kotaAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            kota
        )
        binding.inputKota.setAdapter(kotaAdapter)

        binding.progressBar.visibility=GONE

        val root = binding.root
        setContentView(root)
        inisialisasi()
    }
    
    private fun inisialisasi() = with(binding) {
        binding.btnDaftar.setOnClickListener {

            if (binding.inputEmail.text.toString().isEmpty() || binding.inputPass.toString().isEmpty() || binding.inputNama.toString().isEmpty() ||
                    binding.inputNohp.text.toString().isEmpty() || binding.inputKota.text.toString().isEmpty() || binding.inputKelamin.text.toString().isEmpty() ||
                    binding.inputRetype.text.toString().isEmpty() || binding.inputGoldar.text.toString().isEmpty()) {

                Toast.makeText(
                    applicationContext, "Harap isi kolom.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility=GONE

            } else if (binding.inputRetype.text.toString() == binding.inputPass.text.toString()) {

                auth?.createUserWithEmailAndPassword(
                    binding.inputEmail.text.toString(),
                    binding.inputPass.text.toString()
                )
                    ?.addOnCompleteListener(this@RegisterActivity) { task ->
                        if (task.isSuccessful) {
                            val mNama = binding.inputNama.text.toString()
                            val mKota = binding.inputKota.text.toString()
                            val mKelamin = binding.inputKelamin.text.toString()
                            val mNohp = binding.inputNohp.text.toString()
                            val mEmail = binding.inputEmail.text.toString()
                            val mpass = binding.inputPass.text.toString()
                            val mRetype = binding.inputRetype.text.toString()
                            val mgoldar = binding.inputGoldar.text.toString()

                            if(mNama.isEmpty() || mKota.isEmpty() || mKelamin.isEmpty() || mgoldar.isEmpty() ||
                                    mNohp.isEmpty() ||mEmail.isEmpty() || mpass.isEmpty() || mRetype.isEmpty()){
                                Toast.makeText(
                                    applicationContext,
                                    "Harap Isi Dengan Sesuai",
                                Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                binding.progressBar.visibility= View.VISIBLE
                                val dataModel = UserModel()
                                dataModel.namaLengkap = mNama
                                dataModel.email = mEmail
                                dataModel.goldar = mgoldar
                                dataModel.kelamin = mKelamin
                                dataModel.kota = mKota
                                dataModel.noHp = mNohp
                                dataModel.password = mpass
                                dataModel.retype = mRetype
                                dataModel.uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

                                val db = FirebaseFirestore.getInstance()
                                db.collection("users")
                                    .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                    .set(dataModel)
                                    .addOnSuccessListener {
                                        val donorModel = DonorModel()
                                        donorModel.nama = mNama
                                        donorModel.kota = mKota
                                        donorModel.noHp = mNohp
                                        donorModel.goldar = mgoldar
                                        donorModel.uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

                                        val db1 = FirebaseFirestore.getInstance()
                                        db1.collection("donor")
                                            .document(mKota)
                                            .collection(mgoldar)
                                            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                            .set(donorModel)
                                            .addOnSuccessListener {
                                                binding.progressBar.visibility= GONE
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Registrasi Berhasil",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                                finish()
                                            }

                                            .addOnFailureListener{
                                                binding.progressBar.visibility = GONE
                                                Toast.makeText(
                                                    applicationContext,
                                                    it.localizedMessage?.toString(),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    .addOnFailureListener{
                                        binding.progressBar.visibility = GONE
                                        Toast.makeText(
                                        applicationContext,
                                        it.localizedMessage?.toString(),
                                        Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                applicationContext, "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility=GONE
                        }
                    }

            } else {
                Toast.makeText(
                    baseContext, "Password tidak sama.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility=GONE
            }
        }
    }
}