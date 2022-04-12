package com.example.challengechapterfour.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengechapterfour.databinding.FragmentRegisterBinding
import com.example.challengechapterfour.room.UserAccount
import com.example.challengechapterfour.room.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var mDb: UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDb = UserDatabase.getInstance(requireContext())

        binding.buttonSignupSignup.setOnClickListener {
            val name = binding.etUsernameSignup.text.toString()
            val email = binding.etEmailSignup.text.toString()
            val password = binding.etPasswordSignup.text.toString()
            val confirmPassword = binding.etConfirmPasswordSignup.text.toString()

            val regist = UserAccount(null,name,email,password)
            when {
                name.isNullOrEmpty() -> {
                    binding.etUsernameSignup.error = "Kolom nama harus diisi"
                }
                email.isNullOrEmpty() -> {
                    binding.etEmailSignup.error = "Kolom email harus diisi"
                }
                password.isNullOrEmpty() -> {
                    binding.etPasswordSignup.error = "Kolom password harus diisi"
                }
                confirmPassword.isNullOrEmpty() -> {
                    binding.etConfirmPasswordSignup.error = "Kolom konfirmasi password harus diisi"
                }
                password.lowercase() != confirmPassword.lowercase() -> {
                    binding.etConfirmPasswordSignup.error = "Password dan konfirmasi password tidak sama"
                    binding.etConfirmPasswordSignup.setText("")
                }else-> {
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = mDb?.userAccountDao()?.insertUserAccount(regist)
                    activity?.runOnUiThread {
                        if (result != 0.toLong()){
                            Toast.makeText(context, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Pendaftaran gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val direct = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(direct)
            }
            }
        }

    }

}