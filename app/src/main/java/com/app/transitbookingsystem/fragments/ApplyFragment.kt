package com.app.transitbookingsystem.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ApplyFragment : Fragment() {

    lateinit var applyBtn: Button
    lateinit var etVisName: EditText
    lateinit var etAdd : EditText
    lateinit var etPurpose: EditText
    lateinit var etDateOfArr : EditText
    lateinit var etDateOfDep: EditText
    lateinit var etTotDays: EditText
    lateinit var etStudName: EditText
    lateinit var etStudRegNo: EditText
    private lateinit var hostelNoSpinner: Spinner
    lateinit var etRoomNo: EditText
    lateinit var etMobNo: EditText
    lateinit var etPaymentMode: EditText
    private lateinit var database: DatabaseReference


    private  val sharedFileName = "users"

    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_apply, container, false)
        applyBtn = view.findViewById(R.id.applyBtn)
        etVisName = view.findViewById(R.id.etVisName)
        etAdd = view.findViewById(R.id.etVisAdd)
        etPurpose = view.findViewById(R.id.etPurpose)
        etDateOfArr = view.findViewById(R.id.etDateOfArrival)
        etDateOfDep = view.findViewById(R.id.etDateOfDeparture)
        etTotDays = view.findViewById(R.id.etTotalDays)
        etStudName = view.findViewById(R.id.etStudName)
        etStudRegNo = view.findViewById(R.id.StudRegNo)
        hostelNoSpinner = view.findViewById(R.id.spinner)
        etRoomNo = view.findViewById(R.id.etRoomNo)
        etMobNo = view.findViewById(R.id.etMobNo)
        etPaymentMode = view.findViewById(R.id.etPaymentOption)

        database = Firebase.database.reference

        sp = activity?.getSharedPreferences(sharedFileName, AppCompatActivity.MODE_PRIVATE)!!

        val email = sp.getString("email", "")
        val name = sp.getString("name", "")

        val arr = arrayOf("BH1", "BH2", "BH3", "BH4", "BH5", "BH6", "BH7", "BH8", "BH9", "BH10", "GH1", "GH2", "GH3")
        val adapter = ArrayAdapter(requireContext(), androidx.transition.R.layout.support_simple_spinner_dropdown_item, arr)
        hostelNoSpinner.adapter = adapter

        var hostel : String = "BH1"

        hostelNoSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                hostel = arr[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        applyBtn.setOnClickListener {
            if(
                etVisName.text.toString().trim() == "" ||
                        etAdd.text.toString().trim() == "" ||
                        etPurpose.text.toString().trim() == "" ||
                        etDateOfArr.text.toString().trim() == "" ||
                        etDateOfDep.text.toString().trim() == "" ||
                        etTotDays.text.toString().trim() == "" ||
                        etStudName.text.toString().trim() == "" ||
                        etStudRegNo.text.toString().trim() == "" ||
                        etRoomNo.text.toString().trim() == "" ||
                        etMobNo.text.toString().trim() == "" ||
                        etPaymentMode.text.toString().trim() == ""
            ){

                Toast.makeText(context, "All fields should be completely filled", Toast.LENGTH_LONG).show()
            }
            else{
                val id = DateTimeFormatter
                    .ofPattern("yyyyMMddHHmmssSSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now())

                val application = com.app.transitbookingsystem.models.Application(
                    id,
                    email.toString().trim(),
                    etVisName.text.toString().trim(),
                    etPurpose.text.toString().trim(),
                    etDateOfArr.text.toString().trim(),
                    etDateOfDep.text.toString().trim(),
                    etTotDays.text.toString().trim(),
                    etStudName.text.toString().trim(),
                    etStudRegNo.text.toString().trim(),
                    hostel,
                    etRoomNo.text.toString().trim(),
                    etMobNo.text.toString().trim(),
                    etPaymentMode.text.toString().trim(),
                    false,
                    false
                )
                database.child("applications").child(id).setValue(application).addOnSuccessListener {
                    Toast.makeText(context, "Applied", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    it.printStackTrace()
                }
            }
            Toast.makeText(context, "Work in progress", Toast.LENGTH_LONG).show()
        }
        return view
    }
}