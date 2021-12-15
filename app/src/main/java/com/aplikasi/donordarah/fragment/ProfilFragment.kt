package com.aplikasi.donordarah.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.aplikasi.donordarah.DetailActivity
import com.aplikasi.donordarah.EditActivity
import com.aplikasi.donordarah.LoginActivity
import com.aplikasi.donordarah.databinding.FragmentProfilBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logout.setOnClickListener {
            val alert = context?.let { it1 -> MaterialAlertDialogBuilder(it1) }
            alert?.setTitle("Konfirmasi")
                ?.setMessage("Anda yakin ingin keluar?")
                ?.setPositiveButton("Ya") { dialogInterface, i ->
                    context?.let { it1 ->
                        AuthUI.getInstance()
                            .signOut(it1)
                            .addOnCompleteListener{
                                startActivity(Intent(context, LoginActivity::class.java))
                                (context as AppCompatActivity).finish()
                            }
                    }

                }
                ?.setNegativeButton("Tidak") { dialogInterface, i -> dialogInterface.dismiss()}
            alert?.create()
            alert?.show()
        }
        binding.btnEdit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(EditActivity.NAMA_ID, binding.tvNama.text.toString())
            intent.putExtra(EditActivity.KOTA_ID, binding.tvKota.text.toString())
            intent.putExtra(EditActivity.GOLDAR_ID, binding.tvGoldar.text.toString())
            intent.putExtra(EditActivity.EMAIL_ID, binding.tvEmail.text.toString())
            intent.putExtra(EditActivity.JK_ID, binding.tvJk.text.toString())
            intent.putExtra(EditActivity.NOHP_ID, binding.tvNohp.text.toString())
            (context as AppCompatActivity).startActivity(intent)
        }
        showUserProfile()
    }
    private fun showUserProfile()= with(binding) {
        binding.swipeRefresh?.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())

        db.addSnapshotListener { it, _ ->

            tvNama.text = it?.getString("namaLengkap").toString()
            tvKota.text = it?.getString("kota").toString()
            tvGoldar.text = it?.getString("goldar").toString()
            tvEmail.text = it?.getString("email").toString()
            tvJk.text = it?.getString("kelamin").toString()
            tvNohp.text = it?.getString("noHp").toString()
            swipeRefresh.isRefreshing = false
        }
    }
}