package com.liuxe.wordchallenge.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix


/**
 *  author : liuxe
 *  date : 2023/5/12 09:08
 *  description :
 */
class PicNumber {

    fun creatNumberBitmap(num: Int) {
        num.toString().forEach {

        }
    }

    private fun mergeBitmap(firstBitmap: Bitmap, secondBitmap: Bitmap): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            firstBitmap.width + secondBitmap.width, firstBitmap.height, firstBitmap.config
        )
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(firstBitmap, Matrix(), null)
        canvas.drawBitmap(secondBitmap, firstBitmap.width.toFloat(), 0f, null)
        return bitmap
    }

}