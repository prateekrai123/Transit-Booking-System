package com.app.transitbookingsystem.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
    lateinit var txtHostelVerified : TextView
    lateinit var txtGuestHouseVerified: TextView
    lateinit var database : DatabaseReference

    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id")
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

        var application : HashMap<String, Any>? = null
        database.child("applications").child(id!!).get().addOnSuccessListener {
            println(it.value)
            application = it.value as HashMap<String, Any>?
            approvedByHostelView = view.findViewById(R.id.card_view)
            approvedByGuestHouseView = view.findViewById(R.id.card2_view)
            txtHostelVerified = view.findViewById(R.id.txtHostelVerified)
            txtGuestHouseVerified = view.findViewById(R.id.txtGuestHouseVerified)

            txtMobile.text= "Mobile :- ${application?.get("mobNo").toString()}"
            txtRoom.text="Room No. :- ${application?.get("roomNo").toString()}"
            txtHostel.text="Hostel :- ${application?.get("hostel").toString()}"
            txtDays.text="Total Number of Days :- ${application?.get("totalNumberOfDays").toString()}"
            txtDeparture.text= "Departure Date :- ${ application?.get("dateOfDeparture").toString() }"
            txtArrival.text= "Arrival Date :-  ${ application?.get("dateOfArrival").toString() }"
            txtRegno.text="Reg. No. :- ${application?.get("regNo").toString()}"
            txtStudentName.text="Student Name :- ${application?.get("studentName").toString()}"

            val approvedByHostel : Boolean = application?.get("approvedByHostel") as Boolean
            val approvedByGuestHouse: Boolean = application?.get("approvedByGuestHouse") as Boolean
            val green = Color.GREEN
            val red = Color.RED

            if(approvedByGuestHouse){
                approvedByGuestHouseView.setBackgroundColor(green)
                txtGuestHouseVerified.text = "Verified by guest house"

            }
            else{
                approvedByGuestHouseView.setBackgroundColor(red)
                txtHostelVerified.text = "Guest house verification pending"
            }

            if(approvedByHostel){
                approvedByHostelView.setBackgroundColor(green)
                txtHostelVerified.text = "Verified by hostel"
            }
            else{
                approvedByHostelView.setBackgroundColor(red)
                txtHostelVerified.text = "Hostel Verification Pending"
            }
        }.addOnFailureListener {
            it.printStackTrace()
            Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
        }

        btnDelete.setOnClickListener {
            btnDelete.isActivated = false
            database.child("applications").child(id!!).setValue(null).addOnSuccessListener {
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