package com.example.challengechapterfour.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.challengechapterfour.R
import com.example.challengechapterfour.databinding.FragmentAddBinding
import com.example.challengechapterfour.databinding.UserItemBinding
import com.example.challengechapterfour.room.User
import com.example.challengechapterfour.room.UserDatabase
import kotlinx.coroutines.Job

class AddFragment(private val listUser: (User)->Unit): DialogFragment() {

    private var _binding: FragmentAddBinding? = null
    private var mDb: UserDatabase? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mDb = UserDatabase.getInstance(requireContext())
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            binding.apply {
                saveButtonAdd.setOnClickListener {
                    val tittle: String = binding.etTittleAdd.text.toString()
                    val note: String = binding.etNoteAdd.text.toString()
                    val user = User(
                        null, tittle, note
                    )
                    listUser(user)
                    dialog?.dismiss()
                }
            }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}