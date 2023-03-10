package com.shopping.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shopping.data.Order
import com.shopping.data.Product

@Database(entities = [Product::class, Order::class], version = 3, exportSchema = false)
@TypeConverters(ProductTypeConverter::class, DateTypeConverter::class, OrderTypeConverter::class)
abstract class ShoppingDataBase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao

}