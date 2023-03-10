package com.shopping.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.util.*


open class BaseTypeConverter<T> {

    private val gson = Gson()

    @TypeConverter
    fun someObjectListToString(someObjects: List<T>): String? {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToSomeObjectList(string: String?): List<T>? {
        if (string == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(string, listType)
    }


    @TypeConverter
    fun objectToString(obj: T): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    fun stringToObject(string: String?): T? {
        return gson.fromJson(string, (this.javaClass.genericSuperclass as ParameterizedType?)?.actualTypeArguments?.get(0))
    }

}