package com.aplikasi.donordarah.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.donordarah.DonorModel
import com.aplikasi.donordarah.LinearLayoutManagerWrapper
import com.aplikasi.donordarah.R
import com.aplikasi.donordarah.adapter.RequestAdapter
import com.aplikasi.donordarah.adapter.SearchAdapter
import com.aplikasi.donordarah.databinding.FragmentCariPendonorBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class CariPendonorFragment : Fragment() {

    private var _binding: FragmentCariPendonorBinding? = null
    private val binding get() = _binding!!
    private val list_kota = arrayOf("Cianjur", "Bandung", "Jakarta", "Depok")
    private val list_Goldar = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCariPendonorBinding.inflate(inflater, container, false)
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
                list_Goldar
            )
        }
        binding.cariGoldar.setAdapter(goldarAdapter)

        val kotaAdapter: ArrayAdapter<String>? = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                list_kota
            )
        }
            binding.cariKota.setAdapter(kotaAdapter)
            binding.progressBar.visibility = View.GONE

            inisialiasai(this)
    }

    private fun inisialiasai(view: CariPendonorFragment) = with(binding) {
        binding.btnCari.setOnClickListener {
            val mGoldar = binding.cariGoldar.text.toString()
            val mKota = binding.cariKota.text.toString()

            if (mGoldar.isEmpty() || mKota.isEmpty()) {
                Toast.makeText(
                    activity, "Harap Isi Kolom",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                val query = FirebaseFirestore.getInstance()
                    .collection("donor")
                    .document(mKota)
                    .collection(mGoldar)

                val options = FirestoreRecyclerOptions.Builder<DonorModel>()
                    .setQuery(query, DonorModel::class.java)
                    .setLifecycleOwner(view)
                    .build()

                dataCari.layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)
                dataCari.setHasFixedSize(true)

                val adapter = context?.let { SearchAdapter(options) }
                dataCari.adapter = adapter
                progressBar.visibility = View.GONE
            }
        }
    }
}

