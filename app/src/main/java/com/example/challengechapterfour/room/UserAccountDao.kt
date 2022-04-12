package com.example.challengechapterfour.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserAccountDao {
    @Query("SELECT * FROM UserAccount WHERE email = :email AND password = :password")
    fun login(email: String, password: String):UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAccount(user: UserAccount):Long
}