package com.liuxe.wordchallenge.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * author : liuxe
 * date : 2023/5/12 11:08
 * description :
 */
class CustomTextView : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        //设置字体样式
        typeface = FontCustom.setFont(context)
    }
}