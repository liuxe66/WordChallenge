package com.liuxe.wordchallenge.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liuxe.wordchallenge.app.MyApp
import com.liuxe.wordchallenge.base.BaseRepository
import com.liuxe.wordchallenge.http.api.ApiService
import com.liuxe.wordchallenge.data.entity.BookEntity
import com.liuxe.wordchallenge.data.entity.UserEntity
import com.liuxe.wordchallenge.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

/**
 *  author : liuxe
 *  date : 2023/4/18 17:28
 *  description : 单词数据仓库
 */
class DataRepository : BaseRepository() {
    private val api: ApiService = createApi()

    suspend fun loadBooks() = flow<List<BookEntity>> {
        // 解析Json数据
        val newstringBuilder = StringBuilder()
        var inputStream: InputStream? = null
        try {
            inputStream = MyApp.context.assets.open("book.json")
            val isr = InputStreamReader(inputStream)
            val reader = BufferedReader(isr)
            var jsonLine: String?
            while (reader.readLine().also { jsonLine = it } != null) {
                newstringBuilder.append(jsonLine)
            }
            reader.close()
            isr.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val str = newstringBuilder.toString()
        LogUtils.json(str)
        val gson = Gson()
        val bookType: Type = object : TypeToken<List<BookEntity>>() {}.type
        var bookList: MutableList<BookEntity> = gson.fromJson(str, bookType)

        bookList.indices.forEach {index ->

            var data = bookList[index]
            data.isLock = false
            MyApp.appDatabase.bookDao().insertBook(data)
        }

        MyApp.appDatabase.userDao().insertUser(
            UserEntity(userId = 1, userName = "", wordNum = 0, wordBookId = 1, studyDays = 1, coin = 100)
        )
        emit(bookList)
    }.flowOn(Dispatchers.IO)


    /**
     * 加载单词
     * @param bookName String
     * @return [Error type: Return type for function cannot be resolved]
     */
    suspend fun loadWords(bookName:String) = tryCatch {
        api.getAllWord(bookName)
    }

    /**
     * 获取用户信息
     * @return Flow<UserEntity>
     */
    suspend fun getUser() = flow<UserEntity> {
        var data = MyApp.appDatabase.userDao().queryUser()
        emit(data)
    }.flowOn(Dispatchers.IO)

    /**
     * 插入用户信息
     * @param user UserEntity
     * @return Flow<UserEntity>
     */
    suspend fun insertUser(user:UserEntity) = flow<UserEntity> {
        MyApp.appDatabase.userDao().insertUser(user)
        emit(user)
    }.flowOn(Dispatchers.IO)

    /**
     * 更新用户信息
     * @param user UserEntity
     * @return Flow<UserEntity>
     */
    suspend fun updateUser(user:UserEntity) = flow<UserEntity> {
        MyApp.appDatabase.userDao().updateUser(user)
        emit(user)
    }.flowOn(Dispatchers.IO)


    /**
     * 更新单词书
     * @param bookEntity BookEntity
     * @return Flow<BookEntity>
     */
    suspend fun updateBook(bookEntity: BookEntity) = flow<BookEntity> {
        MyApp.appDatabase.bookDao().updateBook(bookEntity)
        emit(bookEntity)
    }.flowOn(Dispatchers.IO)

    /**
     * 插入单词书
     */
    suspend fun insertBook(bookEntity: BookEntity) = flow<BookEntity> {
        MyApp.appDatabase.bookDao().insertBook(bookEntity)
        emit(bookEntity)
    }.flowOn(Dispatchers.IO)

    /**
     * 查询
     * @param bookEntity BookEntity
     * @return Flow<BookEntity>
     */
    suspend fun queryBook(bookId: Int) = flow<BookEntity> {
        var data =MyApp.appDatabase.bookDao().queryBook(bookId)
        emit(data)
    }.flowOn(Dispatchers.IO)

    /**
     * 查询所有单词本
     */
    suspend fun queryBookAll() = flow<List<BookEntity>> {
        var data =MyApp.appDatabase.bookDao().queryAll()
        emit(data)
    }.flowOn(Dispatchers.IO)

}