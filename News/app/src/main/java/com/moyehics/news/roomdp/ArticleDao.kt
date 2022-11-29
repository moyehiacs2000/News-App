package com.moyehics.news.roomdp

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moyehics.news.data.model.news.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article : Article):Long

    @Query("Select * from articles")
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}