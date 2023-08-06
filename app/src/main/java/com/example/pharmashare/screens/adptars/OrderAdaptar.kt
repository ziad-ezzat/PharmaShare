package com.example.pharmashare.screens.adptars

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.database.entities.SharedMedicine

class OrderAdaptar(arr1 :ArrayList<SharedMedicine>) :RecyclerView.Adapter<OrderAdaptar.Holder>(),Parcelable {
    var arr :ArrayList<SharedMedicine> = arr1
    var unitPrice:Double = 0.0
    val totalPrice:MutableList<Double> = MutableList(arr.size){
        0.0
    }
    var connectFragment1:connectFragment? = null

    constructor(parcel: Parcel) : this(arrayListOf()) {

    }

    class Holder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val medicineName:TextView = itemView.findViewById(R.id.order_med_name)
        val pharmacyName:TextView = itemView.findViewById(R.id.order_pharmacy_name)
        val medicineAvaliable:TextView = itemView.findViewById(R.id.order_med_avaliable)
        val medicinePrice:TextView = itemView.findViewById(R.id.order_med_price)
        val medicineQuantity:TextView = itemView.findViewById(R.id.order_med_quantity)
        val addButton:ImageButton = itemView.findViewById(R.id.order_plus_btn)
        val minusButton:ImageButton = itemView.findViewById(R.id.order_minus_btn)
        var quentity = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        LayoutInflater.from(parent.context).inflate(R.layout.ordercard,parent,false)
    )

    override fun getItemCount(): Int  = arr.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        setDate(holder, position)
        totalPrice[position] = arr[position].price * holder.quentity
        connectFragment1?.addTotal(totalPrice.sum())
        holder.medicineName.text = "Medicine name is ${arr[position].medicineId}"
        holder.pharmacyName.text = "Pharmacy name is ${arr[position].fromPharmacyId}"
        holder.addButton.setOnClickListener{
            if (holder.quentity<arr[position].quantity){
                holder.addButton.isEnabled = true
                holder.quentity++
                println("add ${holder.quentity}")
                setDate(holder,position)
                unitPrice = arr[position].price * holder.quentity
                totalPrice[position] = unitPrice
                connectFragment1?.addTotal(totalPrice.sum())
            }else{
                holder.addButton.isEnabled = false
            }
        }
        holder.minusButton.setOnClickListener{
            if (holder.quentity>1){
                holder.minusButton.isEnabled = true
                holder.quentity--
                println("minus ${holder.quentity}")
                setDate(holder, position)
                unitPrice = arr[position].price * holder.quentity
                totalPrice[position]=unitPrice
                connectFragment1?.addTotal(totalPrice.sum())
            }else{
                holder.minusButton.isEnabled = false
            }
        }

    }

    private fun setDate(
        holder: Holder,
        position: Int
    ) {
        holder.medicineAvaliable.text =
            "available Medicine: ${arr[position].quantity - holder.quentity}"
        holder.medicinePrice.text = "price: ${arr[position].price * holder.quentity}"
        holder.medicineQuantity.text = holder.quentity.toString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<OrderAdaptar> {
        override fun createFromParcel(parcel: Parcel): OrderAdaptar {
            return OrderAdaptar(parcel)
        }

        override fun newArray(size: Int): Array<OrderAdaptar?> {
            return arrayOfNulls(size)
        }
    }
    interface connectFragment{
        fun addTotal(total:Double)
    }
}