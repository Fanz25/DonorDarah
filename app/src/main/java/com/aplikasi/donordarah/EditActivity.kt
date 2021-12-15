package com.aplikasi.donordarah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.View.GONE
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aplikasi.donordarah.databinding.ActivityEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mEmail: String? = null
    private val kota = arrayOf("Cianjur", "Bandung", "Jakarta", "Depok")
    private val Goldar = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    private val Kelamin = arrayOf("Laki-laki", "Perempuan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        binding = ActivityEditBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser

        val kotaAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            kota
        )
        binding.inputKota.setAdapter(kotaAdapter)

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

        binding.progressBar.visibility = View.GONE

        val root = binding.root
        setContentView(root)
        inisialisasi()
    }

    companion object {
        const val NAMA_ID = "nama_id"
        const val KOTA_ID = "kota_id"
        const val GOLDAR_ID = "goldar_id"
        const val EMAIL_ID = "email_id"
        const val JK_ID = "jk_id"
        const val NOHP_ID = "nohp_id"
    }

    private fun inisialisasi() = with(binding) {
        inputNama.setText(intent.getStringExtra(NAMA_ID).toString())
        inputKota.setText(intent.getStringExtra(KOTA_ID).toString())
        inputGoldar.setText(intent.getStringExtra(GOLDAR_ID).toString())
        inputEmail.setText(intent.getStringExtra(EMAIL_ID).toString())
        inputKelamin.setText(intent.getStringExtra(JK_ID).toString())
        inputNohp.setText(intent.getStringExtra(NOHP_ID).toString())

        binding.btnEdit.setOnClickListener {

            if (binding.inputEmail.text.toString().isEmpty() || binding.inputPass.toString()
                    .isEmpty() || binding.inputNama.toString().isEmpty() ||
                binding.inputNohp.text.toString().isEmpty() || binding.inputKota.text.toString()
                    .isEmpty() || binding.inputKelamin.text.toString().isEmpty() ||
                binding.inputRetype.text.toString().isEmpty() || binding.inputGoldar.text.toString()
                    .isEmpty()
            ) {

                Toast.makeText(
                    applicationContext, "Harap isi kolom.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE

            } else if (binding.inputRetype.text.toString() == binding.inputPass.text.toString()) {
                mEmail= binding.inputEmail.text.toString()
                user?.updateEmail(
                    mEmail.toString()
                )

                    ?.addOnCompleteListener(this@EditActivity) { task ->
                            user?.updatePassword(
                                binding.inputPass.text.toString()
                            )
                                ?.addOnCompleteListener(this@EditActivity) {

                                    val mNama = binding.inputNama.text.toString()
                                        val mKota = binding.inputKota.text.toString()
                                        val mKelamin = binding.inputKelamin.text.toString()
                                        val mNohp = binding.inputNohp.text.toString()
                                        val mEmail = binding.inputEmail.text.toString()
                                        val mpass = binding.inputPass.text.toString()
                                        val mRetype = binding.inputRetype.text.toString()
                                        val mgoldar = binding.inputGoldar.text.toString()

                                            binding.progressBar.visibility = View.VISIBLE
                                            val dataModel = UserModel()
                                            dataModel.namaLengkap = mNama
                                            dataModel.email = mEmail
                                            dataModel.goldar = mgoldar
                                            dataModel.kelamin = mKelamin
                                            dataModel.kota = mKota
                                            dataModel.noHp = mNohp
                                            dataModel.password = mpass
                                            dataModel.retype = mRetype
                                            dataModel.uid =
                                                FirebaseAuth.getInstance().currentUser?.uid.toString()

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
                                                    donorModel.uid =
                                                        FirebaseAuth.getInstance().currentUser?.uid.toString()

                                                    val db1 = FirebaseFirestore.getInstance()
                                                    db1.collection("donor")
                                                        .document(mKota)
                                                        .collection(mgoldar)
                                                        .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                                        .set(donorModel)
                                                        .addOnSuccessListener {
                                                            binding.progressBar.visibility = GONE
                                                            Toast.makeText(
                                                                applicationContext,
                                                                "Edit Berhasil",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                        .addOnFailureListener {
                                                            binding.progressBar.visibility = GONE
                                                            Toast.makeText(
                                                                applicationContext,
                                                                it.localizedMessage?.toString(),
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                }
                                                .addOnFailureListener {
                                                    binding.progressBar.visibility = GONE
                                                    Toast.makeText(
                                                        applicationContext,
                                                        it.localizedMessage?.toString(),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                }
                                ?.addOnFailureListener{
                                    binding.progressBar.visibility = GONE
                                    Toast.makeText(
                                        applicationContext,
                                        it.localizedMessage?.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                        } ?.addOnFailureListener {
                            // If update fails, display a message to the user.
                            Toast.makeText(
                                baseContext, it.localizedMessage.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = GONE
                        }
                    }else {
                Toast.makeText(
                    applicationContext, "Password Tidak Sama.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = GONE
            }
            }
        }
    }

