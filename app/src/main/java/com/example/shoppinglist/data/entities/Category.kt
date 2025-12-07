package com.example.shoppinglist.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Category entity representing a category in the shopping list
@Entity(tableName = "categories")
data class Category (
    @PrimaryKey(autoGenerate = true) val category_id: Long = 0,
    @ColumnInfo(name = "category_name") var category_name: String)