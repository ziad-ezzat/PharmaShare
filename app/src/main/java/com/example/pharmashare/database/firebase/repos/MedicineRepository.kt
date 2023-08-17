package com.example.pharmashare.database.firebase.repos

import com.example.pharmashare.database.objects.Medicine
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object MedicineRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("medicines")

    // insert a medicines list to the database make sure that the list has no duplicates before inserting and generate a unique id for each medicine
    fun insertMedicines(medicines: List<String>) {
        val uniqueMedicines = medicines.toSet()
        medicines.forEach { medicineName ->
            val medicineId = generateUniqueId() // Generate a unique ID for each medicine
            val medicine = Medicine(medicineId, medicineName, /* other properties */)
            val medicineRef = usersRef.child(medicineId)
            medicineRef.setValue(medicine)
        }
    }

    fun generateUniqueId(): String {
        // Generate a unique ID using a combination of current timestamp and a random number
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt() // Generate a random number between 0 and 999
        return "medicine_$timestamp$random"
    }


    // get all medicines from the database
    fun getAllMedicines(callback: (List<Medicine>) -> Unit) {
        usersRef.get().addOnSuccessListener { medicinesSnapshot ->
            val medicines = mutableListOf<Medicine>()
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

    // remove all medicines from the database
    fun removeAllMedicines() {
        usersRef.removeValue()
    }

}