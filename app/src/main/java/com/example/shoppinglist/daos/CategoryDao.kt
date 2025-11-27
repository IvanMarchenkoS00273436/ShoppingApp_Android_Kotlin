package com.example.shoppinglist.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    // --- Create ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category): Long

    // --- Read ---
    @Query("SELECT * FROM categories ORDER BY category_name ASC")
    suspend fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE category_id = :id")
    suspend fun getCategoryById(id: Long): Category?

    // --- Update ---
    @Update
    suspend fun update(category: Category)

    // --- Delete ---
    @Delete
    suspend fun delete(category: Category)
}