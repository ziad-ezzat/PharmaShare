package com.example.pharmashare.screens.adptars

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pharmashare.R
import com.example.pharmashare.database.entities.Order
import java.text.DateFormat

class profileAdaptar() : RecyclerView.Adapter<profileAdaptar.Holder>(),Parcelable {

    var arrayList: ArrayList<Order> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(parcel: Parcel) : this() {

    }

    class Holder(itemView: View) : ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.Order_username)
//        val userName: TextView = itemView.findViewById(R.id.Order_username)
        val TotalPrice: TextView = itemView.findViewById(R.id.Order_price)
        val date_order: TextView = itemView.findViewById(R.id.Order_date)
        val datails: TextView = itemView.findViewById(R.id.Order_datails)
        val stutes: TextView = itemView.findViewById(R.id.Order_statues)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ordercard, parent, false
            )
        )
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dateFormat:DateFormat = DateFormat.getInstance()
        holder.userName.text = arrayList[position].pharmacyId.toString()
        holder.datails.text = arrayList[position].orderDetails
        holder.date_order.text = dateFormat.format(arrayList[position].orderDate)
        holder.TotalPrice.text = arrayList[position].totalPrice.toString()
        holder.stutes.text = arrayList[position].status
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<profileAdaptar> {
        override fun createFromParcel(parcel: Parcel): profileAdaptar {
            return profileAdaptar(parcel)
        }

        override fun newArray(size: Int): Array<profileAdaptar?> {
            return arrayOfNulls(size)
        }
    }
}