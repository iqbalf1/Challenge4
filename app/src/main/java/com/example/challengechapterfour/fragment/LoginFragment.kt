package com.example.challengechapterfour.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengechapterfour.MainActivity
import com.example.challengechapterfour.R
import com.example.challengechapterfour.databinding.FragmentLoginBinding
import com.example.challengechapterfour.room.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private var mDb: UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDb = UserDatabase.getInstance(requireContext())
        val sharedPreferences = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        binding.buttonLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO){
                val Login = mDb?.userAccountDao()?.login(email,password)
                activity?.runOnUiThread {
                    if (Login ==null){
                        Toast.makeText(context, "Username atau Password Anda Salah", Toast.LENGTH_SHORT).show()
                    }else{
                        val editor = sharedPreferences.edit()
                        editor.putString("email",email)
                        editor.apply()
                        val direct = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(direct)
                    }
                }
            }

        }

        binding.buttonSignup.setOnClickListener {
            val direct = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(direct)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}