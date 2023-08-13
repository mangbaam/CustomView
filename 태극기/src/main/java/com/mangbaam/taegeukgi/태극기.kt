package com.mangbaam.taegeukgi

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

class 태극기(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {
    private val 물감 = Paint()
    private val 태극문양회전각도 = Math.toDegrees(atan(2.0 / 3)).toFloat()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        // 태극 문양
        물감.apply {
            val 태극반지름 = width / 6F
            val 태극문양영역 = RectF(
                태극반지름 * 2,
                height / 2 - 태극반지름,
                태극반지름 * 4,
                height / 2 + 태극반지름,
            )
            color = Color.RED
            canvas.drawArc(태극문양영역, 180 + 태극문양회전각도, 180F, true, this)

            color = Color.BLUE
            canvas.drawArc(태극문양영역, 태극문양회전각도, 180F, true, this)

            color = Color.CYAN
            Log.d("mangbaam_태극기", "onDraw: x: ${cos(Math.toRadians(태극문양회전각도.toDouble()))}, y: ${Math.toDegrees(sin(태극문양회전각도).toDouble())}")

            /*canvas.drawCircle(
                (width + cos(Math.toRadians(태극문양회전각도.toDouble()))).toFloat() / 2,
                (height - Math.toDegrees(sin(태극문양회전각도).toDouble()).toFloat()) / 2,
                태극반지름 / 2,
                this,
            )

            color = Color.MAGENTA
            canvas.drawCircle(
                (width - Math.toDegrees(cos(태극문양회전각도).toDouble()).toFloat()) / 2,
                (height + Math.toDegrees(sin(태극문양회전각도).toDouble()).toFloat()) / 2,
                태극반지름 / 2,
                this,
            )*/
        }
        // 건괘

        // 곤괘

        // 감괘

        // 이괘
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // TODO 3:2 비율
    }
}
