package com.example.shoppinglist.data.dbcontext

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppinglist.data.daos.CategoryDao
import com.example.shoppinglist.data.daos.ProductDao
import com.example.shoppinglist.data.daos.UserDao
import com.example.shoppinglist.data.entities.Category
import com.example.shoppinglist.data.entities.Product
import com.example.shoppinglist.data.entities.User
import com.example.shoppinglist.data.convertors.Converters

// AppDatabase class integrating DAOs and Converters
@Database(entities = [User::class, Product::class, Category::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shopping-list-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}