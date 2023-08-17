package com.example.pharmashare.app.adptars

import android.app.AlertDialog
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
        holder.orderPrice.text = "Total Order Price : " + profileItem.totalPrice.toString()
        holder.orderDate.text = "in :" + profileItem.orderDate
        holder.orderDetails.setOnClickListener {
            // Show order details in a dialog
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Order Details")
                .setMessage(profileItem.orderDetails)
                .setPositiveButton("OK") { dialog, _ ->
                    // Dismiss the dialog when OK is clicked
                    dialog.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        holder.orderStatus.text = profileItem.status
    }


    override fun getItemCount(): Int = profileItems.size
}
