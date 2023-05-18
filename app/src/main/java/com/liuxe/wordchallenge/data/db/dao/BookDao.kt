package com.liuxe.wordchallenge.data.db.dao

import androidx.room.*
import com.liuxe.wordchallenge.data.entity.ErrorWordEntity
import com.liuxe.wordchallenge.data.bean.BookBean
import com.liuxe.wordchallenge.data.entity.BookEntity

/**
 *  author : liuxe
 *  date : 2023/5/5 10:15
 *  description :
 */
@Dao
interface BookDao {
    //插入错误单词
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: BookEntity)

    //id 查询 book
    @Query("SELECT * FROM book WHERE book.bookId = :bookId")
    fun queryBook(bookId: Int): BookEntity

    @Query("SELECT * FROM book")
    fun queryAll():List<BookEntity>

    //更新错误单词
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateBook(book: BookEntity)
}