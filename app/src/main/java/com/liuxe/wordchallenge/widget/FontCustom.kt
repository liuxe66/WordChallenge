package com.liuxe.wordchallenge.widget

import android.content.Context
import android.graphics.Typeface

/**
 * author : liuxe
 * date : 2023/5/12 11:10
 * description :
 */
object FontCustom {
    // fongUrl是自定义字体分类的名称
    private const val fongUrl = "PangMenZhengDaoBiaoTiTi-1.ttf"

    //Typeface是字体，这里我们创建一个对象
    private var tf: Typeface? = null

    /**
     * 设置字体
     */
    fun setFont(context: Context): Typeface? {
        if (tf == null) {
            //给它设置你传入的自定义字体文件，再返回回来
            tf = Typeface.createFromAsset(context.assets, fongUrl)
        }
        return tf
    }
}