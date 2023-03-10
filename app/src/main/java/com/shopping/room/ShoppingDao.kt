package com.shopping.room

import androidx.room.*
import com.shopping.data.Order
import com.shopping.data.Product

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM product")
    fun getProductList(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: Product)

    @Delete
    fun removeProduct(product: Product)

    @Query("DELETE FROM product")
    fun removeProductList()

    @Update
    fun editProduct(product: Product)

    @Transaction
    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        getProductList().forEach {
            totalPrice += it.price * it.currentAmount
        }
        return totalPrice
    }

    @Query("SELECT * FROM orders")
    fun getOrderList(): List<Order>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrder(order: Order)

    @Update
    fun editOrder(order: Order)

}
