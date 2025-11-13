package com.example.uts_mobile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts_mobile.databinding.FragmentKontakBinding
import androidx.appcompat.widget.SearchView

class KontakFragment : Fragment() {

    private var _binding: FragmentKontakBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: KontakAdapter
    private lateinit var allKontakItems: List<KontakItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKontakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listKontak = listOf(
            Kontak("Gon Freecss", "08123456789", R.raw.gon),
            Kontak("Killua Zoldyck", "081212121212", R.raw.killua),
            Kontak("Kurapika", "081398765432", R.raw.kurapika),
            Kontak("Leorio Paradinight", "081345678910", R.raw.leorio),
            Kontak("Hisoka Morow", "081234567800", R.raw.hisoka),
            Kontak("Chrollo Lucilfer", "081211223344", R.raw.chrollo),
            Kontak("Neferpitou", "081233344455", R.raw.neferpitou),
            Kontak("Meruem", "081255566677", R.raw.meruem),
            Kontak("Eren Jeager", "081277788899", R.raw.eren),
            Kontak("Kite", "081299900011", R.raw.kite),
            Kontak("Isaac Netero", "081222233344", R.raw.isaac),
            Kontak("Palm Siberia", "081211111111", R.raw.palm),
            Kontak("Shizuku Murasaki", "081333222111", R.raw.shizuku),
            Kontak("Feitan Portor", "081444555666", R.raw.feitan),
            Kontak("Phinks Magcub", "081777888999", R.raw.phinks)
        )

        allKontakItems = createKontakItemsWithHeaders(listKontak)

        adapter = KontakAdapter(allKontakItems.toMutableList())
        binding.recyclerKontak.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerKontak.adapter = adapter

        binding.searchKontak.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterKontak(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterKontak(newText)
                return false
            }
        })

        binding.cardSearch.setOnClickListener {
            binding.searchKontak.isIconified = false
            binding.searchKontak.requestFocus()

            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchKontak, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun createKontakItemsWithHeaders(kontakList: List<Kontak>): List<KontakItem> {
        val items = mutableListOf<KontakItem>()
        val sortedKontak = kontakList.sortedBy { it.nama }

        var currentLetter = ""
        sortedKontak.forEach { kontak ->
            val firstLetter = kontak.nama.first().uppercaseChar().toString()
            if (firstLetter != currentLetter) {
                currentLetter = firstLetter
                items.add(KontakItem.Header(firstLetter))
            }
            items.add(KontakItem.Contact(kontak))
        }

        return items
    }

    private fun filterKontak(query: String?) {
        if (query.isNullOrEmpty()) {
            adapter.updateList(allKontakItems)
        } else {
            val filteredKontak = allKontakItems.filterIsInstance<KontakItem.Contact>()
                .filter { it.kontak.nama.contains(query, ignoreCase = true) }
                .map { it.kontak }

            val filteredItems = createKontakItemsWithHeaders(filteredKontak)
            adapter.updateList(filteredItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
