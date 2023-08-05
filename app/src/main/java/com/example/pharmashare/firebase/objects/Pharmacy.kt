package com.example.pharmashare.firebase.objects

data class Pharmacy @JvmOverloads constructor(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val ownerId: String = ""
)
