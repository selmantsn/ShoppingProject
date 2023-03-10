package com.shopping.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var name: String,

    @ColumnInfo(name = "price")
    var price: Double,
    var currentAmount: Int = 0
)