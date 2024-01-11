package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar

class DoubleThumbProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var progress: Float = 0f
    var secondaryProgress: Float = 0f
    private var progressBarHeight: Float = 20f // 进度条高度
    private var progressColor: Int = Color.BLUE // 进度条颜色
    private var secondaryProgressColor: Int = Color.GRAY // 次进度条颜色
    private var thumbDrawable: Drawable? = null // 用于存储thumb的Drawable
    private var thumbDrawable2: Drawable? = null // 用于存储第二个thumb的Drawable

    init {
        // 从资源中加载图片作为thumb
        thumbDrawable = resources.getDrawable(R.drawable.slide, null)
        thumbDrawable2 = resources.getDrawable(R.drawable.slide, null)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DoubleThumbProgressBar)
        progress = typedArray.getFloat(R.styleable.DoubleThumbProgressBar_progress, 0f)
        secondaryProgress = typedArray.getFloat(R.styleable.DoubleThumbProgressBar_secondaryProgress, 0f)
        progressBarHeight = typedArray.getDimension(R.styleable.DoubleThumbProgressBar_progressBarHeight, 20f)
        progressColor = typedArray.getColor(R.styleable.DoubleThumbProgressBar_progressColor, Color.BLUE)
        secondaryProgressColor = typedArray.getColor(R.styleable.DoubleThumbProgressBar_secondaryProgressColor, Color.GRAY)
        typedArray.recycle()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < width * progress) {
                    // 第一个thumb被按下
                    val newProgress = event.x / width
                    setProgressAndSecondaryProgress(newProgress, secondaryProgress)
                    return true
                } else if (event.x > width * secondaryProgress) {
                    // 第二个thumb被按下
                    val newSecondaryProgress = event.x / width
                    setProgressAndSecondaryProgress(progress, newSecondaryProgress)
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.x < width * progress && event.x >= 0) {
                    // 第一个thumb移动
                    val newProgress = event.x / width
                    setProgressAndSecondaryProgress(newProgress, secondaryProgress)
                    return true
                } else if (event.x > width * secondaryProgress && event.x <= width) {
                    // 第二个thumb移动
                    val newSecondaryProgress = event.x / width
                    setProgressAndSecondaryProgress(progress, newSecondaryProgress)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val progressWidth = width * progress
        val secondaryProgressWidth = width * secondaryProgress

        val progressRect = RectF(0f, height / 2 - progressBarHeight / 2, progressWidth, height / 2 + progressBarHeight / 2)
        val secondaryProgressRect = RectF(0f, height / 2 - progressBarHeight / 2, secondaryProgressWidth, height / 2 + progressBarHeight / 2)

        val progressPaint = Paint()
        progressPaint.color = progressColor
        canvas.drawRoundRect(progressRect, progressBarHeight / 2, progressBarHeight / 2, progressPaint)

        val secondaryProgressPaint = Paint()
        secondaryProgressPaint.color = secondaryProgressColor
        canvas.drawRoundRect(secondaryProgressRect, progressBarHeight / 2, progressBarHeight / 2, secondaryProgressPaint)

        // 绘制第一个图片作为第一个thumb
        thumbDrawable?.let {
            it.setBounds((progressWidth - it.intrinsicWidth / 2).toInt(), (height / 2 - it.intrinsicHeight / 2).toInt(), (progressWidth + it.intrinsicWidth / 2).toInt(), (height / 2 + it.intrinsicHeight / 2).toInt())
            it.draw(canvas)
        }

        // 绘制第二个图片作为第二个thumb
        thumbDrawable2?.let {
            it.setBounds((secondaryProgressWidth - it.intrinsicWidth / 2).toInt(), (height / 2 - it.intrinsicHeight / 2).toInt(), (secondaryProgressWidth + it.intrinsicWidth / 2).toInt(), (height / 2 + it.intrinsicHeight / 2).toInt())
            it.draw(canvas)
        }
    }
    inline fun setProgressAndSecondaryProgress(progress: Float, secondaryProgress: Float) {
        this.progress = progress
        this.secondaryProgress = secondaryProgress
        invalidate()
    }
}
