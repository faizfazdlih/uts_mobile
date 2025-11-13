package com.example.uts_mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BeritaAdapter(
    private val listBerita: List<Berita>,
    private val onClick: (Berita) -> Unit
) : RecyclerView.Adapter<BeritaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgBerita: ImageView = view.findViewById(R.id.imgBerita)
        val tvJudul: TextView = view.findViewById(R.id.tvJudul)
        val tvRingkasan: TextView = view.findViewById(R.id.tvRingkasan)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_berita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val berita = listBerita[position]
        holder.imgBerita.setImageResource(berita.gambar)
        holder.tvJudul.text = berita.judul
        holder.tvRingkasan.text = berita.ringkasan
        holder.tvCategory.text = berita.kategori

        holder.itemView.setOnClickListener { onClick(berita) }
    }

    override fun getItemCount() = listBerita.size
}

data class Berita(
    val judul: String,
    val ringkasan: String,
    val isi: String,
    val gambar: Int,
    val kategori: String = "Umum"
)