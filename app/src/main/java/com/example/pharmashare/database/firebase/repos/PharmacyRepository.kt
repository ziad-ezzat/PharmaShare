package com.example.pharmashare.database.firebase.repos

import com.example.pharmashare.database.objects.Pharmacy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object PharmacyRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val pharmaciesRef: DatabaseReference = database.getReference("pharmacies")

    fun insertPharmacy(pharmacy: Pharmacy, callback: (Boolean, String?) -> Unit) {
        if (auth.currentUser != null) {
            val pharmacyId = pharmaciesRef.push().key ?: ""
            val newPharmacy = Pharmacy(pharmacyId, pharmacy.name, pharmacy.address, auth.currentUser!!.uid)

            pharmaciesRef.child(pharmacyId).setValue(newPharmacy)
                .addOnCompleteListener { createPharmacyTask ->
                    if (createPharmacyTask.isSuccessful) {
                        callback(true, null) // Success
                    } else {
                        callback(false, createPharmacyTask.exception?.message ?: "Failed to create pharmacy")
                    }
                }
        } else {
            callback(false, "User not authenticated")
        }
    }

    private fun getPharmaciesWithFilter(filter: ((Pharmacy) -> Boolean), callback: (List<Pharmacy>) -> Unit) {
        val pharmacies = mutableListOf<Pharmacy>()

        pharmaciesRef.get().addOnSuccessListener { pharmaciesSnapshot ->
            pharmaciesSnapshot.children.forEach { pharmacySnapshot ->
                val pharmacy = pharmacySnapshot.getValue(Pharmacy::class.java)
                if (pharmacy != null && filter(pharmacy)) {
                    pharmacies.add(pharmacy)
                }
            }
            callback(pharmacies)
        }
    }

    fun getAllPharmaciesByOwnerId(ownerId: String, callback: (List<Pharmacy>) -> Unit) {
        getPharmaciesWithFilter({ it.ownerId == ownerId }, callback)
    }

    fun getAllPharmacies(callback: (List<Pharmacy>) -> Unit) {
        getPharmaciesWithFilter({ true }, callback)
    }

    // return pharmacy id by name
    fun getPharmacyIdByName(name: String, callback: (String) -> Unit) {
        pharmaciesRef.get().addOnSuccessListener { pharmaciesSnapshot ->
            pharmaciesSnapshot.children.forEach { pharmacySnapshot ->
                val pharmacy = pharmacySnapshot.getValue(Pharmacy::class.java)
                if (pharmacy != null && pharmacy.name == name) {
                    callback(pharmacy.id)
                }
            }
        }
    }
}