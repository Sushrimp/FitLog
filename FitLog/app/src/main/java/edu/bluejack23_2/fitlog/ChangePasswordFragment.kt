package edu.bluejack23_2.fitlog

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentChangePasswordBinding
import edu.bluejack23_2.fitlog.handler.AuthenticationHandler
import java.util.concurrent.Executor


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
        binding.changePasswordButton.setOnClickListener {
            val oldPassword = binding.oldPassword.text.toString()
            val newPassword = binding.newPassword.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (isBiometricSupported()) {
                val executor: Executor = ContextCompat.getMainExecutor(requireContext())
                val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(requireContext(), "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // Proceed with password change
                        changePassword(oldPassword, newPassword, confirmPassword)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Authenticate to change password")
                    .setNegativeButtonText("Cancel")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            } else {
                changePassword(oldPassword, newPassword, confirmPassword)
            }
        }
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        authHandler.changePassword(oldPassword, newPassword, confirmPassword) { response ->
            Toast.makeText(requireContext(), response.msg, Toast.LENGTH_SHORT).show()
            if (response.status) {
                binding.oldPassword.text.clear()
                binding.newPassword.text.clear()
                binding.confirmPassword.text.clear()
            }
        }
    }
}