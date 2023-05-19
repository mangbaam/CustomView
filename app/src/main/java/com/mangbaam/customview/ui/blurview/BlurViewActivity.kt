package com.mangbaam.customview.ui.blurview

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mangbaam.customview.R
import com.mangbaam.customview.databinding.ActivityBlurViewBinding
import com.mangbaam.customview.extension.repeatOn

class BlurViewActivity : AppCompatActivity() {
    private val viewModel: BlurViewViewModel by viewModels()
    private lateinit var binding: ActivityBlurViewBinding
    private var popupMenu: PopupMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blur_view)
        binding.lifecycleOwner = this

        initViews()
        observe()
    }

    private fun initViews() = with(binding) {
        vm = viewModel

        root.setOnClickListener {
            if (popupMenu == null && viewModel.menu.value != SettingMenu.None) {
                viewModel.setMenu(SettingMenu.None)
            }
        }

        // Blur attr listeners
        blurView.setOnTouchListener(DraggableTouchListener())
        sliderRadius.addOnChangeListener { v, value, _ ->
            if (v.isShown) viewModel.setBlurRadius(value)
        }
        sliderDownsampleFactor.addOnChangeListener { v, value, _ ->
            if (v.isShown) viewModel.setDownsampleFactor(value)
        }

        // Corner radius listeners
        cbApplyAllCorner.setOnCheckedChangeListener { _, isChecked ->
            viewModel.applyAllCornerRadius(isChecked)
        }
        sliderAllRadius.addOnChangeListener { v, value, _ ->
            if (v.isShown) viewModel.setCornerRadius(value)
        }
        sliderLeftTopRadius.addOnChangeListener { v, value, _ ->
            if (v.isShown) {
                viewModel.setCornerRadius(leftTop = value)
            }
        }
        sliderRightTopRadius.addOnChangeListener { v, value, _ ->
            if (v.isShown) {
                viewModel.setCornerRadius(rightTop = value)
            }
        }
        sliderRightBottomRadius.addOnChangeListener { v, value, _ ->
            if (v.isShown) {
                viewModel.setCornerRadius(rightBottom = value)
            }
        }
        sliderLeftBottomRadius.addOnChangeListener { v, value, _ ->
            if (v.isShown) {
                viewModel.setCornerRadius(leftBottom = value)
            }
        }

        fabController.setOnClickListener {
            if (popupMenu == null) {
                showSettingMenu(binding.fabController, R.menu.menu_blur_setting)
            } else {
                hideSettingMenu()
            }
        }
    }

    private fun observe() {
        repeatOn {
            viewModel.blurSetting.collect {
                if (it.applyAllCornerRadius) {
                    binding.blurView.setCornerRadius(it.cornerRadius)
                } else {
                    binding.blurView.setCornerRadius(
                        leftTop = it.leftTopRadius,
                        rightTop = it.rightTopRadius,
                        rightBottom = it.rightBottomRadius,
                        leftBottom = it.leftBottomRadius,
                    )
                }
                binding.blurView.setBlurRadius(it.blurRadius)
                binding.blurView.setDownsampleFactor(it.blurDownsampleFactor)
            }
        }
    }

    private fun showSettingMenu(v: View, @MenuRes menuRes: Int) {
        popupMenu = PopupMenu(this, v).apply {
            menuInflater.inflate(menuRes, menu)
            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.blur_strength -> viewModel.setMenu(SettingMenu.Blur)
                    R.id.blur_corner_radius -> viewModel.setMenu(SettingMenu.CornerRadius)
                    else -> viewModel.setMenu(SettingMenu.None)
                }
                hideSettingMenu()
                true
            }
            setOnDismissListener {
                hideSettingMenu()
            }
            show()
        }
    }

    private fun hideSettingMenu() {
        popupMenu?.apply {
            dismiss()
            setOnMenuItemClickListener(null)
            setOnDismissListener(null)
            popupMenu = null
        }
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}
