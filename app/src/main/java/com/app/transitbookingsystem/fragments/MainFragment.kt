package com.app.transitbookingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.transitbookingsystem.adapters.ApplierAdapter
import com.sliet.transitbookingsystem.R

class MainFragment : Fragment() {

    lateinit var newApplicationBtn: Button
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_main, container, false)

        newApplicationBtn = view.findViewById(R.id.newApplicationBtn)
        recyclerView = view.findViewById(R.id.recyclerView2)


        newApplicationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_applyFragment)
        }

        return view
    }
}