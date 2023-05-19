package com.mangbaam.customview.ui.blurview

import android.view.MotionEvent
import android.view.View

class DraggableTouchListener : View.OnTouchListener {
    private var lastX: Float = 0f
    private var lastY: Float = 0f

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY

                val newX = view.x + deltaX
                val newY = view.y + deltaY

                view.x = newX
                view.y = newY

                lastX = x
                lastY = y
            }
        }
        return true
    }
}
