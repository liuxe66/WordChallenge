package com.liuxe.wordchallenge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : liuxe
 *  date : 2023/5/5 10:09
 *  description :
 */
@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey
    val bookId: Int,
    val name: String,
    val url: String,
    val wordNum: Int,
    var wordIndex: Int = 1,
    var isLock:Boolean //是否加锁
)