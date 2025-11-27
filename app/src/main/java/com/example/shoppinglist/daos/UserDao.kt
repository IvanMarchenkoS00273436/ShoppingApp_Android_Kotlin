package com.example.shoppinglist.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.entities.User

@Dao
interface UserDao {
    // --- Create ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    // --- Read ---
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE Id = :userId")
    fun getUserById(userId: Long): User?

    // --- Update ---
    @Update
    fun update(user: User)

    // --- Delete ---
    @Delete
    suspend fun delete(user: User)
}