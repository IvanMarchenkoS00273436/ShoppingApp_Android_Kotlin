package com.example.shoppinglist.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    // --- Create ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product): Long

    // --- Read ---
    @Query("SELECT * FROM products ORDER BY DateAdded DESC")
    suspend fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE Id = :productId")
    suspend fun getProductById(productId: Long): Product?

    @Query("SELECT * FROM products WHERE product_category_id = :categoryId")
    suspend fun getProductsByCategory(categoryId: Long): Flow<List<Product>>

    // --- Update ---
    @Update
    suspend fun update(product: Product)

    // --- Delete ---
    @Delete
    suspend fun delete(product: Product)

}