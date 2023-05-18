package com.liuxe.wordchallenge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : liuxe
 *  date : 2023/5/5 10:09
 *  description :
 */
@Entity(tableName = "errorWord")
data class ErrorWordEntity(
    @PrimaryKey(autoGenerate = true)
    val errorId: Int = 0,
    val name: String? = null,
    val trans: String? = null,
    val ukphone: String? = null,
    val usphone: String? = null,
    val fromBook: String?,//来源
    var creatTime: Long?, //插入时间
    var deleteTime: Long?//删除时间
)