package com.touchpad.cursoroverlay.overlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.view.View

class CursorView(context: Context) : View(context) {

    private val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.parseColor("#4FD1C5")
    }

    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#4FD1C5")
    }

    private var cursorRadius = 10f
    private var cursorOpacity = 1.0f

    fun updateSize(sizeDp: Int) {
        val density = resources.displayMetrics.density
        cursorRadius = sizeDp * density / 2f
        outerPaint.shader = RadialGradient(
            cursorRadius, cursorRadius, cursorRadius,
            Color.parseColor("#334FD1C5"),
            Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        )
        val newSize = (cursorRadius * 2 + 4f).toInt()
        val params = layoutParams
        if (params != null) {
            params.width = newSize
            params.height = newSize
            layoutParams = params
        }
        invalidate()
    }

    fun updateOpacity(opacity: Float) {
        cursorOpacity = opacity.coerceIn(0.0f, 1.0f)
        alpha = cursorOpacity
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f

        canvas.drawCircle(cx, cy, cursorRadius + 2f, borderPaint)
        canvas.drawCircle(cx, cy, cursorRadius, outerPaint)
        canvas.drawCircle(cx, cy, 3f, centerPaint)
    }
}
