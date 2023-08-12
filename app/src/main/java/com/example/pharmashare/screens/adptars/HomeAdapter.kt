package com.example.pharmashare.screens.adptars

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.SharedMedicine
import com.example.pharmashare.firebase.repos.SharedMedicineRepository

class HomeAdapter(private val sharedMedicines: List<SharedMedicine>, private val homeListener: HomeListener) :
    RecyclerView.Adapter<HomeAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pharmacyNameTv: TextView = itemView.findViewById(R.id.pharmacy_name)
        val medNameTv: TextView = itemView.findViewById(R.id.med_name)
        val expiredDateTv: TextView = itemView.findViewById(R.id.expired_date)
        val medQuantityTv: TextView = itemView.findViewById(R.id.med_quantity)
        val medPriceTv: TextView = itemView.findViewById(R.id.med_price)
        val addToCartBtn: Button = itemView.findViewById(R.id.med_makeOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_card, parent, false)
        return Holder(itemView)
    }

    override fun getItemCount(): Int = sharedMedicines.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val sharedMedicine = sharedMedicines[position]

        holder.pharmacyNameTv.text = sharedMedicine.pharmacyName
        holder.medNameTv.text = sharedMedicine.medicineName
        holder.expiredDateTv.text = sharedMedicine.expiredDate
        holder.medQuantityTv.text = "Quantity: ${sharedMedicine.quantity}"
        holder.medPriceTv.text = "Price: $${sharedMedicine.price}"

        holder.addToCartBtn.setOnClickListener {
            val sharedMedicine = sharedMedicines[position]
            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Choose Quantity")
                .setItems((1..sharedMedicine.quantity).map { it.toString() }.toTypedArray()) { _, which ->
                    val selectedQuantity = which + 1
                    homeListener.addToCart(sharedMedicine, selectedQuantity,sharedMedicine.price)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

    interface HomeListener {
        fun addToCart(sharedMedicine: SharedMedicine , selectedQuantity: Int,price:Double)
    }
}