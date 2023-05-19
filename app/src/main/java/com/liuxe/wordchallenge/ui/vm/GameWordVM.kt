package com.liuxe.wordchallenge.ui.vm

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.liuxe.wordchallenge.base.BaseViewModel
import com.liuxe.wordchallenge.data.bean.WordQuestion
import com.liuxe.wordchallenge.data.entity.BookEntity
import com.liuxe.wordchallenge.data.entity.UserEntity
import com.liuxe.wordchallenge.data.entity.WordEntity
import com.liuxe.wordchallenge.data.repository.RepositoryFactory
import com.liuxe.wordchallenge.http.api.doSuccess
import com.liuxe.wordchallenge.utils.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by Liuxe on 2023/5/13 14:39
 *desc : 游戏VM
 */
class GameWordVM : BaseViewModel() {
    private val repository = RepositoryFactory.makeRepository()

    val wordsSize = 10
    var questionList = ArrayList<WordQuestion>()
    val wordsList = ArrayList<WordEntity>()
    lateinit var book: BookEntity
    lateinit var user: UserEntity

    var curQuestionPosition = 0

    fun initGameWord(bookId: Int = 1) = viewModelScope.launch(Dispatchers.IO) {
        repository.queryBook(bookId).collectLatest {
            wordsList.clear()
            questionList.clear()
            curQuestionPosition = 0
            book = it
            repository.loadWords(book.url).collectLatest { res ->
                res.doSuccess {
                    //获得所有单词
                    var data = it
                    data.indices.forEach { index ->
                        wordsList.add(
                            WordEntity(
                                wordIndex = index,
                                name = data[index].name,
                                trans = initTrans(data[index].trans!!),
                                ukphone = data[index].ukphone,
                                usphone = data[index].usphone,
                                fromBook = book
                            )
                        )
                    }
                    //截取数据
                    val wordData = if (book.wordIndex + wordsSize > wordsList.size) {
                        wordsList.subList(wordsList.size - wordsSize, wordsList.size)
                    } else {
                        wordsList.subList(book.wordIndex, book.wordIndex + wordsSize)
                    }

                    //组装问题
                    val randomStart = 0
                    val randomEnd = wordsList.size

                    wordData.forEach {
                        var index1 = (randomStart until randomEnd).random()
                        while (index1 == book.wordIndex) {
                            index1 = (randomStart until randomEnd).random()
                        }
                        questionList.add(
                            WordQuestion(
                                word = it,
                                otherWord = wordsList[index1],
                                errorAnswerPosition = (0 until 2).random()
                            )
                        )
                    }
                }
            }
        }

        repository.getUser().collectLatest {
            user = it
        }
    }

    /**
     * 获取下一道题
     */

    fun nextQustion() = liveData<WordQuestion> {
        emit(questionList[curQuestionPosition])
        curQuestionPosition++
    }

    /**
     * 处理挑战失败
     */
    fun handleFail() = liveData {
        book.wordIndex = questionList[curQuestionPosition - 1].word.wordIndex
        //单词书更新
        repository.updateBook(book).collectLatest {

            user.coin = user.coin + curQuestionPosition + 1
            user.wordNum = user.wordNum + curQuestionPosition + 1
            //更新用户信息
            repository.updateUser(user).collectLatest {
                emit((curQuestionPosition + 1) * 10)
            }

        }

    }

    /**
     * 处理挑战成功
     */
    fun handleSuccess() = liveData {
        if (questionList[curQuestionPosition - 1].word.wordIndex + 1 == book.wordNum) {
            //可以解锁下一本
            book.wordIndex = 0
            //单词书更新
            repository.updateBook(book).collectLatest {
                //获取用户信息
                user.coin = user.coin + 10
                user.wordNum = user.wordNum + 10
                //更新用户信息
                repository.updateUser(user).collectLatest {
                    //解锁下一本 id +1
                    repository.queryBook(book.bookId + 1).collectLatest {
                        val book = it
                        book.isLock = false
                        repository.updateBook(book).collectLatest {
                            emit(100)
                        }
                    }

                }

            }

        } else {

            book.wordIndex = questionList[curQuestionPosition - 1].word.wordIndex
            //单词书更新
            repository.updateBook(book).collectLatest {
                //获取用户信息
                user.wordNum = user.wordNum + 10
                //更新用户信息
                repository.updateUser(user).collectLatest {
                    emit(100)
                }
            }

        }
    }


    fun initTrans(list: List<String>): String = list[0].split("；")[0]

}
