package org.jellyfin.androidtv.ui

import android.content.Context
import android.graphics.Color
import android.widget.PopupWindow
import android.view.Gravity
import android.view.LayoutInflater
import android.graphics.drawable.ColorDrawable
import android.view.View
import org.jellyfin.androidtv.databinding.PlaybackSpeedPopupBinding
import org.jellyfin.androidtv.util.Utils

class PlaybackSpeedPopup(
	context: Context,
	private val anchor: View,
	changeListener: ((value: Float) -> Unit)
) {
    val popupWindow: PopupWindow?
	private val binding: PlaybackSpeedPopupBinding = PlaybackSpeedPopupBinding.inflate(LayoutInflater.from(context))

    val isShowing: Boolean
        get() = popupWindow != null && popupWindow.isShowing

    fun show(value: Float) {
		binding.floatSpinner.value = value
        popupWindow!!.showAsDropDown(anchor, 0, 0, Gravity.END)
    }

    fun dismiss() {
        if (popupWindow != null && popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }

    init {
		binding.floatSpinner.setOnChangeListener(changeListener)
        binding.floatSpinner.minValue = .1f
        binding.floatSpinner.maxValue = 4f
        val width = Utils.convertDpToPixel(context, 240)
        val height = Utils.convertDpToPixel(context, 130)
        popupWindow = PopupWindow(binding.root, width, height)
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // necessary for popup to dismiss
    }
}
