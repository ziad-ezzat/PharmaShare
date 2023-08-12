package com.example.pharmashare.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Dao
import androidx.room.Room
import com.example.pharmashare.R
import com.example.pharmashare.firebase.objects.Cart
import com.example.pharmashare.firebase.objects.SharedMedicine
import com.example.pharmashare.firebase.repos.SharedMedicineRepository
import com.example.pharmashare.firebase.repos.UserRepository
import com.example.pharmashare.firebase.room.MyRoomDatabase
import com.example.pharmashare.firebase.room.repo.CartRepo
import com.example.pharmashare.screens.adptars.HomeAdapter

class HomeFragment : Fragment(), HomeAdapter.HomeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var datebase:MyRoomDatabase
    private lateinit var dao: CartRepo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        recyclerView = rootView.findViewById(R.id.fragment_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //declare of DB
        datebase = Room.databaseBuilder(container!!.context,MyRoomDatabase::class.java,"myDataBase").allowMainThreadQueries().build()
        dao = datebase.cartDao()

        // Replace this with actual data fetching logic
        val userId = UserRepository.getCurrentUserId()
        SharedMedicineRepository.getAllSharedMedicines(userId) { sharedMedicines ->
            homeAdapter = HomeAdapter(sharedMedicines, this)
            recyclerView.adapter = homeAdapter
        }

        return rootView
    }
    override fun addToCart(sharedMedicine: SharedMedicine, selectedQuantity: Int, price: Double) {

        val cartItem = Cart( pharmacyId = "NbFvwsXIUquhBsJLvRE",medicine = sharedMedicine.medicineName, quantity =selectedQuantity, price = price ,priceTotal = selectedQuantity*price, availableQuantity = sharedMedicine.quantity, discountPercentage = 10.0)
//        CartRepository.insertCart(cartItem) { isSuccess ->
//            if (isSuccess) {
//                Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
//            }
//            else
//            {
//                Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
//            }
//        }
        dao.insertByRoom(cartItem)
    }
}