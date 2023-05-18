package com.liuxe.wordchallenge.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.liuxe.wordchallenge.data.bean.BookBean
import kotlinx.parcelize.Parcelize

/**
 *  author : liuxe
 *  date : 2023/4/18 16:46
 *  description :
 */

data class WordEntity(
    val wordIndex:Int,
    val name: String? = null,
    val trans: String? = null,
    val ukphone: String? = null,
    val usphone: String? = null,
    var fromBook:BookEntity? = null
)