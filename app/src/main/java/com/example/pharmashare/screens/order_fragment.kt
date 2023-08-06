package com.example.pharmashare.screens

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R
import com.example.pharmashare.screens.adptars.OrderAdaptar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "Adapter"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [order_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class order_fragment : Fragment(),OrderAdaptar.connectFragment {
    // TODO: Rename and change types of parameters
//
     private var adapter: Any? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adapter = it.getParcelable(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
         }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_fragment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment order_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Any) =
            order_fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1 as Parcelable)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
    var totalPrice:TextView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.order_recyclerView)
        totalPrice = view.findViewById(R.id.order_total_price)
        rv.layoutManager = LinearLayoutManager(view.context)
        rv.setHasFixedSize(true)
        rv.adapter = adapter as OrderAdaptar
        (adapter as OrderAdaptar).connectFragment1 =this

    }
    override fun addTotal(total: Double) {
        println("hy $total")
        totalPrice?.text ="total cost $total"
    }
}