package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "products", foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["product_category_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Product (
            @PrimaryKey(autoGenerate = true)
            val Id: Long = 0,
            var ProductName: String,
            var DateAdded: Date,
            var Quantity: Int,

            @ColumnInfo(name = "product_category_id", index = true)
            var categoryId: Long? = null,

            var Unit: String,
            var Notes: String? = null)
{

}