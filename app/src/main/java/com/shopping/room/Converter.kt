package com.shopping.room

import com.shopping.data.Order
import com.shopping.data.Product
import java.util.Date

class ProductTypeConverter : BaseTypeConverter<Product>()
class DateTypeConverter : BaseTypeConverter<Date>()
class OrderTypeConverter : BaseTypeConverter<Order>()