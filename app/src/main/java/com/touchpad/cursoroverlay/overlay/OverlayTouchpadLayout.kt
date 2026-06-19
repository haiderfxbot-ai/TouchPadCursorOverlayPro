package com.touchpad.cursoroverlay.overlay

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

class OverlayTouchpadLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var dragStartX = 0f
    private var dragStartY = 0f
    private var initialX = 0
    private var initialY = 0
    private var isDragging = false

    var onPositionChanged: ((x: Int, y: Int) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dragStartX = event.rawX
                dragStartY = event.rawY
                initialX = (layoutParams as? android.view.WindowManager.LayoutParams)?.x ?: 0
                initialY = (layoutParams as? android.view.WindowManager.LayoutParams)?.y ?: 0
                isDragging = true
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    val params = layoutParams as? android.view.WindowManager.LayoutParams
                    if (params != null) {
                        val dx = (event.rawX - dragStartX).toInt()
                        val dy = (event.rawY - dragStartY).toInt()
                        params.x = initialX + dx
                        params.y = initialY + dy
                        onPositionChanged?.invoke(params.x, params.y)
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
