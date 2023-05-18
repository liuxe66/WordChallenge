package com.liuxe.wordchallenge.app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.app.MyApp

/**
 *  author : liuxe
 *  date : 2023/3/24 13:42
 *  description :
 */
class AppConfig {
    companion object {
        fun getPicWithNumber(number: Int): Bitmap = when (number) {
            0 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n0)
            1 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n1)
            2 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n2)
            3 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n3)
            4 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n4)
            5 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n5)
            6 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n6)
            7 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n7)
            8 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n8)
            9 -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n9)
            else -> BitmapFactory.decodeResource(MyApp.context.resources, R.drawable.n0)
        }

        fun getPreson(number: Int):Int = when(number){
            1 -> R.drawable.p1anim
            2 -> R.drawable.p2anim
            3 -> R.drawable.p3anim
            4 -> R.drawable.p4anim
            5 -> R.drawable.p5anim
            0 -> R.drawable.p6anim
            else ->R.drawable.p1anim
        }
    }
}