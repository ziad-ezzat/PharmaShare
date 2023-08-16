package com.example.pharmashare.app.adptars

import android.app.AlertDialog
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.database.firebase.repos.PharmacyRepository
import com.example.pharmashare.database.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.database.objects.SharedMedicine
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeAdapter(private val sharedMedicines: List<SharedMedicine>, private val homeListener: HomeListener) :
    RecyclerView.Adapter<HomeAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pharmacyNameTv: TextView = itemView.findViewById(R.id.pharmacy_name)
        val medNameTv: TextView = itemView.findViewById(R.id.med_name)
        val expiredDateTv: TextView = itemView.findViewById(R.id.expired_date)
        val medQuantityTv: TextView = itemView.findViewById(R.id.med_quantity)
        val medPriceTv: TextView = itemView.findViewById(R.id.med_price)
        val addToCartBtn: FloatingActionButton = itemView.findViewById(R.id.med_makeOrder)
        val discountTv: TextView = itemView.findViewById(R.id.discount)
        val discountPriceTv: TextView = itemView.findViewById(R.id.med_price_discount)
        val pharmacyAddressTv: TextView = itemView.findViewById(R.id.pharmacy_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_card, parent, false)
        return Holder(itemView)
    }

    override fun getItemCount(): Int = sharedMedicines.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val sharedMedicine = sharedMedicines[position]

        holder.pharmacyNameTv.text = sharedMedicine.pharmacyName
        PharmacyRepository.getPharmacyAddressByName(sharedMedicine.pharmacyName) { pharmacy ->
            holder.pharmacyAddressTv.text = pharmacy
        }
        holder.medNameTv.text = sharedMedicine.medicineName
        holder.expiredDateTv.text = sharedMedicine.expiredDate
        holder.medQuantityTv.text = "Quantity: ${sharedMedicine.quantity}"
        holder.medPriceTv.text = "${sharedMedicine.price}"
        holder.discountTv.text = "Discount: ${sharedMedicine.discount}%"
        holder.discountPriceTv.text = sharedMedicine.priceAfterDiscount.toString()
        holder.medPriceTv.paintFlags = holder.medPriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.addToCartBtn.setOnClickListener {
            val sharedMedicine = sharedMedicines[position]
            var pharmacyId = ""
            PharmacyRepository.getPharmacyIdByName(sharedMedicine.pharmacyName) { pharmacy ->
                pharmacyId = pharmacy.replace("-", "") // Remove the hyphen
            }
            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Choose Quantity")
                .setItems((1..sharedMedicine.quantity).map { it.toString() }.toTypedArray()) { _, which ->
                    val selectedQuantity = which + 1
                    val check = homeListener.addToCart(sharedMedicine, selectedQuantity,sharedMedicine.priceAfterDiscount,pharmacyId)
                    if (check)
                    {
                        SharedMedicineRepository.updateSharedMedicineQuantity(sharedMedicine.id, sharedMedicine.quantity - selectedQuantity)
                        holder.medQuantityTv.text = "Quantity: ${sharedMedicine.quantity - selectedQuantity}"
                        sharedMedicine.quantity -= selectedQuantity
                    }
                    else
                    { }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

    interface HomeListener {
        fun addToCart(sharedMedicine: SharedMedicine, selectedQuantity: Int, price:Double,pharmacyId: String): Boolean
    }
}