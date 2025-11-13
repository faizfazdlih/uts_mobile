package com.example.uts_mobile

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts_mobile.databinding.FragmentBeritaBinding

class BeritaFragment : Fragment() {

    private var _binding: FragmentBeritaBinding? = null
    private val binding get() = _binding!!

    private lateinit var beritaList: List<Berita>
    private lateinit var filteredList: MutableList<Berita>
    private lateinit var adapter: BeritaAdapter
    private var selectedCategory: String = "Semua"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeritaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beritaList = listOf(
            Berita(
                "Anime Demon Slayer Final Arc Siap Tayang!",
                "Arc terakhir segera hadir...",
                "Demon Slayer memasuki saga penutup dengan visual memukau...",
                R.drawable.anime1, "Action"
            ),

            Berita(
                "Cuaca di Negeri Ghibli: Studio Ghibli Rilis Film Baru",
                "Film baru Ghibli siap buat nostalgia...",
                "Cerita baru penuh kehangatan khas Hayao Miyazaki...",
                R.drawable.anime2, "Slice of Life"
            ),

            Berita(
                "My Hero Academia Season Baru Diumumkan",
                "Midoriya dan kawan-kawan kembali...",
                "Season terbaru menjanjikan pertarungan epik...",
                R.drawable.anime3, "Action"
            ),

            Berita(
                "Anime Isekai Baru Banjiri Tahun 2025",
                "Trend isekai semakin naik...",
                "Banyak anime isekai unik dengan konsep segar...",
                R.drawable.anime4, "Isekai"
            ),

            Berita(
                "Kimi no Nawa Masuk Film Anime Terbaik Sepanjang Masa",
                "Makoto Shinkai kembali dipuji...",
                "Kimi no Nawa terus menjadi favorit karena visual dan cerita...",
                R.drawable.anime5, "Romance"
            ),

            Berita(
                "Solo Leveling Anime Season 2 Rilis Trailer Baru",
                "Sung Jin-Woo makin overpower...",
                "Trailer menunjukkan banyak scene penuh aksi intens...",
                R.drawable.anime6, "Action"
            ),

            Berita(
                "Cuaca Buruk Ganggu Syuting Live Action One Piece",
                "Produksi sedikit tertunda...",
                "Tim produksi memastikan kualitas tetap terbaik...",
                R.drawable.anime7, "Slice of Life"
            ),

            Berita(
                "Anime Re:Zero Umumkan Spin-Off",
                "Fans Subaru heboh...",
                "Spin-off fokus pada Emilia dengan cerita masa lalu...",
                R.drawable.anime8, "Isekai"
            ),

            Berita(
                "Transportasi Dunia Anime: Kereta Spirited Away Ikonik!",
                "Kereta tanpa suara kembali viral...",
                "Adegan ini dianggap simbol ketenangan dan makna kehidupan...",
                R.drawable.anime9, "Slice of Life"
            ),

            Berita(
                "Destinasi Anime Tourism di Jepang Meningkat",
                "Fans semakin berdatangan...",
                "Lokasi seperti Akihabara dan Kyoto jadi favorit para otaku...",
                R.drawable.anime10, "Romance"
            )
        )

        filteredList = mutableListOf()

        setupHeroCard()
        setupSearchBar()
        setupFilterButtons()
        setupRecyclerView()

        filterBerita("")
    }

    private fun setupHeroCard() {
        if (beritaList.isNotEmpty()) {
            val hero = beritaList[0]
            binding.imgHero.setImageResource(hero.gambar)
            binding.tvHeroTitle.text = hero.judul
            binding.tvHeroDescription.text = hero.ringkasan
            binding.tvHeroCategory.text = hero.kategori

            binding.cardHero.setOnClickListener { tampilkanDetailBerita(hero) }
        }
    }

    private fun setupRecyclerView() {
        adapter = BeritaAdapter(filteredList) { berita ->
            tampilkanDetailBerita(berita)
        }

        binding.recyclerBerita.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerBerita.isNestedScrollingEnabled = false
        binding.recyclerBerita.adapter = adapter
    }

    private fun setupSearchBar() {
        binding.btnSearch.setOnClickListener {
            if (binding.searchContainer.visibility == View.GONE) {
                binding.searchContainer.visibility = View.VISIBLE
                binding.etSearch.requestFocus()
            } else {
                binding.searchContainer.visibility = View.GONE
                binding.etSearch.setText("")
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBerita(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFilterButtons() {
        val categories = listOf("Semua", "Action", "Slice of Life", "Isekai", "Romance")

        categories.forEach { category ->
            val btn = Button(requireContext()).apply {
                text = category
                textSize = 14f
                isAllCaps = false
                setPadding(12, 6, 12, 6)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = 16 }

                background = resources.getDrawable(R.drawable.bg_category_default, null)
                setTextColor(Color.parseColor("#6B7280"))

                setOnClickListener {
                    selectedCategory = category
                    updateButtonSelection()
                    filterBerita(binding.etSearch.text.toString())
                }
            }
            binding.layoutFilterButtons.addView(btn)
        }

        updateButtonSelection()
    }

    private fun updateButtonSelection() {
        for (i in 0 until binding.layoutFilterButtons.childCount) {
            val btn = binding.layoutFilterButtons.getChildAt(i) as Button

            if (btn.text == selectedCategory) {
                btn.background = resources.getDrawable(R.drawable.bg_category_selected, null)
                btn.setTextColor(Color.WHITE)
            } else {
                btn.background = resources.getDrawable(R.drawable.bg_category_default, null)
                btn.setTextColor(Color.parseColor("#6B7280"))
            }
        }
    }

    private fun filterBerita(query: String) {
        filteredList.clear()

        val listTanpaHero = beritaList.drop(1)

        val filtered = listTanpaHero.filter { berita ->
            val matchCategory = selectedCategory == "Semua" || berita.kategori == selectedCategory
            val matchSearch = query.isBlank() ||
                    berita.judul.contains(query, ignoreCase = true) ||
                    berita.ringkasan.contains(query, ignoreCase = true)

            matchCategory && matchSearch
        }

        filteredList.addAll(filtered)
        adapter.notifyDataSetChanged()

        binding.tvNoData.visibility =
            if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun tampilkanDetailBerita(berita: Berita) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_detail_berita, null)

        dialogView.findViewById<ImageView>(R.id.imgDetail)
            .setImageResource(berita.gambar)
        dialogView.findViewById<TextView>(R.id.tvJudulDetail).text = berita.judul
        dialogView.findViewById<TextView>(R.id.tvIsiDetail).text = berita.isi

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Tutup") { d, _ -> d.dismiss() }
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
