package com.moyehics.news.util

import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun DateToTimeFormat(oldstringDate:String):String{
            var prettyDate=PrettyTime(Locale(getCountry()))
            var isTime:String ?= null
            try {
                var sdf:SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.ENGLISH)
                var date =sdf.parse(oldstringDate)
                isTime=prettyDate.format(date)
            }catch (e: ParseException){
                e.printStackTrace()
            }
            return isTime!!
        }
        fun getCountry():String{
            var local =Locale.getDefault()
            var country = local.country
            return country.lowercase()
        }
    }
}