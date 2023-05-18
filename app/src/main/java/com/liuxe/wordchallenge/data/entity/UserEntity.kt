package com.liuxe.wordchallenge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : liuxe
 *  date : 2023/4/19 09:52
 *  description :
 */
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 1,
    val userName: String = "",

    var wordNum: Int,//一共学习了多少单词
    var wordBookId: Int,//当前的单词书id

    var studyDays: Int,//学习多少天了
    var coin: Int,
    //金币 兑换人物 首次使用+100 每天打开+10 一个单词+1 完成关卡+5 看广告+20
    //金币可以购买 人物 背景 道具
)