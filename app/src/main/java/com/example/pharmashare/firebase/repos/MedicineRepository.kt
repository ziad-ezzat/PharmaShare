package com.example.pharmashare.firebase.repos

import com.example.pharmashare.firebase.objects.Medicine
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object MedicineRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("medicines")

    // insert a new medicine into the database auto generated id
    fun insertMedicine(medicine: Medicine, callback: (Boolean, String?) -> Unit) {
        val medicineId = database.getReference("medicines").push().key ?: ""
        val newMedicine = Medicine(medicineId, medicine.name)

        database.getReference("medicines").child(medicineId).setValue(newMedicine)
            .addOnCompleteListener { createMedicineTask ->
                if (createMedicineTask.isSuccessful) {
                    callback(true, null) // Success
                } else {
                    callback(false, createMedicineTask.exception?.message) // Handle medicine creation failure
                }
            }
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

    // get a medicine by name
    fun getMedicineByName(name: String, callback: (Medicine?) -> Unit) {
        usersRef.get().addOnSuccessListener { medicinesSnapshot ->
            medicinesSnapshot.children.forEach { medicineSnapshot ->
                val medicine = medicineSnapshot.getValue(Medicine::class.java)
                if (medicine != null && medicine.name == name) {
                    callback(medicine)
                }
            }
            callback(null)
        }
    }

}