package edu.bluejack23_2.fitlog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentAddPersonalRecordBinding
import edu.bluejack23_2.fitlog.handler.PersonalRecordHandler

class AddPersonalRecordFragment : DialogFragment() {

    private var _binding: FragmentAddPersonalRecordBinding? = null
    private var handler: PersonalRecordHandler = PersonalRecordHandler()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPersonalRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.setAllBodyParts(this.context, binding.bodyPartsSpinner)
    }
}