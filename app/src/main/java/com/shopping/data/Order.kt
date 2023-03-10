package com.shopping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    var orderId: Int? = null,
    var maskedCardNo: String,
    var amount: Double,
    var createdDate: Date,
    var isCancelled: Boolean = false
)