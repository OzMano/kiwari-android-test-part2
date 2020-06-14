package com.chataja.codingchallenges

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sample_list_item.view.*


class SampleAdapter(var items : ArrayList<Int>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newDataset: List<Int>) {
        items.clear()
        items.addAll(newDataset)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvData.text = items[position].toString()
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvData: TextView = view.tv_data
}