package com.liuxe.wordchallenge.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.liuxe.wordchallenge.base.BaseViewModel
import com.liuxe.wordchallenge.data.entity.BookEntity
import com.liuxe.wordchallenge.data.entity.UserEntity
import com.liuxe.wordchallenge.data.repository.RepositoryFactory
import kotlinx.coroutines.flow.collectLatest

/**
 *  author : liuxe
 *  date : 2023/3/22 16:33
 *  description :
 */
class MainVM : BaseViewModel() {
    private val repository = RepositoryFactory.makeRepository()

    private var curBookIndex = 0
    private val _bookIndex = MutableLiveData<Int>()
    var bookIndex = _bookIndex
    private val bookList = ArrayList<BookEntity>()

    /**
     * 获取用户信息
     * @return LiveData<UserEntity>
     */
    fun loadUser() = liveData {
        repository.getUser().collectLatest {
            emit(it)
        }
    }
    /**
     * 更新用户信息
     * @return LiveData<UserEntity>
     */
    fun updateUser(userEntity: UserEntity) = liveData<UserEntity> {
        repository.updateUser(userEntity).collectLatest {
            emit(it)
        }
    }

    /**
     * 插入用户信息
     * @param userEntity UserEntity
     * @return LiveData<UserEntity>
     */
    fun insertUser(userEntity: UserEntity) = liveData<UserEntity> {
        repository.insertUser(userEntity).collectLatest {
            emit(it)
        }
    }

    /**
     * 加载单词本
     * @return [Error type: Return type for function cannot be resolved]
     */
    fun loadAllBook() = liveData {
        repository.queryBookAll().collectLatest {
            bookList.addAll(it)
            _bookIndex.value = 0
            emit(bookList)
        }

    }

    /**
     * 更新单词书
     * @param bookEntity BookEntity
     * @return LiveData<BookEntity>
     */
    fun updateBook(bookEntity: BookEntity) = liveData<BookEntity> {
        repository.updateBook(bookEntity).collectLatest {
            emit(it)
        }
    }

    /**
     * 根据id 查询单词书
     * @param bookId Int
     * @return LiveData<BookEntity>
     */
    fun queryBook(bookId: Int) = liveData<BookEntity> {
        repository.queryBook(bookId).collectLatest {
            emit(it)
        }
    }

    /**
     * 当前单词书
     * @return BookEntity
     */
    fun getCurBook() = bookList[curBookIndex]

    /**
     * 当前单词书Id
     * @return Int
     */
    fun getCurBookId() = bookList[curBookIndex].bookId

    /**
     * 加载单词书
     * @return LiveData<String>
     */
    fun init() = liveData<String> {
        repository.loadBooks().collectLatest {
            emit("success")
        }
    }

    /**
     * 左按钮
     */
    fun toLeft() {
        if (curBookIndex == 0) {
            curBookIndex = 0
        } else {
            curBookIndex--
        }
        _bookIndex.value = curBookIndex
    }

    /**
     * 右按钮
     */
    fun toRight() {
        if (curBookIndex == bookList.size - 1) {
            curBookIndex = bookList.size - 1
        } else {
            curBookIndex++
        }
        _bookIndex.value = curBookIndex
    }


    /**
     * 当前单词书位置
     * @param index Int
     */
    fun setCurIndex(index: Int) {
        curBookIndex = index
    }


}