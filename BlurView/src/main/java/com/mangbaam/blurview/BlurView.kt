package com.mangbaam.blurview

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import com.mangbaam.blurview.blurtool.BlurTool
import com.mangbaam.blurview.blurtool.EmptyBlurTool
import com.mangbaam.blurview.blurtool.RenderScriptBlurTool

class BlurView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var downsampleFactor: Float
    private var blurRadius: Float // 0 <= blurRadius <= 25 (0 은 Blur 처리 안 됨)

    private var dirty = false
    private var bitmapToBlur: Bitmap? = null
    private var blurredBitmap: Bitmap? = null
    private var blurringCanvas: Canvas? = null
    private var isRendering = false
    private var outlinePath: Path? = null

    private var leftTopRadius = 0f
    private var rightTopRadius = 0f
    private var rightBottomRadius = 0f
    private var leftBottomRadius = 0f

    private val activityRootView: View?
        get() {
            var ctx = context
            var i = 0
            while (i++ < 4 && ctx !is Activity && ctx is ContextWrapper) {
                ctx = ctx.baseContext
            }
            return (ctx as? Activity)?.window?.decorView
        }

    private var differentRoot = false

    private val blurTool: BlurTool =
        RenderScriptBlurTool.getInstanceIfPossible(this.context) ?: EmptyBlurTool()

    init {
        clipToOutline = true

        context.obtainStyledAttributes(attrs, R.styleable.BlurView).run {
            blurRadius = getDimension(
                R.styleable.BlurView_blurRadius,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_BLUR_RADIUS,
                    context.resources.displayMetrics,
                ),
            )
            downsampleFactor = getFloat(
                R.styleable.BlurView_downsampleFactor,
                DEFAULT_DOWNSAMPLE_FACTOR,
            )

            val cornerRadius = getDimension(
                R.styleable.BlurView_cornerRadius,
                0f,
            )
            leftTopRadius = getDimension(
                R.styleable.BlurView_leftTopRadius,
                cornerRadius,
            ).addIfPositive(1.dp)
            rightTopRadius = getDimension(
                R.styleable.BlurView_rightTopRadius,
                cornerRadius,
            ).addIfPositive(1.dp)
            rightBottomRadius = getDimension(
                R.styleable.BlurView_rightBottomRadius,
                cornerRadius,
            ).addIfPositive(1.dp)
            leftBottomRadius = getDimension(
                R.styleable.BlurView_leftBottomRadius,
                cornerRadius,
            ).addIfPositive(1.dp)

            recycle()
        }

        outlinePath = Path()
    }

    /**
     * ## Blur radius 설정
     *
     * 블러의 강도를 설정한다.
     * @param radius 0 ~ 25 사이의 값, 0 은 블러 적용 안 됨
     * */
    fun setBlurRadius(radius: Float) {
        if (blurRadius == radius) return

        blurRadius = radius
        dirty = true
        invalidate()
    }

    /**
     * ## Downsample Factor 설정
     *
     * 이미지의 화질을 저하시킨다. 값이 클수록 화질이 저하되며, 블러 처리 비용이 줄어든다. 단, 값이 클수록 Blockiness 현상(타일 현상)이 심해진다
     *
     * @param factor 0 보다 큰 값
     * */
    fun setDownsampleFactor(factor: Float) {
        require(factor > 0) { "Downsample factor must be greater than 0." }

        if (downsampleFactor != factor) {
            downsampleFactor = factor
            dirty = true
            releaseBitmap()
            invalidate()
        }
    }

    /**
     * ## 코너 Radius 설정
     *
     * BlurView 의 네 모서리에 곡률 처리
     * */
    fun setCornerRadius(radius: Float) {
        setCornerRadius(radius, radius, radius, radius)
    }

    /**
     * ## 코너 Radius 설정
     *
     * BlurView 의 각 모서리에 곡률 처리
     * */
    fun setCornerRadius(leftTop: Float, rightTop: Float, rightBottom: Float, leftBottom: Float) {
        leftTopRadius = leftTop.addIfPositive(1.dp)
        rightTopRadius = rightTop.addIfPositive(1.dp)
        rightBottomRadius = rightBottom.addIfPositive(1.dp)
        leftBottomRadius = leftBottom.addIfPositive(1.dp)
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        activityRootView?.let {
            it.viewTreeObserver.addOnPreDrawListener(preDrawListener)
            differentRoot = it.rootView !== rootView // 현재 BlurView 가 액티비티의 루트 뷰에 포함되어있는지 확인
            if (differentRoot) {
                it.postInvalidate()
            }
        } ?: run { differentRoot = false }
    }

    /**
     * ## draw() 호출 전 수행할 동작
     *
     * - [전처리][prepare]
     * - 위치 계산
     * - 캔버스 설정(스케일 및 이동)
     * - 배경에 blurredBitmap 그리도록 설정
     * */
    private val preDrawListener = ViewTreeObserver.OnPreDrawListener {
        val oldBmp = blurredBitmap
        val decor: View = activityRootView ?: return@OnPreDrawListener false

        if (!isShown) return@OnPreDrawListener true

        // 전처리
        prepare().also { blurrable ->
            if (!blurrable) return@OnPreDrawListener false
        }

        val shouldRedraw = blurredBitmap != oldBmp

        val locations = IntArray(2)

        // Activity 의 RootView 에서의 좌표
        decor.getLocationOnScreen(locations)
        var x = locations[0]
        var y = locations[1]

        // BlurView 가 속한 Window 에서의 좌표
        getLocationOnScreen(locations)
        x -= locations[0]
        y -= locations[1]

        val bitmapToBlur = bitmapToBlur ?: return@OnPreDrawListener false
        val blurringCanvas = blurringCanvas ?: return@OnPreDrawListener false

        val saveCount: Int = blurringCanvas.save()
        isRendering = true
        RENDERING_COUNT++

        try {
            blurringCanvas.scale(
                bitmapToBlur.width.toFloat() / width,
                bitmapToBlur.height.toFloat() / height,
            )
            blurringCanvas.translate(x.toFloat(), y.toFloat())
            decor.draw(blurringCanvas)
        } catch (_: StopException) {
        } finally {
            isRendering = false
            RENDERING_COUNT--
            blurringCanvas.restoreToCount(saveCount)
        }

        // Blur 처리
        blur(this.bitmapToBlur, blurredBitmap)

        if (shouldRedraw || differentRoot) {
            invalidate()
        }
        true
    }

    /**
     * ## 블러 전처리
     *
     * - bitmapToBlur, blurredBitmap 에 Bitmap 객체 할당 및 blurringCanvas 에 bitmapToBlur 설정
     * - blurTool 준비(prepare)
     *
     * @return 블러 처리를 위한 전처리가 성공적으로 완료됐다면 `true`, 그렇지 않다면 `false`
     * */
    private fun prepare(): Boolean {
        if (blurRadius == 0f) {
            release()
            return false
        }

        var downsampleFactor = downsampleFactor
        var radius = blurRadius / downsampleFactor

        if (radius > 25) {
            downsampleFactor = downsampleFactor * radius / 25
            radius = 25f
        }

        val width = width
        val height = height
        val scaledWidth = (width / downsampleFactor).toInt().coerceAtLeast(1)
        val scaledHeight = (height / downsampleFactor).toInt().coerceAtLeast(1)

        var dirty = dirty

        if (blurredBitmap?.width != scaledWidth || blurredBitmap?.height != scaledHeight) {
            dirty = true
            releaseBitmap()

            var prepareSuccess = false
            try {
                bitmapToBlur =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                        ?: return false

                blurringCanvas = Canvas(bitmapToBlur ?: return false)
                blurredBitmap =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                        ?: return false
                prepareSuccess = true
            } catch (e: OutOfMemoryError) { // Bitmap.createBitmap() 에서 OOM 발생 가능성 존재
            } finally {
                if (!prepareSuccess) {
                    release()
                    return false
                }
            }
        }
        if (dirty) {
            this.dirty = if (blurTool.prepare(context, bitmapToBlur, radius)) {
                false
            } else {
                return false
            }
        }
        return true
    }

    private fun blur(bitmapToBlur: Bitmap?, blurredBitmap: Bitmap?) {
        blurTool.blur(bitmapToBlur, blurredBitmap)
    }

    override fun draw(canvas: Canvas) {
        if (isRendering) throw StopException()

        canvas.save()
        applyCornerRadius(canvas)
        if (RENDERING_COUNT <= 0) { // BlurView 가 겹치는 경우 무시
            super.draw(canvas)
        }
        canvas.restore()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBlurredBitmap(canvas, blurredBitmap)
    }

    private fun drawBlurredBitmap(canvas: Canvas, blurredBitmap: Bitmap?) {
        blurredBitmap ?: return

        val rectSrc = Rect(0, 0, blurredBitmap.width, blurredBitmap.height)
        val rectDst = Rect(0, 0, width, height)

        canvas.drawBitmap(blurredBitmap, rectSrc, rectDst, null) // 블러 이미지 그리기
    }

    private fun applyCornerRadius(canvas: Canvas) {
        outlinePath?.apply {
            reset()

            val w = width.toFloat()
            val h = height.toFloat()

            moveTo(leftTopRadius, 0f)

            // 상단, 오른쪽 위 코너
            lineTo(w - rightTopRadius, 0f)
            quadTo(w, 0f, w, rightTopRadius)

            // 우측, 오른쪽 아래 코너
            lineTo(w, h - rightBottomRadius)
            quadTo(w, h, w - rightBottomRadius, h)

            // 하단, 왼쪽 아래 코너
            lineTo(leftBottomRadius, h)
            quadTo(0f, h, 0f, h - leftBottomRadius)

            // 좌측, 왼쪽 위 코너
            lineTo(0f, leftTopRadius)
            quadTo(0f, 0f, leftTopRadius, 0f)

            canvas.clipPath(this)
        }
    }

    override fun onDetachedFromWindow() {
        activityRootView?.viewTreeObserver?.removeOnPreDrawListener(preDrawListener)
        release()
        super.onDetachedFromWindow()
    }

    private fun release() {
        releaseBitmap()
        blurTool.release()
    }

    private fun releaseBitmap() {
        bitmapToBlur?.let {
            it.recycle()
            bitmapToBlur = null
        }
        blurredBitmap?.let {
            it.recycle()
            blurredBitmap = null
        }
    }

    private fun Float.addIfPositive(amount: Float) = if (this > 0f) this + amount else this

    private class StopException : RuntimeException()

    companion object {
        private const val DEFAULT_DOWNSAMPLE_FACTOR = 4f
        private const val DEFAULT_BLUR_RADIUS = 10f

        private var RENDERING_COUNT = 0
    }
}
