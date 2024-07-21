package edu.bluejack23_2.fitlog.models

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MoveSetSpinnerAdapter(context: Context, moveSets: List<MoveSet>) :
    ArrayAdapter<MoveSet>(context, android.R.layout.simple_spinner_item, moveSets) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        val moveSet = getItem(position)
        view.text = moveSet?.moveSet
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        val moveSet = getItem(position)
        view.text = moveSet?.moveSet
        return view
    }

    override fun getItem(position: Int): MoveSet? {
        return super.getItem(position)
    }
}
