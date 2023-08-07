package com.example.pharmashare.screens.adptars

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Medicine
import com.example.pharmashare.firebase.objects.SharedMedicine

class HomeAdaptar(var homeListner: HomeLinsener?) : RecyclerView.Adapter<HomeAdaptar.Holder>(),Parcelable {
    var arr: ArrayList<SharedMedicine> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var list: ArrayList<Medicine> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.med_name)
        val price: TextView = itemView.findViewById(R.id.med_price)
        val contaty: TextView = itemView.findViewById(R.id.med_contaty)
        val addToCart: Button = itemView.findViewById(R.id.med_makeOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_card, parent, false)
        )
    }

    override fun getItemCount(): Int = arr.size


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.nameTv.text = getName(arr[position].medicineId)
        holder.contaty.text = arr[position].quantity.toString()
        holder.price.text = arr[position].price.toString()
        holder.addToCart.setOnClickListener {
            homeListner?.addToCart(arr[holder.adapterPosition])?: println("null object")
            holder.addToCart.isEnabled = false
        }
    }

    private fun getName(id: String): String {
        var s = ""
        list.forEach {
            if (it.id == id) {
                s = it.name
            }
        }
        return s
    }

    interface HomeLinsener {
        fun addToCart(arr:SharedMedicine)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeAdaptar> {
        override fun createFromParcel(parcel: Parcel): HomeAdaptar {
            return HomeAdaptar(homeListner = null)
        }

        override fun newArray(size: Int): Array<HomeAdaptar?> {
            return arrayOfNulls(size)
        }
    }
}