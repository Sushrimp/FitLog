package edu.bluejack23_2.fitlog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentEditTimerBinding
import edu.bluejack23_2.fitlog.databinding.FragmentTimerBinding

class EditTimerFragment : DialogFragment() {
    private var _binding: FragmentEditTimerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = this.context
        var myTimer = 0
        if(context != null) {
            val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            myTimer = sharedPreferences.getInt("myTimer", 0)
            val editor = sharedPreferences.edit()
            if (myTimer == 0) {
                editor.putInt("myTimer", 152000)
                editor.apply()
                myTimer = sharedPreferences.getInt("myTimer", 0)
            }
            val minutes = ((myTimer / 1000) % 3600) / 60
            val seconds = (myTimer / 1000) % 60
            binding.minsField.setText(minutes.toString())
            binding.secsField.setText(seconds.toString())

            binding.updateBtn.setOnClickListener {
                var min = binding.minsField.text.toString().toInt() * 60 * 1000
                var sec = binding.secsField.text.toString().toInt() * 1000
                var millis = min + sec
                editor.putInt("myTimer", millis)
                editor.apply()
                Toast.makeText(context, "Timer successfully updated!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}