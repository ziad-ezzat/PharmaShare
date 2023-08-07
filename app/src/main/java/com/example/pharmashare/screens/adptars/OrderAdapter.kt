package com.example.pharmashare.screens.adptars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Cart
import com.google.firebase.database.FirebaseDatabase

class OrderAdapter(private val cartItems: MutableList<Cart>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private val totalLiveData = MutableLiveData<Double>()

    init {
        calculateTotalPrice()
    }

    fun getTotalLiveData(): LiveData<Double> {
        return totalLiveData
    }

    private fun calculateTotalPrice() {
        val total = cartItems.sumByDouble { it.priceTotal }
        totalLiveData.value = total
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineName: TextView = itemView.findViewById(R.id.mrdicine_name)
        val price: TextView = itemView.findViewById(R.id.price)
        val quantity: TextView = itemView.findViewById(R.id.order_med_quantity)
        val plusButton: ImageButton = itemView.findViewById(R.id.order_plus_btn)
        val minusButton: ImageButton = itemView.findViewById(R.id.order_minus_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ordercard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.medicineName.text = cartItem.medicine
        holder.price.text = cartItem.priceTotal.toString()
        holder.quantity.text = cartItem.quantity.toString()

        holder.plusButton.setOnClickListener {
            cartItem.quantity += 1
            cartItem.priceTotal = cartItem.price * cartItem.quantity
            holder.quantity.text = cartItem.quantity.toString()
            holder.price.text = cartItem.priceTotal.toString()
            calculateTotalPrice()
            updateCartItem(cartItem)
            notifyItemChanged(position)
        }

        holder.minusButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                cartItem.priceTotal = cartItem.price * cartItem.quantity
                holder.quantity.text = cartItem.quantity.toString()
                holder.price.text = cartItem.priceTotal.toString()
                calculateTotalPrice()
                updateCartItem(cartItem)
                notifyItemChanged(position)
            }
        }
    }

    private fun updateCartItem(cartItem: Cart) {
        val cartRef = FirebaseDatabase.getInstance().getReference("carts").child(cartItem.id)
        cartRef.setValue(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}