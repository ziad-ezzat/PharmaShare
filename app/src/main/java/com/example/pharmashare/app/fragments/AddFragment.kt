package com.example.pharmashare.app.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pharmashare.R
import com.example.pharmashare.database.objects.SharedMedicine
import com.example.pharmashare.database.firebase.repos.MedicineRepository
import com.example.pharmashare.database.firebase.repos.PharmacyRepository
import com.example.pharmashare.database.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.database.firebase.repos.UserRepository
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class AddFragment : Fragment() {

    private lateinit var medicinespinner: Spinner
    private lateinit var pharmacySpinner: Spinner
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var priceEditText: TextInputEditText
    private lateinit var calendarTextView: TextView
    private lateinit var addMedicineButton: Button
    private lateinit var discountSeekBar: SeekBar
    private lateinit var selectedDiscountTextView: TextView
    private lateinit var priceAfterDiscount: TextView
    private lateinit var filterEditText: EditText

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
        discountSeekBar = rootView.findViewById(R.id.fragment_discount_seekbar)
        selectedDiscountTextView = rootView.findViewById(R.id.fragment_selected_discount)
        priceAfterDiscount = rootView.findViewById(R.id.fragment_price_after_discount)
        filterEditText = rootView.findViewById(R.id.medicine_filter_edittext)

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

        // Set up seek bar listener
        discountSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, discount: Int, fromUser: Boolean) {
                selectedDiscountTextView.text = "$discount%"
                val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
                val priceAfterDiscountValue = price - (price * (discount.toDouble() / 100))
                priceAfterDiscount.text = priceAfterDiscountValue.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

        return rootView
    }

    private fun initMedicineSpinner() {
        val medicineNames = mutableListOf<String>()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, medicineNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        medicinespinner.adapter = adapter


        filterEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        MedicineRepository.getAllMedicines { medicines ->
            medicineNames.addAll(medicines.map { it.name })
            adapter.notifyDataSetChanged()
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
        val userId = UserRepository.getCurrentUserId()
        val discount = discountSeekBar.progress
        var priceAfterDiscount = priceAfterDiscount.text.toString()
        if (discount == 0) {
            priceAfterDiscount = price
        }

        if (selectedMedicine.isNullOrEmpty() || selectedPharmacy.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please select a medicine and a pharmacy", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedMedicine = SharedMedicine(
            id = "",
            userId = userId,
            medicineName = selectedMedicine,
            pharmacyName = selectedPharmacy,
            quantity = quantity.toIntOrNull() ?: 0,
            price = price.toDoubleOrNull() ?: 0.0,
            expiredDate = calendarTextView.text.toString(),
            discount = discount,
            priceAfterDiscount = priceAfterDiscount.toDoubleOrNull() ?: 0.0
        )

        // Build the confirmation dialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Confirm Sharing")
            .setMessage("Are you sure you want to share this medicine?")
            .setPositiveButton("Yes") { _, _ ->
                // User confirmed, proceed to share the medicine
                sharedMedicineRepository.insertSharedMedicine(sharedMedicine) { isSuccess ->
                    if (isSuccess) {
                        clearFields()
                        Toast.makeText(requireContext(), "Medicine shared successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to share medicine", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // User canceled, do nothing
                dialog.dismiss()
            }

        // Show the confirmation dialog
        alertDialogBuilder.create().show()
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