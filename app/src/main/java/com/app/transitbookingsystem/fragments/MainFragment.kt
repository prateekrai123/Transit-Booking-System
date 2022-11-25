package com.app.transitbookingsystem.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.transitbookingsystem.activities.LoginActivity
import com.app.transitbookingsystem.adapters.ApplierAdapter
import com.app.transitbookingsystem.models.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
    lateinit var btnLogOut: Button
    private lateinit var database: DatabaseReference
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var sp: SharedPreferences
    private  val sharedFileName = "users"

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
        btnLogOut = view.findViewById(R.id.btnLogOut)

        database = Firebase.database.reference

        sp = activity?.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)!!

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        btnLogOut.setOnClickListener {
            mGoogleSignInClient.signOut().addOnSuccessListener {
                sp.edit().clear().commit()
                startActivity(Intent(context, LoginActivity::class.java))
                ActivityCompat.finishAffinity(requireActivity())
            }.addOnFailureListener {
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
            }
        }

        val email = sp.getString("email", "")

        println(1)

        var applications: HashMap<String, HashMap<String, Any>>

        database.child("applications").get().addOnSuccessListener {
            if(it.value==null){
                return@addOnSuccessListener
            }
            applications = it.value as HashMap<String, HashMap<String, Any>>
            var list = arrayListOf<Application>()
            val x = applications.keys
            for(i in x){
                val temp = applications[i]
                if(temp!!.get("email")!=email){
                    continue
                }
                val singleApplication = Application(
                    temp.get("id").toString(),
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