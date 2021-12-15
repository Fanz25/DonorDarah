package com.aplikasi.donordarah.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.donordarah.LinearLayoutManagerWrapper
import com.aplikasi.donordarah.PostModel
import com.aplikasi.donordarah.adapter.RequestAdapter
import com.aplikasi.donordarah.databinding.FragmentBerandaBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class BerandaFragment : Fragment() {
    private var _binding: FragmentBerandaBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData(this)
    }
    private fun requestData(view: BerandaFragment) =  with(binding) {
        val query = FirebaseFirestore.getInstance()
            .collection("post")

        val options = FirestoreRecyclerOptions.Builder<PostModel>()
            .setQuery(query, PostModel::class.java)
            .setLifecycleOwner(view)
            .build()

        postingan.layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)
        postingan.setHasFixedSize(true)

        val adapter = context?.let { RequestAdapter(options) }
        postingan.adapter = adapter
    }
}