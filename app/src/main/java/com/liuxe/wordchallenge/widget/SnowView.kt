package com.liuxe.wordchallenge.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.liuxe.wordchallenge.R
import java.util.*

/**
 * author : liuxe
 * date : 2023/5/11 16:33
 * description :
 */
class SnowView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //画笔
    var mPaint: Paint

    //保存点的集合
    var mSnowBeanList: MutableList<SnowBean>

    //第一步测量
    //默认的View大小
    private val mDefaultWidth = dp2px(100)
    private val mDefaultHeight = dp2px(100)

    //测量过后的View 的大小  也就是画布的大小
    private var mMeasureWidth = 0
    private var mMeasureHeight = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //获取测量计算相关内容
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            //当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
            mMeasureWidth = widthSpecSize
        } else {
            //指定默认大小
            mMeasureWidth = mDefaultWidth
            if (widthSpecMode == MeasureSpec.AT_MOST) {
                mMeasureWidth = Math.min(mMeasureWidth, widthSpecSize)
            }
        }

        //测量计算View的高
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            //当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
            mMeasureHeight = heightSpecSize
        } else {
            //指定默认大小
            mMeasureHeight = mDefaultHeight
            if (heightSpecMode == MeasureSpec.AT_MOST) {
                mMeasureHeight = Math.min(mMeasureHeight, heightSpecSize)
            }
        }
        mMeasureHeight = mMeasureHeight - paddingBottom - paddingTop
        mMeasureWidth = mMeasureWidth - paddingLeft - paddingBottom
        //重新测量
        setMeasuredDimension(mMeasureWidth, mMeasureHeight)
    }

    //一个 dp 转 像素的计算
    private fun dp2px(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    //这里面创建 点
    var mRandom = Random()

    init {
        mPaint = Paint()
        mSnowBeanList = ArrayList()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        for (i in 0..49) {
            val lSnowBean = SnowBean()

            lSnowBean.alpha = mRandom.nextInt(150)+100
            //生成位置信息  随机
            //取值范围是 0 ~ mMeasureWidth
            val x = mRandom.nextInt(mMeasureWidth)
            val y = mRandom.nextInt(mMeasureHeight)

            //绘制使用的位置
            lSnowBean.postion = Point(x, y)
            //重置的位置
            lSnowBean.origin = Point(x, 0)
            //随机的半径  1 ~ 10
            lSnowBean.radius = mRandom.nextInt(3) * 3 + dp2px(1)
            //随机的速度  5 ~ 15
            lSnowBean.speed = 3 + mRandom.nextInt(10)
            lSnowBean.color = resources.getColor(R.color.white)
            val bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_snow)
            val matrix = Matrix()
            val scale = mRandom.nextFloat() + mRandom.nextInt(1) + 0.5f
            matrix.setScale(scale, scale)

            val newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            lSnowBean.bitmap = newbmp
            mSnowBeanList.add(lSnowBean)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //绘制时重新计算位置
        for (lSnowBean in mSnowBeanList) {
            val lPostion = lSnowBean.postion
            //在竖直方向上增加偏移
            lPostion.y += lSnowBean.speed

            //在 x 轴方向上再微微偏移一点
            val randValue = mRandom.nextFloat() * 2 + 0.5f
            lPostion.x -= randValue.toInt()

            //边界控制
            if (lPostion.y > mMeasureHeight) {
                lPostion.y = 0
                lPostion.x = mRandom.nextInt(mMeasureWidth)
            }
        }

        //先将这些点全部绘制出来
        for (lSnowBean in mSnowBeanList) {
            //修改画笔的颜色
            mPaint.color = lSnowBean.color
            mPaint.alpha =lSnowBean.alpha
            //绘制
            // 参数一 二 圆点位置
            // 参数 三 半径
            // 参数 四 画笔
            canvas.drawBitmap(
                lSnowBean.bitmap!!,
                lSnowBean.postion.x.toFloat(),
                lSnowBean.postion.y.toFloat(),
                mPaint
            )
            //            canvas.drawCircle(lBobbleBean.postion.x, lBobbleBean.postion.y, lBobbleBean.radius, mPaint);
        }

        //循环刷新 10 毫秒刷新一次
        postInvalidateDelayed(10L)
    }
}