package com.liuxe.wordchallenge.widget

import android.graphics.Bitmap
import android.graphics.Point

/**
 * author : liuxe
 * date : 2023/5/11 16:33
 * description :
 */
class SnowBean {
    //位置
    var postion: Point = Point()

    //初始位置
    var origin: Point = Point()

    //颜色
    var color = 0

    //运动的速度
    var speed = 0

    //半径
    var radius = 0
    var bitmap: Bitmap? = null

    //透明度0~255
    var alpha = 0
}