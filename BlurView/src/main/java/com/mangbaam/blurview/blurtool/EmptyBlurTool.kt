package com.mangbaam.blurview.blurtool

import android.content.Context
import android.graphics.Bitmap

class EmptyBlurTool : BlurTool {
    override fun prepare(context: Context, buffer: Bitmap?, radius: Float) = false
    override fun release() {}
    override fun blur(input: Bitmap?, output: Bitmap?) {}
}
