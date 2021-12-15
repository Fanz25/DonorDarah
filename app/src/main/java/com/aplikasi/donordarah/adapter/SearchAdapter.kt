package com.aplikasi.donordarah.adapter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.donordarah.DetailActivity
import com.aplikasi.donordarah.DonorModel
import com.aplikasi.donordarah.PostModel
import com.aplikasi.donordarah.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.request_list_item.view.*

class SearchAdapter(
    options: FirestoreRecyclerOptions<DonorModel>
) :
    FirestoreRecyclerAdapter<DonorModel, SearchAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_list_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: DonorModel) {
        var kota = item.kota.toString()
        var noHp = item.noHp.toString()
        var goldar = item.goldar.toString()
        var nama = item.nama.toString()
        var context = holder.itemView.context
        var getId = snapshots.getSnapshot(position).id

        holder.apply {
            containerView.tv_goldar.text = goldar
            containerView.tv_kota.text = kota
            containerView.tv_nohp.text = noHp
            containerView.tv_goldar.text = goldar
            containerView.tv_nama.text = nama
            containerView.layoutClick.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_PLACE_ID, getId)
                (context as AppCompatActivity).startActivity(intent)
            }

        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}