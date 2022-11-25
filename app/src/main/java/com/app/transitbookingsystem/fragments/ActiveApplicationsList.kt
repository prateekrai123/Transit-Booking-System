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
import com.app.transitbookingsystem.activities.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActiveApplicationsList.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActiveApplicationsList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var btnLogOut: Button
    private lateinit var database: DatabaseReference
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var sp: SharedPreferences
    private  val sharedFileName = "users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_active_applications_list, container, false)

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

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ActiveApplicationsList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActiveApplicationsList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}