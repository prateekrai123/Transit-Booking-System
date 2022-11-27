package com.app.transitbookingsystem.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R
import java.sql.DatabaseMetaData

class ConfirmSingleApplication : Fragment() {

    lateinit var StudName1: TextView
    lateinit var txtStudRegNo1: TextView
    lateinit var VisName1: TextView
    lateinit var VisAdd1: TextView
    lateinit var Purpose1: TextView
    lateinit var DateOfArrival1: TextView
    lateinit var DateofDeparture1: TextView
    lateinit var TotalDays1: TextView
    lateinit var Hostel1: TextView
    lateinit var RoomNo1: TextView
    lateinit var MobNo1: TextView
    lateinit var PaymentOption1: TextView
    lateinit var database : DatabaseReference

    lateinit var btnAccept: Button
    lateinit var btnReject: Button
    private var role: String? = null
    private  val sharedFileName = "users"
    lateinit var sp: SharedPreferences

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
        val view = inflater.inflate(R.layout.fragment_confirm_single_application, container, false)

        sp = context?.getSharedPreferences(sharedFileName, AppCompatActivity.MODE_PRIVATE)!!

        role = sp.getString("role", "0")

        database = Firebase.database.reference

        var application :HashMap<String, Any>? = null

        PaymentOption1= view.findViewById(R.id.etPaymentOption1)
        MobNo1= view.findViewById(R.id.etMobNo1)
        RoomNo1= view.findViewById(R.id.etRoomNo1)
        Hostel1= view.findViewById(R.id.etHostel1)
        TotalDays1= view.findViewById(R.id.etTotalDays1)
        DateofDeparture1= view.findViewById(R.id.etDateOfDeparture1)
        DateOfArrival1= view.findViewById(R.id.etDateOfArrival1)
        Purpose1= view.findViewById(R.id.etPurpose1)
        VisName1= view.findViewById(R.id.etVisName1)
        StudName1= view.findViewById(R.id.etStudName1)
        txtStudRegNo1=view.findViewById(R.id.StudRegNo1)
        btnReject=view.findViewById(R.id.btnReject)
        btnAccept=view.findViewById(R.id.btnAccept)

        database.child("applications").child(id!!).get().addOnSuccessListener {
            if(it.value == null){
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            application = it.value as HashMap<String, Any>?
            PaymentOption1.text= application?.get("paymentMode").toString()
            MobNo1.text=application?.get("mobNo").toString()
            RoomNo1.text=application?.get("roomNo").toString()
            Hostel1.text=application?.get("hostel").toString()
            TotalDays1.text=application?.get("TotalNumberOfDays").toString()
            DateofDeparture1.text=application?.get("dateOfDeparture").toString()
            DateOfArrival1.text=application?.get("dateOfArrival").toString()
            Purpose1.text=application?.get("purpose").toString()
            VisName1.text=application?.get("visitorName").toString()
            txtStudRegNo1.text=application?.get("regNo").toString()
            StudName1.text=application?.get("studentName").toString()
        }.addOnFailureListener {
            it.printStackTrace()
            Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
        }

        btnAccept.setOnClickListener {
            btnAccept.isActivated = false
            if(role == "1"){
                database.child("applications").child(id!!).child("approvedByHostel").setValue(true)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Accepted", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_confirmSingleApplication_to_activeApplicationsList)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                        btnAccept.isActivated = true
                    }
            }
            else if(role == "2"){
                database.child("applications").child(id!!).child("approvedByGuestHouse").setValue(true)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Accepted", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_confirmSingleApplication_to_activeApplicationsList)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                        btnAccept.isActivated = true
                    }
            }
            else{
                Toast.makeText(context, "You are not permitted for the action", Toast.LENGTH_LONG).show()
            }
        }

        btnReject.setOnClickListener {
            btnAccept.isActivated = false
            if(role == "1"){
                database.child("applications").child(id!!).setValue(null)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Accepted", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_confirmSingleApplication_to_activeApplicationsList)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                        btnAccept.isActivated = true
                    }
            }
            else if(role == "2"){
                database.child("applications").child(id!!).setValue(null)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Accepted", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_confirmSingleApplication_to_activeApplicationsList)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                        btnAccept.isActivated = true
                    }
            }
            else{
                Toast.makeText(context, "You are not permitted for the action", Toast.LENGTH_LONG).show()
            }
        }

        return view;
    }
}