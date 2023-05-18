package com.mangbaam.blurview.blurtool

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RSRuntimeException
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class RenderScriptBlurTool : BlurTool {
    private var renderScript: RenderScript? = null
    private var blurScript: ScriptIntrinsicBlur? = null
    private var blurInput: Allocation? = null
    private var blurOutput: Allocation? = null

    override fun prepare(context: Context, buffer: Bitmap?, radius: Float): Boolean {
        if (renderScript == null) {
            try {
                renderScript = RenderScript.create(context)
                blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            } catch (e: RSRuntimeException) {
                release()
                return false
            }
        }
        blurScript?.setRadius(radius)
        blurInput = Allocation.createFromBitmap(
            renderScript,
            buffer,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT,
        )
        blurOutput = Allocation.createTyped(renderScript, blurInput?.type)
        return true
    }

    override fun release() {
        blurInput?.let {
            it.destroy()
            blurInput = null
        }
        blurOutput?.let {
            it.destroy()
            blurOutput = null
        }
        blurScript?.let {
            it.destroy()
            blurScript = null
        }
        renderScript?.let {
            it.destroy()
            renderScript = null
        }
    }

    override fun blur(input: Bitmap?, output: Bitmap?) {
        blurInput?.copyFrom(input)
        blurScript?.setInput(blurInput)
        blurScript?.forEach(blurOutput)
        blurOutput?.copyTo(output)
    }

    private fun blurrable(context: Context): Boolean {
        return try {
            val bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
            prepare(context, bmp, 4f)
            release()
            bmp.recycle()
            true
        } catch (_: Exception) {
            false
        }
    }

    companion object {
        fun getInstanceIfPossible(context: Context): RenderScriptBlurTool? {
            val instance = RenderScriptBlurTool()
            return if (instance.blurrable(context)) instance else null
        }
    }
}
