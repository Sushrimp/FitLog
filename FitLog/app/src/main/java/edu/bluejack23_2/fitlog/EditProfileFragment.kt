package edu.bluejack23_2.fitlog

import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.text.set
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentAddPersonalRecordBinding
import edu.bluejack23_2.fitlog.databinding.FragmentEditProfileBinding
import edu.bluejack23_2.fitlog.handler.PersonalRecordHandler
import edu.bluejack23_2.fitlog.handler.UserHandler


class EditProfileFragment : DialogFragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var userHandler: UserHandler = UserHandler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfile()

        binding.updateButtonEditProfile.setOnClickListener {
            val selectedRadioButtonId = binding.radioGroupEditProfile.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = binding.root.findViewById<RadioButton>(selectedRadioButtonId)
                val genderSelected = selectedRadioButton.text.toString()
                val name = binding.nameEditProfile.text.toString()
                val username = binding.usernameEditProfile.text.toString()
                val age = binding.ageEditProfile.text.toString()

                userHandler.updateUser(name, username, age, genderSelected) { response ->
                    if (!response.status) {
                        Toast.makeText(requireContext(), response.msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "User updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please select a gender", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setProfile() {
        userHandler.getUserDetails { response ->
            binding.nameEditProfile.setText(response.user.name.toString())
            binding.usernameEditProfile.setText(response.user.username.toString())
            binding.ageEditProfile.setText(response.user.age.toString())
            if(response.user.gender.equals("Male")) {
                binding.maleRadioEditProfile.isChecked = true
            } else {
                binding.femaleRadioEditProfile.isChecked = true
            }
        }
    }

}