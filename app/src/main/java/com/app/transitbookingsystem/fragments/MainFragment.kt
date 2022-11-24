package com.app.transitbookingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.transitbookingsystem.adapters.ApplierAdapter
import com.app.transitbookingsystem.models.Application
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R

class MainFragment : Fragment() {

    lateinit var newApplicationBtn: Button
    lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference

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

        database = Firebase.database.reference

        println(1)

        var applications: HashMap<String, HashMap<String, Any>>

        database.child("applications").get().addOnSuccessListener {
            applications = it.value as HashMap<String, HashMap<String, Any>>
            var list = arrayListOf<Application>()
            val x = applications.keys
            for(i in x){
                val temp = applications[i]
                val singleApplication = Application(
                    temp!!.get("id").toString(),
                    temp.get("email").toString(),
                    temp.get("visitorName").toString(),
                    temp.get("purpose").toString(),
                    temp.get("dateOfArrival").toString(),
                    temp.get("dateOfDeparture").toString(),
                    temp.get("TotalNumberOfDays").toString(),
                    temp.get("studentName").toString(),
                    temp.get("regNo").toString(),
                    temp.get("hostel").toString(),
                    temp.get("roomNo").toString(),
                    temp.get("mobNo").toString(),
                    temp.get("paymentMode").toString(),
                    temp.get("approvedByHostel") as Boolean,
                    temp.get("approvedByGuestHouse") as Boolean,
                )
                list.add(singleApplication)
            }
            recyclerView.adapter = ApplierAdapter(list)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }.addOnFailureListener {
            it.printStackTrace()
        }
        newApplicationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_applyFragment)
        }

        return view
    }
}