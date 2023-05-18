package com.mangbaam.blurview.blurtool

import android.content.Context
import android.graphics.Bitmap

interface BlurTool {
    /**
     * ## 블러 처리 전처리
     * @param buffer 블러 처리할 Bitmap
     * @param radius 블러 radius (블러 수준)
     * @return 블러가 가능하면 true, 불가능하면 false
     * */
    fun prepare(context: Context, buffer: Bitmap?, radius: Float): Boolean

    /**
     * ## 블러 처리
     * */
    fun blur(input: Bitmap?, output: Bitmap?)

    /**
     * ## 블러 처리 후처리
     * */
    fun release()
}
