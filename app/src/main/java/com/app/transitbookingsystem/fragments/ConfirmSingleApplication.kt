package com.app.transitbookingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R
import java.sql.DatabaseMetaData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmSingleApplication.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmSingleApplication : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_confirm_single_application, container, false)


        database = Firebase.database.reference

        val id = activity?.intent?.getStringExtra("id")!!
        val application = database.child("application").child(id) as HashMap<String, HashMap<String, String>>

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

        PaymentOption1.text= application?.get("paymentMode").toString()
        MobNo1.text=application["mobNo"].toString()
        RoomNo1.text=application["roomNo"].toString()
        Hostel1.text=application["hostel"].toString()
        TotalDays1.text=application["TotalNumberOfDays"].toString()
        DateofDeparture1.text=application["dateOfDeparture"].toString()
        DateOfArrival1.text=application["dateOfArrival"].toString()
        Purpose1.text=application["purpose"].toString()
        VisName1.text=application["visitorName"].toString()
        txtStudRegNo1.text=application["regNo"].toString()
        StudName1.text=application["studentName"].toString()

        return view;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConfirmSingleApplication.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConfirmSingleApplication().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}