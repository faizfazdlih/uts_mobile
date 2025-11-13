package com.example.uts_mobile

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_mobile.databinding.ItemHeaderAlfabetBinding
import com.example.uts_mobile.databinding.ItemKontakBinding

class KontakAdapter(private var kontakItems: MutableList<KontakItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_CONTACT = 1
    }

    inner class HeaderViewHolder(val binding: ItemHeaderAlfabetBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class KontakViewHolder(val binding: ItemKontakBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (kontakItems[position]) {
            is KontakItem.Header -> VIEW_TYPE_HEADER
            is KontakItem.Contact -> VIEW_TYPE_CONTACT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemHeaderAlfabetBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemKontakBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                KontakViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = kontakItems[position]) {

            is KontakItem.Header -> {
                (holder as HeaderViewHolder).binding.tvHeaderAlfabet.text = item.letter
            }

            is KontakItem.Contact -> {
                val kontakHolder = holder as KontakViewHolder

                kontakHolder.binding.tvNama.text = item.kontak.nama
                kontakHolder.binding.tvTelepon.text = item.kontak.telepon

                val context = kontakHolder.itemView.context
                val inputStream = context.resources.openRawResource(item.kontak.foto)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                kontakHolder.binding.ivFoto.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int = kontakItems.size

    fun updateList(newItems: List<KontakItem>) {
        kontakItems.clear()
        kontakItems.addAll(newItems)
        notifyDataSetChanged()
    }
}
