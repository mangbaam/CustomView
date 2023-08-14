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

    private val 태극문양회전각도 = Math.toDegrees(atan(2.0 / 3)).toFloat()
    private var 너비 = 0
    private var 높이 = 0
    private val 태극문양영역: RectF
        get() = RectF(
            태극반지름 * 2,
            높이 / 2 - 태극반지름,
            태극반지름 * 4,
            높이 / 2 + 태극반지름,
        )
    private val 태극반지름: Float
        get() = 너비 / 6F
    private val 가로중앙
        get() = 너비 / 2F
    private val 세로중앙
        get() = 높이 / 2F

    private val 괘_높이
        get() = 태극반지름 / 6
    private val 괘_간격
        get() = 태극반지름 / 12
    private val 괘_너비
        get() = 태극반지름
    private val 괘_중앙으로부터_거리
        get() = 높이 * (3F / 8)

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

        canvas.apply {
            save()
            // 건괘
            rotate(90 + 태극문양회전각도, 가로중앙, 세로중앙)
            repeat(3) {
                괘_그리기(this, 괘_중앙으로부터_거리 + it * 괘_간격 + (it + 1) * 괘_높이)
            }
            restore()
        }
    }

    private fun 괘_그리기(캔버스: Canvas, 높이: Float, 작은괘: Boolean = false) {
        if (작은괘) {
            캔버스.drawRect(
                가로중앙 - 괘_너비 / 2,
                세로중앙 + 높이 + 괘_높이,
                가로중앙 - 괘_간격 / 2,
                세로중앙 + 높이,
                괘_물감,
            )
            캔버스.drawRect(
                가로중앙 + 괘_간격 / 2,
                세로중앙 + 높이 + 괘_높이,
                가로중앙 + 괘_너비 / 2,
                세로중앙 + 높이,
                괘_물감,
            )
        } else {
            캔버스.drawRect(
                가로중앙 - 괘_너비 / 2,
                세로중앙 + 높이 + 괘_높이,
                가로중앙 + 괘_너비 / 2,
                세로중앙 + 높이,
                괘_물감,
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        너비 = MeasureSpec.getSize(widthMeasureSpec)
        높이 = (너비 * 2F / 3).toInt()
        setMeasuredDimension(너비, 높이)
    }

    companion object {
        const val 하양 = Color.WHITE
        const val 검정 = Color.BLACK
        const val 파랑 = Color.BLUE
        const val 빨강 = Color.RED
    }
}

typealias 물감 = Paint
