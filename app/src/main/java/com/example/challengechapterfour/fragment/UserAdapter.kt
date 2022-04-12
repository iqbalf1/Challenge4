package com.example.challengechapterfour.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengechapterfour.room.User
import com.example.challengechapterfour.databinding.UserItemBinding

class UserAdapter (
    private val listUser: List<User>,
    private val delete: (User)->Unit,
    private val edit: (User)->Unit) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){
//    Class Holder
    class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvTittle.text = listUser[position].tittle
            tvNote.text = listUser[position].note
            ivDelete.setOnClickListener {
                delete.invoke(listUser[position])
            }
            ivEdit.setOnClickListener {
                edit.invoke(listUser[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}