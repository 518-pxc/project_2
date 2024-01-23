package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

@SuppressLint("UseCompatLoadingForDrawables")
class DoubleThumbProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var rangeMin = 0
    private var rangeMax = 100
    private var thumbDrawable: Drawable? = null
    private var thumbRadius = 0f  // 滑块半径
    private var thumbPaint = Paint()
    private var thumb1X = 0f  // 第一个滑块的X坐标
    private var thumb2X = 0f  // 第二个滑块的X坐标
    private var thumbY = 0f  // 滑块的Y坐标
    private var progressPaint = Paint()
    private var progressRect = RectF()

    // 使用可空类型声明属性
    private var tvAgeFirst: TextView? = null
    private var tvAgeSecond: TextView? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DoubleThumbProgressBar)
        rangeMin = typedArray.getInt(R.styleable.DoubleThumbProgressBar_rangeMin, 0)
        rangeMax = typedArray.getInt(R.styleable.DoubleThumbProgressBar_rangeMax, 100)
        thumbDrawable = context.getDrawable(R.drawable.ic_slide)
        typedArray.recycle()
        thumbRadius = thumbDrawable?.intrinsicWidth?.toFloat() ?: 2f // 设置滑块半径为滑块图片的宽度
        thumbPaint.isAntiAlias = true
        progressPaint.isAntiAlias = true
        progressPaint.color = Color.parseColor("#383838")
        if (!isInEditMode) {
            // 在非预览模式下初始化控件
            tvAgeFirst = findViewById(R.id.tvAgeFirst)
            tvAgeSecond = findViewById(R.id.tvAgeSecond)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //W:进度条的宽度
        super.onSizeChanged(w, h, oldw, oldh)
        thumbY = h / 2f
        thumb1X = w * (rangeMin - rangeMin) / (rangeMax - rangeMin).toFloat()
        thumb2X = w * (rangeMax - rangeMin) / (rangeMax - rangeMin).toFloat()
        // 调整滑块的位置，使其在进度条的最大值和最小值处完整显示
        // thumb1X -= thumbRadius / 2
        //thumb2X -= thumbRadius / 2
    }


    @SuppressLint("DrawAllocation")
    // 在 onDraw 方法中修改滑块图片的绘制部分
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制未划过区域的进度条
        val unselectedRect = RectF(0f, thumbY - 5, thumb1X, thumbY + 5)
        progressPaint.color = Color.parseColor("#DEE0E3")
        canvas.drawRect(unselectedRect, progressPaint)

        val unselectedRect2 = RectF(thumb2X, thumbY - 5, width.toFloat(), thumbY + 5)
        canvas.drawRect(unselectedRect2, progressPaint)

        // 绘制滑块划过的区域
        thumbPaint.color = Color.parseColor("#383838")
        val thumbRect = RectF(thumb1X, thumbY - 5, thumb2X, thumbY + 5)
        canvas.drawRect(thumbRect, thumbPaint)

        // 绘制滑块图片
        thumbDrawable?.let {
            // 获取滑块图片的 Bitmap 对象
            val thumbBitmap = (it as BitmapDrawable).bitmap
            // 计算第一个滑块图片的绘制范围
            val thumb1Left = thumb1X - 19 * thumbBitmap.width / 100
            val thumb1Right = thumb1X + 81 * thumbBitmap.width / 100
            val thumb1Top = thumbY - thumbBitmap.height / 2
            val thumb1Bottom = thumbY + thumbBitmap.height / 2
            // 计算第二个滑块图片的绘制范围
            val thumb2Left = thumb2X - 80.3 * thumbBitmap.width / 100
            val thumb2Right = thumb2X + 19.7 * thumbBitmap.width / 100
            val thumb2Top = thumbY - thumbBitmap.height / 2
            val thumb2Bottom = thumbY + thumbBitmap.height / 2

            // 绘制滑块图片
            it.setBounds(
                thumb1Left.toInt(),
                thumb1Top.toInt(),
                thumb1Right.toInt(),
                thumb1Bottom.toInt()
            )
            it.draw(canvas)

            it.setBounds(
                thumb2Left.toInt(),
                thumb2Top.toInt(),
                thumb2Right.toInt(),
                thumb2Bottom.toInt()
            )
            it.draw(canvas)
        }

        // 更新 TextView 中的值
        val minValue = (rangeMin + (thumb1X / width * (rangeMin - rangeMin))).toInt()
        val maxValue = (rangeMax + (thumb2X / width * (rangeMax - rangeMin))).toInt()
        tvAgeFirst?.text = minValue.toString()
        tvAgeSecond?.text = maxValue.toString()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // 根据手指位置更新滑块的位置
                if (event.x < thumb1X) {
                    thumb1X = event.x
                } else if (event.x > thumb2X) {
                    thumb2X = event.x
                } else {
                    if (Math.abs(event.x - thumb1X) < Math.abs(event.x - thumb2X)) {
                        thumb1X = event.x
                    } else {
                        thumb2X = event.x
                    }
                }

                // 确保滑块位置在范围内
                if (thumb1X < 0) {
                    thumb1X = 0f
                }
                if (thumb2X > width) {
                    thumb2X = width.toFloat()
                }
                if (thumb1X > thumb2X) {
                    thumb1X = thumb2X
                }
                if (thumb2X < thumb1X) {
                    thumb2X = thumb1X
                }

                invalidate()

                // 发送进度值改变的事件
                val minValue = (rangeMin + (thumb1X / width * (rangeMax - rangeMin))).toInt()
                val maxValue = (rangeMin + (thumb2X / width * (rangeMax - rangeMin))).toInt()
                onRangeChangedListener?.onRangeChanged(minValue, maxValue)
            }
        }
        return true
    }

    fun init(rangeMin: Int, rangeMax: Int) {
        this.rangeMin = rangeMin
        this.rangeMax = rangeMax
        invalidate()
    }

    interface OnRangeChangedListener {
        fun onRangeChanged(minValue: Int, maxValue: Int)
    }

    private var onRangeChangedListener: OnRangeChangedListener? = null

    fun setOnRangeChangedListener(listener: OnRangeChangedListener) {
        onRangeChangedListener = listener
    }
}
