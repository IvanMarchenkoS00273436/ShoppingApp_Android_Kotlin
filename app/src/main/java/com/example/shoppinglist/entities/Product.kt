package com.example.shoppinglist.models

import java.util.Date

class Product (val Id: Long = 0,
               var ProductName: String,
               var DateAdded: Date,
               var Quantity: Int,
               var Category: Category,
               var Unit: String,
               var Notes: String? = null)
{

}