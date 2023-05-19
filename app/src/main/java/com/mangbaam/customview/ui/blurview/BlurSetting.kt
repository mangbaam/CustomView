package com.mangbaam.customview.ui.blurview

data class BlurSetting(
    val blurRadius: Float = 10f,
    val blurDownsampleFactor: Float = 5f,
    val cornerRadius: Float = 0f,
    val applyAllCornerRadius: Boolean = true,
    val leftTopRadius: Float = 0f,
    val rightTopRadius: Float = 0f,
    val leftBottomRadius: Float = 0f,
    val rightBottomRadius: Float = 0f,
)
