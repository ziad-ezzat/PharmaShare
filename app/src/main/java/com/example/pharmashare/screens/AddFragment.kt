package com.example.pharmashare.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pharmashare.R
import java.util.Calendar
import java.util.Date


class AddFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayList:ArrayList<Int> = arrayListOf(1,5,4)
        val adaptar: ArrayAdapter<Int> =
            ArrayAdapter(view.context, android.R.layout.simple_dropdown_item_1line,arrayList)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val editPrice: EditText = view.findViewById(R.id.fragment_price)
        val editQuantaty: EditText = view.findViewById(R.id.fragment_quantaty)
        val calendarTv: TextView = view.findViewById(R.id.fragment_calendar)
        val add: Button = view.findViewById(R.id.fragment_add_medicine)
        spinner.adapter = adaptar
        add.setOnClickListener {
            println("${editPrice.text} ${editQuantaty.text} ${calendarTv.text} ${spinner.selectedItem is Int}")
        }
        calendarTv.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
             DatePickerDialog(
                view.context, { p0, year, month, day ->
                    calendar.set(Calendar.YEAR,year)
                    calendar.set(Calendar.MONTH,month)
                    calendar.set(Calendar.DAY_OF_MONTH,day)
                    calendarTv.text = "$year/${month+1}/$day"
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment().apply {

            }
    }
}