package com.liuxe.wordchallenge.data.bean

import com.liuxe.wordchallenge.data.entity.WordEntity

/**
 *  author : liuxe
 *  date : 2023/4/20 09:18
 *  description :
 */
data class WordQuestion(
    var word: WordEntity, //考察单词
    var otherWord: WordEntity, //干扰项
    var errorAnswerPosition: Int = 0 //0 表示a 其他 表示b
)
