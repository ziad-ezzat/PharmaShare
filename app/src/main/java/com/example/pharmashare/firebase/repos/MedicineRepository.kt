package com.example.pharmashare.firebase.repos

import com.example.pharmashare.firebase.objects.Medicine
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object MedicineRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("medicines")

    fun insertMedicine(name: String, id: String) {
        val medicine = Medicine(id, name)
        usersRef.child(id).setValue(medicine)
    }

    fun getMedicines(callback: (ArrayList<Medicine>) -> Unit) {
        val medicines = ArrayList<Medicine>()
        usersRef.get().addOnSuccessListener { medicinesSnapshot ->
            medicinesSnapshot.children.forEach { medicineSnapshot ->
                val medicine = medicineSnapshot.getValue(Medicine::class.java)
                if (medicine != null) {
                    medicines.add(medicine)
                }
            }
            callback(medicines)
        }
    }

    fun getMedicineNameRef(name: String): DatabaseReference {
        return usersRef.child(name)
    }

}