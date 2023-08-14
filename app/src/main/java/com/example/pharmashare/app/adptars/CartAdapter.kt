package com.example.pharmashare.app.adptars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.pharmashare.R
import com.example.pharmashare.database.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.database.objects.Cart
import com.example.pharmashare.database.room.MyRoomDatabase

class CartAdapter(private val cartItems: MutableList<Cart>, resultBack: ResultBack) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val totalLiveData = MutableLiveData<Double>()
    private val resultBack: ResultBack = resultBack
    lateinit var database: MyRoomDatabase
    val dao by lazy {
        database.cartDao()
    }
    init {
        calculateTotalPrice()
    }

    fun getAllDate(): MutableList<Cart> = cartItems

    private fun calculateTotalPrice() {
        var total = 0.0
        cartItems.forEach {
            total += it.priceTotal
        }
        totalLiveData.value = total
        resultBack.backPrice(total)
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
        database = MyRoomDatabase.buildDatabase(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.medicineName.text = cartItem.medicine
        holder.price.text = cartItem.priceTotal.toString()
        holder.quantity.text = cartItem.quantity.toString()

        if (cartItem.quantity == cartItem.availableQuantity)
            resultBack.checkItIsAvailable(true)
        else {
            resultBack.checkItIsAvailable(false)
        }
        holder.plusButton.setOnClickListener {

            if (cartItem.quantity == cartItem.availableQuantity) {
                resultBack.checkItIsAvailable(true)
            } else {
                resultBack.checkItIsAvailable(false)
            }
            if(cartItem.quantity+1>cartItem.availableQuantity){
                Toast.makeText(holder.itemView.context, "you exceeded available quantity", Toast.LENGTH_SHORT).show()
            }else{
                cartItem.quantity++
                SharedMedicineRepository.decreaseSharedMedicineQuantity(cartItem.sharedMedicineId)
            }
            cartItem.priceTotal = cartItem.price * cartItem.quantity
            holder.quantity.text = cartItem.quantity.toString()
            holder.price.text = cartItem.priceTotal.toString()
            calculateTotalPrice()
            updateCartItem(cartItem)
            notifyItemChanged(position)
            resultBack.backPrice(totalLiveData.value!!)
        }

        holder.minusButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                cartItem.priceTotal = cartItem.price * cartItem.quantity
                SharedMedicineRepository.increaseSharedMedicineQuantity(cartItem.sharedMedicineId)
                holder.quantity.text = cartItem.quantity.toString()
                holder.price.text = cartItem.priceTotal.toString()
                calculateTotalPrice()
                updateCartItem(cartItem)
                notifyItemChanged(position)
                resultBack.backPrice(totalLiveData.value!!)
            } else if (cartItem.quantity == 1 && it.isPressed) {
                SharedMedicineRepository.increaseSharedMedicineQuantity(cartItem.sharedMedicineId)
                resultBack.deleteItemFromCart(cartItem)
            }
        }

    }

    private fun updateCartItem(cartItem: Cart) {
        dao.updateByRoom(cartItem)

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    interface ResultBack {
        fun backPrice(totalPrice: Double)
        fun checkItIsAvailable(available: Boolean)
        fun deleteItemFromCart(cartItem: Cart)
    }
}