package com.example.challengechapterfour.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUser(): List<User>

    @Insert(onConflict = REPLACE)
    fun insertUser(student: User):Long

    @Update
    fun updateUser(student: User):Int

    @Delete
    fun deleteUser(student: User):Int
}