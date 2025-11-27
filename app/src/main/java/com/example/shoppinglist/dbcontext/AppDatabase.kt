package com.example.shoppinglist.dbcontext

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoppinglist.entities.Category
import com.example.shoppinglist.entities.Product
import com.example.shoppinglist.entities.User
import com.example.shoppinglist.daos.CategoryDao
import com.example.shoppinglist.daos.ProductDao
import com.example.shoppinglist.daos.UserDao


@Database(entities = [User::class, Product::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
}