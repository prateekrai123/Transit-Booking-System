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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.transitbookingsystem.activities.LoginActivity
import com.app.transitbookingsystem.adapters.AdminAdapter
import com.app.transitbookingsystem.models.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R

class ActiveApplicationsList : Fragment() {

    private var id: Int? = null

    lateinit var btnLogOut: Button
    private lateinit var database: DatabaseReference
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var recyclerView : RecyclerView

    lateinit var sp: SharedPreferences
    private val sharedFileName = "users"
    private var hostel: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id")?.toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_active_applications_list, container, false)

        btnLogOut = view.findViewById(R.id.btnLogOut)
        recyclerView = view.findViewById(R.id.recyclerView)

        database = Firebase.database.reference

        var applications: HashMap<String, HashMap<String, Any>>? = null

        sp = activity?.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)!!

        hostel = sp.getString("hostel", "hostel")
        role = sp.getString("role", "1")

        database.child("applications").get().addOnSuccessListener {
            if(it.value == null){
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            applications = it.value as HashMap<String, HashMap<String, Any>>?
            val applicationList = applicationList(applications)
            recyclerView.adapter = AdminAdapter(applicationList!!, requireContext())
            recyclerView.layoutManager = LinearLayoutManager(context)
        }.addOnFailureListener {
            it.printStackTrace()
            Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show()
        }

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

        return view
    }

    private fun applicationList(applications: HashMap<String, HashMap<String, Any>>?): List<Application>? {
        println(this.hostel)
        val applicationsList = arrayListOf<Application>()
        val item = applications?.keys
        for(i in item!!){
            val temp: java.util.HashMap<String, Any>? = applications[i]
            val hostel = temp?.get("hostel")
            val approvedByHostel: Boolean = temp?.get("approvedByHostel") as Boolean
            val approvedByTransit: Boolean = temp?.get("approvedByGuestHouse") as Boolean
            if(role == "2"){
                if(!approvedByHostel){
                    continue
                }
            }
            if(role == "2"){
                if(approvedByTransit){
                    continue
                }
            }
            if(role=="1"){
                if(approvedByHostel){
                    continue
                }
            }
            if(role=="0"){
                break
            }
            if(role != "2" && hostel != this.hostel){
                continue
            }
            else{
                val singleItem = Application(
                    temp?.get("id").toString(),
                    temp?.get("email").toString(),
                    temp?.get("visitorName").toString(),
                    temp.get("visAdd").toString(),
                    temp?.get("purpose").toString(),
                    temp?.get("dateOfArrival").toString(),
                    temp?.get("dateOfDeparture").toString(),
                    temp?.get("TotalNumberOfDays").toString(),
                    temp?.get("studentName").toString(),
                    temp?.get("regNo").toString(),
                    temp?.get("hostel").toString(),
                    temp?.get("roomNo").toString(),
                    temp?.get("mobNo").toString(),
                    temp?.get("paymentMode").toString(),
                    temp?.get("approvedByHostel") as Boolean,
                    temp?.get("approvedByGuestHouse") as Boolean,
                )
                applicationsList.add(singleItem)
            }
        }
        return applicationsList
    }
}