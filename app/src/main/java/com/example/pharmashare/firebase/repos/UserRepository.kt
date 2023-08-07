package com.example.pharmashare.firebase.repos

import com.example.pharmashare.firebase.objects.Pharmacy
import com.example.pharmashare.firebase.objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

object UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("users")

    // Create a user in Firebase database
    fun createUser(user: User, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword("${user.phoneNumber}@example.com", user.password)
            .addOnCompleteListener { createUserTask ->
                if (createUserTask.isSuccessful) {
                    val id = auth.currentUser?.uid ?: ""
                    val subscriptionDate = calculateSubscriptionDate()

                    val newUser = User(id, user.username, user.phoneNumber, user.password, subscriptionDate)
                    usersRef.child(id).setValue(newUser)
                        .addOnCompleteListener { createUserDbTask ->
                            if (createUserDbTask.isSuccessful) {
                                callback(true, null) // Success
                            } else {
                                callback(false, createUserDbTask.exception?.message) // Handle user creation failure
                            }
                        }
                } else {
                    callback(false, createUserTask.exception?.message) // Handle authentication failure
                }
            }
    }

    // create a pharmacy in Firebase database where the user is the owner
    fun createPharmacy(pharmacy: Pharmacy, callback: (Boolean, String?) -> Unit) {
        if (auth.currentUser != null) {
            val pharmacyId = database.getReference("pharmacies").push().key ?: ""
            val newPharmacy = Pharmacy(pharmacyId, pharmacy.name, pharmacy.address, auth.currentUser!!.uid)

            database.getReference("pharmacies").child(pharmacyId).setValue(newPharmacy)
                .addOnCompleteListener { createPharmacyTask ->
                    if (createPharmacyTask.isSuccessful) {
                        callback(true, null) // Success
                    } else {
                        callback(false, createPharmacyTask.exception?.message) // Handle pharmacy creation failure
                    }
                }
        } else {
            callback(false, "User not authenticated") // Handle user not authenticated
        }
    }


    // Check user login with phone number and password
    fun checkLogin(phoneNumber: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword("${phoneNumber}@example.com", password)
            .addOnCompleteListener { loginTask ->
                if (loginTask.isSuccessful) {
                    callback(true, null) // Success
                } else {
                    val exception = loginTask.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        callback(false, "Invalid phone number or password")
                    } else {
                        callback(false, exception?.message)
                    }
                }
            }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Get the current user id
    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }

    // create user and get user id to create pharmacy and put user id in owner id in pharmacy
    fun createUserAndPharmacy(user: User, pharmacy: Pharmacy, callback: (Boolean, String?) -> Unit) {
        createUser(user) { success, message ->
            if (success) {
                createPharmacy(pharmacy) { success, message ->
                    if (success) {
                        callback(true, null) // Success
                    } else {
                        callback(false, message) // Handle pharmacy creation failure
                    }
                }
            } else {
                callback(false, message) // Handle user creation failure
            }
        }
    }

    // Calculate the subscription date by adding 10 days to the current date
    private fun calculateSubscriptionDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = Date()
        val c = Calendar.getInstance()
        c.time = currentDate
        c.add(Calendar.DATE, 10)
        return sdf.format(c.time)
    }
}