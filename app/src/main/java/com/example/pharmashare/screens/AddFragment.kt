package com.example.pharmashare.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.SharedMedicine
import com.example.pharmashare.firebase.repos.MedicineRepository
import com.example.pharmashare.firebase.repos.PharmacyRepository
import com.example.pharmashare.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.firebase.repos.UserRepository
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class AddFragment : Fragment() {

    private lateinit var medicinespinner: Spinner
    private lateinit var pharmacySpinner: Spinner
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var priceEditText: TextInputEditText
    private lateinit var calendarTextView: TextView
    private lateinit var addMedicineButton: Button

    private val sharedMedicineRepository = SharedMedicineRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add, container, false)

        medicinespinner = rootView.findViewById(R.id.medicine_spinner)
        pharmacySpinner = rootView.findViewById(R.id.pharmacy_spinner)
        quantityEditText = rootView.findViewById(R.id.fragment_quantaty)
        priceEditText = rootView.findViewById(R.id.fragment_price)
        calendarTextView = rootView.findViewById(R.id.fragment_calendar)
        addMedicineButton = rootView.findViewById(R.id.add_medicine)

        // Initialize spinners with data
        initMedicineSpinner()
        initPharmacySpinner()

        // Set up click listeners and add shared medicine
        addMedicineButton.setOnClickListener {
            addSharedMedicine()
        }

        // Set up click listener for calendar
        calendarTextView.setOnClickListener {
            showDatePicker()
        }

        return rootView
    }

    private fun initMedicineSpinner() {
        MedicineRepository.getMedicines { medicines ->
            val medicineNames = medicines.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, medicineNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            medicinespinner.adapter = adapter
        }
    }

    private fun initPharmacySpinner() {
        val userId = UserRepository.getCurrentUserId()
        PharmacyRepository.getAllPharmaciesByOwnerId(userId) { pharmacies ->
            val pharmacyNames = pharmacies.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pharmacyNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            pharmacySpinner.adapter = adapter
        }
    }

    private fun addSharedMedicine() {
        val selectedMedicine = medicinespinner.selectedItem as? String
        val selectedPharmacy = pharmacySpinner.selectedItem as? String
        val quantity = quantityEditText.text.toString()
        val price = priceEditText.text.toString()

        if (selectedMedicine.isNullOrEmpty() || selectedPharmacy.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please select a medicine and a pharmacy", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedMedicine = SharedMedicine(
            id = "",
            medicineName = selectedMedicine,
            pharmacyName = selectedPharmacy,
            quantity = quantity.toIntOrNull() ?: 0,
            price = price.toDoubleOrNull() ?: 0.0,
            expiredDate = calendarTextView.text.toString()
        )

        sharedMedicineRepository.insertSharedMedicine(sharedMedicine) { isSuccess ->
            if (isSuccess) {
                clearFields()
            } else {
                Toast.makeText(requireContext(), "Failed to add medicine", Toast.LENGTH_SHORT).show()
            }
        }

        Toast.makeText(requireContext(), "Medicine added successfully", Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        medicinespinner.setSelection(0)
        pharmacySpinner.setSelection(0)
        quantityEditText.text?.clear()
        priceEditText.text?.clear()
        calendarTextView.text = "Select date"
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        context?.let {
            DatePickerDialog(
                it, { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendarTextView.text = "$year/${month + 1}/$day"
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}