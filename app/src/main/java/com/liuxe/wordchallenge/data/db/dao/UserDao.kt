package com.liuxe.wordchallenge.data.db.dao

import androidx.room.*
import com.liuxe.wordchallenge.data.entity.ErrorWordEntity
import com.liuxe.wordchallenge.data.bean.BookBean
import com.liuxe.wordchallenge.data.entity.BookEntity
import com.liuxe.wordchallenge.data.entity.UserEntity

/**
 *  author : liuxe
 *  date : 2023/5/5 10:15
 *  description :
 */
@Dao
interface UserDao {
    //插入用户数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    //id 查询 用户
    @Query("SELECT * FROM user WHERE user.userId = 1")
    fun queryUser(): UserEntity

    //更新用户信息
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: UserEntity)
}