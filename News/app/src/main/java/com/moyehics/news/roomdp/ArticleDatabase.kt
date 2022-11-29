package com.moyehics.news.roomdp

import android.content.Context
import androidx.room.*
import com.moyehics.news.data.model.news.Article


@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao():ArticleDao

    companion object{
        private var INSTANCE : ArticleDatabase ?= null
        fun getDatabase(context: Context):ArticleDatabase{
            if (INSTANCE == null){

                    INSTANCE= Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java,
                        "article_database"
                    ).build()

            }
            return INSTANCE!!
        }
    }
}