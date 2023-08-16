package com.example.pharmashare.database.firebase.repos

import com.example.pharmashare.database.objects.Pharmacy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object PharmacyRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val pharmaciesRef: DatabaseReference = database.getReference("pharmacies")

    fun insertPharmacy(pharmacyName: String, pharmacyAddress: String, callback: (Boolean) -> Unit) {
        if (auth.currentUser != null) {
            val pharmacyId = pharmaciesRef.push().key ?: ""
            val newPharmacy = Pharmacy(pharmacyId, pharmacyName, pharmacyAddress, auth.currentUser!!.uid)

            pharmaciesRef.child(pharmacyId).setValue(newPharmacy)
                .addOnCompleteListener { createPharmacyTask ->
                    if (createPharmacyTask.isSuccessful) {
                        callback(true) // Success
                    } else {
                        callback(false)
                    }
                }
        } else {
            callback(false)
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

    // return pharmacy address by name
    fun getPharmacyAddressByName(name: String, callback: (String) -> Unit) {
        pharmaciesRef.get().addOnSuccessListener { pharmaciesSnapshot ->
            pharmaciesSnapshot.children.forEach { pharmacySnapshot ->
                val pharmacy = pharmacySnapshot.getValue(Pharmacy::class.java)
                if (pharmacy != null && pharmacy.name == name) {
                    callback(pharmacy.address)
                }
            }
        }
    }
}