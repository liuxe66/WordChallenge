package com.liuxe.wordchallenge.data.db.dao

import androidx.room.*
import com.liuxe.wordchallenge.data.entity.ErrorWordEntity

/**
 *  author : liuxe
 *  date : 2023/5/5 10:15
 *  description :
 */
@Dao
interface ErrorWordDao {
    //插入错误单词
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertErrorWord(errorWord: ErrorWordEntity)

    //根据book 查询错误单词
    @Query("SELECT * FROM errorWord WHERE errorWord.fromBook = :bookName")
    fun queryErrorWord(bookName: String): List<ErrorWordEntity>

    //更新错误单词
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateErrorWord(errorWord: ErrorWordEntity)

    //删除单词
    @Delete
    fun deleteErrorWord(errorWord: ErrorWordEntity)
}