package com.app.transitbookingsystem.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.transitbookingsystem.models.Application
import com.sliet.transitbookingsystem.R

class ApplierAdapter(private var applicationList: List<Application>): RecyclerView.Adapter<ApplierAdapter.ApplierAdapterViewHolder>() {

    inner class ApplierAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val singleApplicationBtn: Button = view.findViewById(R.id.singleApplicationBtn)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplierAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recycler_item, parent, false)
        return ApplierAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplierAdapterViewHolder, position: Int) {
        holder.singleApplicationBtn.setOnClickListener {
            val navController = Navigation.findNavController(it)
            val bundle = Bundle()
            bundle.putString("id", applicationList[position].id)
            navController!!.navigate(R.id.action_mainFragment_to_viewSingleApplication, bundle)
        }
        holder.txtDate.text = applicationList[position].dateOfArrival
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }
}