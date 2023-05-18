package com.liuxe.wordchallenge.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liuxe.wordchallenge.data.db.dao.BookDao
import com.liuxe.wordchallenge.data.db.dao.ErrorWordDao
import com.liuxe.wordchallenge.data.db.dao.UserDao
import com.liuxe.wordchallenge.data.entity.BookEntity
import com.liuxe.wordchallenge.data.entity.ErrorWordEntity
import com.liuxe.wordchallenge.data.entity.UserEntity

/**
 *  author : liuxe
 *  date : 2023/3/22 16:13
 *  description :
 */
@Database(
    entities = [ErrorWordEntity::class, BookEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao():UserDao
    abstract fun errorWordDao(): ErrorWordDao
    abstract fun bookDao(): BookDao

    companion object {

        @JvmStatic
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "app.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}