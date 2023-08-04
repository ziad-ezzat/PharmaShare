package com.example.pharmashare.firebase.repos

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object PharmacyRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("pharmacies")

    // fun insert pharmacy
    fun insertPharmacy(name: String, id: String) {
        usersRef.child(id).setValue(name)
    }

    // fun get all pharmacies that when user login he own a some pharmacies where ownerId = id
    fun getPharmacies(id: String, callback: (ArrayList<String>) -> Unit) {
        val pharmacies = ArrayList<String>()
        usersRef.get().addOnSuccessListener { pharmaciesSnapshot ->
            pharmaciesSnapshot.children.forEach { pharmacySnapshot ->
                val pharmacy = pharmacySnapshot.getValue(String::class.java)
                if (pharmacy != null) {
                    pharmacies.add(pharmacy)
                }
            }
            callback(pharmacies)
        }
    }

    // fun get pharmacy name ref
    fun getPharmacyNameRef(name: String): DatabaseReference {
        return usersRef.child(name)
    }

    // return all pharmacies by name
    fun getAllPharmacies(callback: (ArrayList<String>) -> Unit) {
        val pharmacies = ArrayList<String>()
        usersRef.get().addOnSuccessListener { pharmaciesSnapshot ->
            pharmaciesSnapshot.children.forEach { pharmacySnapshot ->
                val pharmacy = pharmacySnapshot.getValue(String::class.java)
                if (pharmacy != null) {
                    pharmacies.add(pharmacy)
                }
            }
            callback(pharmacies)
        }
    }

}