package com.shopping.ui

import androidx.lifecycle.ViewModel
import com.shopping.room.ShoppingDataBase

class MainViewModel : ViewModel() {

    private lateinit var db: ShoppingDataBase

    fun setDataBase(database: ShoppingDataBase) {
        db = database
    }

    fun getDao() = db.shoppingDao()
}