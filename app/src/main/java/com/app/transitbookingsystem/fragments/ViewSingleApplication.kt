package com.app.transitbookingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sliet.transitbookingsystem.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewSingleApplication.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewSingleApplication : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var txtStudentName:TextView
    lateinit var txtDays:TextView
    lateinit var txtArrival:TextView
    lateinit var txtDeparture:TextView
    lateinit var txtHostel:TextView
    lateinit var txtRoom:TextView
    lateinit var txtMobile:TextView
    lateinit var txtRegno:TextView
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
        val view= inflater.inflate(R.layout.fragment_view_single_application, container, false)
        txtStudentName = view.findViewById(R.id.etStudentName)
        txtDays = view.findViewById(R.id.etDays)
        txtArrival=view.findViewById(R.id.etArrival)
        txtDeparture=view.findViewById(R.id.etDeparture)
        txtHostel=view.findViewById(R.id.etHostel)
        txtRoom=view.findViewById(R.id.etRoom)
        txtMobile=view.findViewById(R.id.etMobile)
        txtRegno=view.findViewById(R.id.etStudentReg)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewSingleApplication.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewSingleApplication().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}