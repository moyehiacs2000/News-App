package com.moyehics.news.roomdp

import androidx.room.TypeConverter
import com.moyehics.news.data.model.news.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source) :  String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String):Source{
        return Source(name,name)
    }
}