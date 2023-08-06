package com.example.pharmashare.screens

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmashare.R

private const val ARG_PARAM1 = "Adapter"
class home_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.fragment_rv)
        rv.setHasFixedSize(true)
        rv.adapter = (param1 as RecyclerView.Adapter<*>)
        rv.layoutManager= LinearLayoutManager(view.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragment, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Any) =
            home_fragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1 as Parcelable)
                }
            }
    }
}