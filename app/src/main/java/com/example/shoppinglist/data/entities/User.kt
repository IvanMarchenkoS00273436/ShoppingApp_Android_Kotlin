package com.example.shoppinglist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// User entity representing a user in the shopping list application
@Entity(tableName = "users")
class User (
            @PrimaryKey(autoGenerate = true)
            val Id: Long = 0,
            var FirstName: String,
            var LastName: String,
            var Email: String)
{
}