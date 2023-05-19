package com.mangbaam.customview.ui.blurview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BlurViewViewModel : ViewModel() {
    private val _blurSetting = MutableStateFlow(BlurSetting())
    val blurSetting = _blurSetting.asStateFlow()

    private val _menu = MutableStateFlow(SettingMenu.None)
    val menu = _menu.asStateFlow()

    fun setBlurRadius(radius: Float) {
        _blurSetting.value = blurSetting.value.copy(
            blurRadius = radius.coerceIn(0f..25f),
        )
    }

    fun setDownsampleFactor(factor: Float) {
        _blurSetting.value = blurSetting.value.copy(
            blurDownsampleFactor = factor.coerceAtLeast(0.1f),
        )
    }

    fun setMenu(menu: SettingMenu) {
        _menu.value = menu
    }

    fun applyAllCornerRadius(applyAll: Boolean) {
        _blurSetting.value = blurSetting.value.copy(
            applyAllCornerRadius = applyAll,
        )
    }

    fun setCornerRadius(radius: Float) {
        _blurSetting.value = blurSetting.value.copy(
            applyAllCornerRadius = true,
            cornerRadius = radius.coerceAtLeast(0f),
        )
    }

    fun setCornerRadius(
        leftTop: Float? = null,
        rightTop: Float? = null,
        rightBottom: Float? = null,
        leftBottom: Float? = null,
    ) {
        leftTop?.let {
            _blurSetting.value = blurSetting.value.copy(
                leftTopRadius = it,
            )
        }
        rightTop?.let {
            _blurSetting.value = blurSetting.value.copy(
                rightTopRadius = it,
            )
        }
        rightBottom?.let {
            _blurSetting.value = blurSetting.value.copy(
                rightBottomRadius = it,
            )
        }
        leftBottom?.let {
            _blurSetting.value = blurSetting.value.copy(
                leftBottomRadius = it,
            )
        }
    }
}
