package com.touchpad.cursoroverlay.overlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TouchpadSurface @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 0.5f
        color = Color.argb(30, 255, 255, 255)
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.argb(60, 255, 255, 255)
    }

    private val cornerRadius = 16f
    private val gridSize = 30f
    private var lastX = 0f
    private var lastY = 0f
    private var isTouching = false

    var onCursorMove: ((dx: Float, dy: Float) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                isTouching = true
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastX
                val dy = event.y - lastY
                lastX = event.x
                lastY = event.y
                onCursorMove?.invoke(dx, dy)
                return true
            }
            MotionEvent.ACTION_UP -> {
                isTouching = false
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                isTouching = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        val bgRect = RectF(1f, 1f, w - 1f, h - 1f)
        canvas.drawRoundRect(bgRect, cornerRadius, cornerRadius, borderPaint)

        var x = gridSize
        while (x < w) {
            canvas.drawLine(x, 0f, x, h, gridPaint)
            x += gridSize
        }
        var y = gridSize
        while (y < h) {
            canvas.drawLine(0f, y, w, y, gridPaint)
            y += gridSize
        }

        val cx = w / 2f
        val cy = h / 2f
        val touchPointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            color = Color.argb(60, 79, 209, 197)
        }
        canvas.drawCircle(cx, cy, 20f, touchPointPaint)
        canvas.drawCircle(cx, cy, 10f, touchPointPaint)
        canvas.drawCircle(cx, cy, 4f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.argb(80, 79, 209, 197)
        })
    }
}
