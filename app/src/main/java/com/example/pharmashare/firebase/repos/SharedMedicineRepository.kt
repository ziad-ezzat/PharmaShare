package com.example.pharmashare.firebase.repos

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object SharedMedicineRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("sharedMedicines")

    // fun insert shared medicine
    fun insertSharedMedicine(name: String, id: String) {
        usersRef.child(id).setValue(name)
    }

    // fun get all shared medicines where pharmacyId != id
    fun getSharedMedicines(id: String, callback: (ArrayList<String>) -> Unit) {
        val sharedMedicines = ArrayList<String>()
        usersRef.get().addOnSuccessListener { sharedMedicinesSnapshot ->
            sharedMedicinesSnapshot.children.forEach { sharedMedicineSnapshot ->
                val sharedMedicine = sharedMedicineSnapshot.getValue(String::class.java)
                if (sharedMedicine != null && sharedMedicine != id) {
                    sharedMedicines.add(sharedMedicine)
                }
            }
            callback(sharedMedicines)
        }
    }

    // remove shared medicine
    fun removeSharedMedicine(id: String) {
        usersRef.child(id).removeValue()
    }

}