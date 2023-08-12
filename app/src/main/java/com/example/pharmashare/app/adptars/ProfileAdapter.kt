package com.example.pharmashare.app.adptars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.database.objects.Order

class ProfileAdapter(private val profileItems: List<Order>) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pharmacyName: TextView = itemView.findViewById(R.id.Pharmacy_Name)
        val orderPrice: TextView = itemView.findViewById(R.id.Order_price)
        val orderDate: TextView = itemView.findViewById(R.id.Order_date)
        val orderDetails: TextView = itemView.findViewById(R.id.Order_datails)
        val orderStatus: TextView = itemView.findViewById(R.id.Order_statues)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profilecard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profileItem = profileItems[position]
        holder.pharmacyName.text = profileItem.pharmacyName
        holder.orderPrice.text = profileItem.totalPrice.toString()
        holder.orderDate.text = profileItem.orderDate
        holder.orderDetails.text = profileItem.orderDetails
        holder.orderStatus.text = profileItem.status
    }

    override fun getItemCount(): Int = profileItems.size
}
