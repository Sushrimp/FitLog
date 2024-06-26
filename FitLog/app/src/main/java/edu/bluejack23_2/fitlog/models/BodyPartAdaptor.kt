package edu.bluejack23_2.fitlog.models

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class BodyPartSpinnerAdapter(context: Context, bodyParts: List<BodyPart>) :
    ArrayAdapter<BodyPart>(context, android.R.layout.simple_spinner_item, bodyParts) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        val bodyPart = getItem(position)
        view.text = bodyPart?.bodyPart
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        val bodyPart = getItem(position)
        view.text = bodyPart?.bodyPart
        return view
    }

    override fun getItem(position: Int): BodyPart? {
        return super.getItem(position)
    }
}