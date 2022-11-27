package com.app.transitbookingsystem.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.transitbookingsystem.models.Application
import com.google.android.material.card.MaterialCardView
import com.sliet.transitbookingsystem.R

class AdminAdapter(private val list: List<Application>,private val context: Context): RecyclerView.Adapter<AdminAdapter.AdminAdapterViewHolder>() {

    open inner class AdminAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtSNo: TextView = itemView.findViewById(R.id.txtsNo)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val singleConfirmBtn: MaterialCardView = itemView.findViewById(R.id.singleApplicationBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recycler_item, parent, false)
        return AdminAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminAdapterViewHolder, position: Int) {
        val item = list[position]
        holder.txtSNo.text = (position+1).toString()
        holder.txtDate.text = item.dateOfArrival
        holder.singleConfirmBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            Navigation.findNavController(it).navigate(R.id.action_activeApplicationsList_to_confirmSingleApplication, bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}