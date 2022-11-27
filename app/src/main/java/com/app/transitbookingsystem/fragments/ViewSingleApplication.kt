package com.app.transitbookingsystem.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R

class ViewSingleApplication : Fragment() {
    lateinit var txtStudentName:TextView
    lateinit var txtDays:TextView
    lateinit var txtArrival:TextView
    lateinit var txtDeparture:TextView
    lateinit var txtHostel:TextView
    lateinit var txtRoom:TextView
    lateinit var txtMobile:TextView
    lateinit var txtRegno:TextView
    lateinit var approvedByHostelView: MaterialCardView
    lateinit var approvedByGuestHouseView: MaterialCardView
    lateinit var btnDelete: Button
    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_view_single_application, container, false)


        database = Firebase.database.reference

        txtStudentName = view.findViewById(R.id.etStudentName)
        txtDays = view.findViewById(R.id.etDays)
        txtArrival=view.findViewById(R.id.etArrival)
        txtDeparture=view.findViewById(R.id.etDeparture)
        txtHostel=view.findViewById(R.id.etHostel)
        txtRoom=view.findViewById(R.id.etRoom)
        txtMobile=view.findViewById(R.id.etMobile)
        txtRegno=view.findViewById(R.id.etStudentReg)

        btnDelete = view.findViewById(R.id.btnDelete)

        val id = activity?.intent?.getStringExtra("id")!!
        var application : HashMap<String, HashMap<String, String>>? = null
        database.child("application").child(id).get().addOnSuccessListener {
            application = it.value as HashMap<String, HashMap<String, String>>?
            approvedByHostelView = view.findViewById(R.id.card_view)
            approvedByGuestHouseView = view.findViewById(R.id.card2_view)

            txtMobile.text=application?.get("mobNo").toString()
            txtRoom.text=application?.get("roomNo").toString()
            txtHostel.text=application?.get("hostel").toString()
            txtDays.text=application?.get("TotalNumberOfDays").toString()
            txtDeparture.text=application?.get("dateOfDeparture").toString()
            txtArrival.text=application?.get("dateOfArrival").toString()
            txtRegno.text=application?.get("regNo").toString()
            txtStudentName.text=application?.get("studentName").toString()

            val approvedByHostel : Boolean = application?.get("approvedByHostel").toString().toBoolean()
            val approvedByGuestHouse: Boolean = application?.get("approvedByGuestHouse").toString().toBoolean()
            val green = Color.GREEN
            val red = Color.RED

            if(approvedByGuestHouse){
                approvedByGuestHouseView.setBackgroundColor(green)
            }
            else{
                approvedByGuestHouseView.setBackgroundColor(red)
            }

            if(approvedByHostel){
                approvedByHostelView.setBackgroundColor(green)
            }
            else{
                approvedByHostelView.setBackgroundColor(red)
            }
        }.addOnFailureListener {
            it.printStackTrace()
            Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
        }

        btnDelete.setOnClickListener {
            btnDelete.isActivated = false
            database.child("applications").child(id).setValue(null).addOnSuccessListener {
                Toast.makeText(context, "Successfully deleted", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_viewSingleApplication_to_mainFragment)
            }.addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                btnDelete.isActivated = true
            }

        }

        return view
    }
}