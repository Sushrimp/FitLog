package edu.bluejack23_2.fitlog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentAddPersonalRecordBinding
import edu.bluejack23_2.fitlog.handler.PersonalRecordHandler
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.MoveSet
import edu.bluejack23_2.fitlog.models.Response

class AddPersonalRecordFragment : DialogFragment() {

    private var _binding: FragmentAddPersonalRecordBinding? = null
    private var handler: PersonalRecordHandler = PersonalRecordHandler()
    private val binding get() = _binding!!
    private var selectedMoveSet: MoveSet = MoveSet("0", "Select Move Set")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPersonalRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = this.context
        handler.setAllBodyParts(context, binding.bodyPartsSpinner)

        binding.bodyPartsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedBodyPart = parent.getItemAtPosition(position) as BodyPart
                handler.setMoveSets(context, selectedBodyPart.bodyPartID, binding.moveSetsSpinner)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.moveSetsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMoveSet = parent.getItemAtPosition(position) as MoveSet
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.addButton.setOnClickListener {
            val weight = binding.weightField.text.toString()
            val reps = binding.repsField.text.toString()
            val sets = binding.setsField.text.toString()
            val forumCheck = binding.forumCheck.isChecked
            handler.addPersonalRecord(selectedMoveSet, weight, reps, sets, forumCheck) {Response ->
                if(!Response.status) {
                    Toast.makeText(context, Response.msg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, Response.msg, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}