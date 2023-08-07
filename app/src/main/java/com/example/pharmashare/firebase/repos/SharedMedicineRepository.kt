package com.example.pharmashare.firebase.repos

import com.example.pharmashare.firebase.objects.SharedMedicine
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object SharedMedicineRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("sharedMedicines")

    // fun insert shared medicine into the database auto generated id (sharedMedicineId)
    fun insertSharedMedicine(sharedMedicine: SharedMedicine, callback: (Boolean) -> Unit) {
        val sharedMedicineId = database.getReference("sharedMedicines").push().key ?: ""
        val newSharedMedicine = SharedMedicine(sharedMedicineId, sharedMedicine.pharmacyName, sharedMedicine.medicineName, sharedMedicine.quantity, sharedMedicine.expiredDate, sharedMedicine.price)

        database.getReference("sharedMedicines").child(sharedMedicineId).setValue(newSharedMedicine)
            .addOnCompleteListener { createSharedMedicineTask ->
                if (createSharedMedicineTask.isSuccessful) {
                    callback(true) // Success
                } else {
                    callback(false) // Handle shared medicine creation failure
                }
            }
    }


    // return all shared medicines where pharmacyId != id as a list
    fun getAllSharedMedicines(id: String, callback: (List<SharedMedicine>) -> Unit) {
        val sharedMedicines = mutableListOf<SharedMedicine>()
        usersRef.get().addOnSuccessListener { sharedMedicinesSnapshot ->
            sharedMedicinesSnapshot.children.forEach { sharedMedicineSnapshot ->
                val sharedMedicine = sharedMedicineSnapshot.getValue(SharedMedicine::class.java)
                if (sharedMedicine != null && sharedMedicine.pharmacyName != id) {
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

    // increase shared medicine quantity
    fun increaseSharedMedicineQuantity(id: String, quantity: Int) {
        usersRef.child(id).child("quantity").setValue(quantity + 1)
    }

    // decrease shared medicine quantity
    fun decreaseSharedMedicineQuantity(id: String, quantity: Int) {
        usersRef.child(id).child("quantity").setValue(quantity - 1)
    }


}