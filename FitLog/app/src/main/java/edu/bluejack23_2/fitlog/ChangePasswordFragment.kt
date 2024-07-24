package edu.bluejack23_2.fitlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentChangePasswordBinding
import edu.bluejack23_2.fitlog.handler.AuthenticationHandler


class ChangePasswordFragment : DialogFragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private var authHandler: AuthenticationHandler = AuthenticationHandler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChangePassword()

    }

    private fun setChangePassword() {
        binding.changePasswordButton.setOnClickListener{
            val oldPassword = binding.oldPassword.text.toString()
            val newPassword = binding.newPassword.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            authHandler.changePassword(oldPassword, newPassword, confirmPassword) { response ->
                Toast.makeText(requireContext(), response.msg, Toast.LENGTH_SHORT).show()

                if(response.status){
                    binding.oldPassword.text.clear()
                    binding.newPassword.text.clear()
                    binding.confirmPassword.text.clear()
                }
            }
        }
    }
}