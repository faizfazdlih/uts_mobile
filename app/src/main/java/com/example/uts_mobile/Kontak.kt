package com.example.uts_mobile

sealed class KontakItem {
    data class Header(val letter: String) : KontakItem()
    data class Contact(val kontak: Kontak) : KontakItem()
}

data class Kontak(
    val nama: String,
    val telepon: String,
    val foto: Int
)