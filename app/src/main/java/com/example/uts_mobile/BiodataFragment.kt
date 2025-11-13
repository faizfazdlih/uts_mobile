package com.example.uts_mobile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.uts_mobile.databinding.FragmentBiodataBinding
import java.util.*

class BiodataFragment : Fragment() {

    private var _binding: FragmentBiodataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiodataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prodiList = arrayOf("Teknik Informatika", "Sistem Informasi", "Teknik Komputer", "Manajemen Informatika")
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_custom, prodiList)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_custom)
        binding.spinnerProdi.adapter = spinnerAdapter

        binding.radioMale.isClickable = false
        binding.radioFemale.isClickable = false

        binding.cardMale.setOnClickListener {
            if (binding.radioMale.isChecked) {
                binding.radioMale.isChecked = false
                binding.radioFemale.isChecked = false
            } else {
                binding.radioMale.isChecked = true
                binding.radioFemale.isChecked = false
            }
        }

        binding.cardFemale.setOnClickListener {
            if (binding.radioFemale.isChecked) {
                binding.radioMale.isChecked = false
                binding.radioFemale.isChecked = false
            } else {
                binding.radioFemale.isChecked = true
                binding.radioMale.isChecked = false
            }
        }

        binding.cardDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val selectedDate = "$d/${m + 1}/$y"
                binding.tvTanggal.text = selectedDate
                binding.tvTanggal.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            }, year, month, day)

            datePicker.show()


            datePicker.show()
        }

        binding.btnTampil.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val alamat = binding.etAlamat.text.toString().trim()
            val prodi = binding.spinnerProdi.selectedItem.toString()
            val tanggal = binding.tvTanggal.text.toString().trim()

            val gender = if (binding.radioMale.isChecked) "Laki-laki"
            else if (binding.radioFemale.isChecked) "Perempuan"
            else "-"

            if (nama.isEmpty() || alamat.isEmpty() || tanggal == "Select date") {
                val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_incomplete, null)
                val btnClose = dialogView.findViewById<android.widget.Button>(R.id.btnCloseWarning)

                val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .create()

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                btnClose.setOnClickListener { dialog.dismiss() }
                dialog.show()
                return@setOnClickListener
            }

            val dialogBinding = com.example.uts_mobile.databinding.DialogBiodataBinding.inflate(LayoutInflater.from(requireContext()))
            dialogBinding.imgProfile.setImageResource(R.drawable.faiz)
            dialogBinding.tvNama.text = nama
            dialogBinding.tvProdi.text = prodi
            dialogBinding.tvAlamat.text = "Alamat: $alamat"
            dialogBinding.tvGender.text = "Jenis Kelamin: $gender"
            dialogBinding.tvTanggalLahir.text = "Tanggal Lahir: $tanggal"

            val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setView(dialogBinding.root)
                .setPositiveButton("Tutup") { d, _ -> d.dismiss() }
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
