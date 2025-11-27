package com.example.shoppinglist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class User (
            @PrimaryKey(autoGenerate = true)
            val Id: Long = 0,
            var FirstName: String,
            var LastName: String,
            var Email: String)
{
}