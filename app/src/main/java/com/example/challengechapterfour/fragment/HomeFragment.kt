package com.example.challengechapterfour.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.challengechapterfour.MainActivity
import com.example.challengechapterfour.databinding.EditDialogCustomBinding
import com.example.challengechapterfour.databinding.FragmentHomeBinding
import com.example.challengechapterfour.room.User
import com.example.challengechapterfour.room.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private var mDB : UserDatabase? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: UserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDB = UserDatabase.getInstance(requireContext())

        val sharedPreferences = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        fetchData()

        binding.tvWelcome.text = "Welcome ${sharedPreferences?.getString("email", null)}"

        binding.fabAdd.setOnClickListener {
            val dialog = AddFragment{
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = mDB?.userDao()?.insertUser(it)
                    activity?.runOnUiThread {
                        if (result == (0).toLong()) {
                            Toast.makeText(context,
                                "gagal insert", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context,
                                "sukses insert", Toast.LENGTH_SHORT).show()
                            fetchData()
                        }

                    }
                }
                fetchData()
            }

            dialog.show(parentFragmentManager, "dialog")
        }
        binding.tvLogout.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val direct = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(direct)
        }
    }

    fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO) {
            val myDB = mDB?.userDao()
            val listUser = myDB?.getAllUser()

            activity?.runOnUiThread {
                listUser?.let {
                    adapter = UserAdapter(it, delete = {
                            User -> AlertDialog.Builder(requireContext())
                        .setPositiveButton("Iya"){_,_ ->
                            val mDb = UserDatabase.getInstance(requireContext())
                            lifecycleScope.launch(Dispatchers.IO){
                                val result = mDb?.userDao()?.deleteUser(User)
                                activity?.runOnUiThread {
                                    if (result != 0){
                                        Toast.makeText(
                                            requireContext(),
                                            "Data ${User.tittle} berhasil dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Data ${User.tittle} gagal dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                fetchData()
                            }
                        }
                        .setNegativeButton("Tidak"){dialog, _ ->
                            dialog.dismiss()
                        }
                        .setMessage("Aoakah anda yakin ingin menghapus ${User.tittle}")
                        .setTitle("Konfirmasi Hapus")
                        .create()
                        .show()
                    }, edit = { user ->
                        val dialogBinding = EditDialogCustomBinding.inflate(LayoutInflater.from(requireContext()))
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setView(dialogBinding.root)
                        val dialog = dialogBuilder.create()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialogBinding.tvId.text = "${user.id}"
                        dialogBinding.etTittleEdit.setText("${user.tittle}")
                        dialogBinding.etNoteEdit.setText("${user.note}")
                        dialogBinding.saveButtonEdit.setOnClickListener {
                            val mDB = UserDatabase.getInstance(requireContext())
                            val id: Int = dialogBinding.tvId.text.toString().toInt()
                            val tittle: String = dialogBinding.etTittleEdit.text.toString()
                            val note: String = dialogBinding.etNoteEdit.text.toString()
                            val user = User(
                                id, tittle, note
                            )
                            lifecycleScope.launch(Dispatchers.IO){
                                val result = mDB?.userDao()?.updateUser(user)
                                runBlocking(Dispatchers.Main){
                                    if (result != 0){
                                        Toast.makeText(
                                            requireContext(),
                                            "${user.tittle} Berhasil di update!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        fetchData()
                                        dialog.dismiss()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "${user.tittle} Gagal di update!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog.dismiss()
                                    }
                                }
                            }
                        }
                        dialog.show()
                    })
                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}