package com.liuxe.wordchallenge.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.widget.FontCustom.setFont

/*
 * StrokeTextView的目标是给文字描边
 * 实现方法是两个TextView叠加,只有描边的TextView为底,实体TextView叠加在上面
 * 看上去文字就有个不同颜色的边框了
 */
class StrokeTextView : AppCompatTextView {
    private var borderText: TextView? = null ///用于描边的TextView

    constructor(context: Context?) : super(context!!) {
        borderText = TextView(context)

        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        borderText = TextView(context, attrs)

        init(context)
    }

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context!!, attrs, defStyle) {
        borderText = TextView(context, attrs, defStyle)

        init(context)
    }

    fun init(context: Context) {
        val tp1 = borderText!!.paint
        tp1.strokeWidth = 20f //设置描边宽度
        tp1.style = Paint.Style.STROKE //对文字只描边
        borderText!!.setTextColor(resources.getColor(R.color.text_border_color)) //设置描边颜色
        borderText!!.gravity = gravity
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        super.setLayoutParams(params)
        borderText!!.layoutParams = params
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val tt = borderText!!.text

        //两个TextView上的文字必须一致
        if (tt == null || tt != this.text) {
            borderText!!.text = text
            this.postInvalidate()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        borderText!!.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderText!!.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        borderText!!.draw(canvas)
        super.onDraw(canvas)
    }
}