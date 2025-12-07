package com.example.shoppinglist.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.data.entities.User

// Data Access Object for User entity
@Dao
interface UserDao {
    // --- Create ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: User)

    // --- Read ---
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE Id = :userId")
    suspend fun getUserById(userId: Long): User?

    // --- Update ---
    @Update
    suspend fun update(user: User)

    // --- Delete ---
    @Delete
    suspend fun delete(user: User)
}