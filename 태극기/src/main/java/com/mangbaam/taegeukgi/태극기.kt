package com.mangbaam.taegeukgi

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

class 태극기(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {
    private val 태극문양물감 = 물감()
    private val 괘_물감 = 물감().apply {
        color = 검정
        style = Paint.Style.FILL
    }
    private lateinit var 태극문양영역: RectF
    private var 태극반지름: Float = 0F
    private val 태극문양회전각도 = Math.toDegrees(atan(2.0 / 3)).toFloat()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(하양)

        // 태극 문양
        태극문양물감.apply {
            color = 빨강
            canvas.drawArc(태극문양영역, 180 + 태극문양회전각도, 180F, true, this)

            color = 파랑
            canvas.drawArc(태극문양영역, 태극문양회전각도, 180F, true, this)

            color = 빨강

            val 가로중앙 = width / 2F
            val 세로중앙 = height / 2F
            canvas.drawCircle(
                가로중앙 + (태극반지름 / 2) * cos(Math.toRadians(태극문양회전각도 + 180.0)).toFloat(),
                세로중앙 + (태극반지름 / 2) * sin(Math.toRadians(태극문양회전각도 + 180.0)).toFloat(),
                태극반지름 / 2,
                this,
            )

            color = 파랑
            canvas.drawCircle(
                가로중앙 + (태극반지름 / 2) * cos(Math.toRadians(태극문양회전각도.toDouble())).toFloat(),
                세로중앙 + (태극반지름 / 2) * sin(Math.toRadians(태극문양회전각도.toDouble())).toFloat(),
                태극반지름 / 2,
                this,
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val 너비 = MeasureSpec.getSize(widthMeasureSpec)
        val 높이 = (너비 * 2F / 3).toInt()
        setMeasuredDimension(너비, 높이)

        태극반지름 = 너비 / 6F
        태극문양영역 = RectF(
            태극반지름 * 2,
            높이 / 2 - 태극반지름,
            태극반지름 * 4,
            높이 / 2 + 태극반지름,
        )
    }

    companion object {
        const val 하양 = Color.WHITE
        const val 검정 = Color.BLACK
        const val 파랑 = Color.BLUE
        const val 빨강 = Color.RED
    }
}

typealias 물감 = Paint
