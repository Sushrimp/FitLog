package edu.bluejack23_2.fitlog.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.fitlog.R

class PRAdapter(private val prs : List<PersonalRecordDetail>) : RecyclerView.Adapter<PRAdapter.PRViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_pr, parent, false)
        return PRViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return prs.size
    }

    override fun onBindViewHolder(holder: PRViewHolder, position: Int) {
        val currentItem = prs[position]
        holder.moveSet.text = currentItem.moveSet.moveSet.toString()
        holder.detail.text = "Weight : " + currentItem.weight.toString() + "kg - Set : " + currentItem.sets.toString()  + " - Rep : " + currentItem.reps.toString()
    }

    class PRViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moveSet: TextView = itemView.findViewById(R.id.prMove)
        val detail: TextView = itemView.findViewById(R.id.prDetail)
    }
}