package com.example.shoppinglist.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "ShoppingListDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE products (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                ProductName TEXT,
                DateAdded DATE,
                Quantity INTEGER,
                Category INTEGER ,
                Unit TEXT,
                Notes TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }
}