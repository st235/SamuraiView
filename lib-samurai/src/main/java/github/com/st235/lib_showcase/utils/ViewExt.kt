package github.com.st235.lib_showcase.utils

import android.graphics.RectF
import android.view.View
import android.view.ViewTreeObserver
import github.com.st235.lib_showcase.geometry.Vector2

internal fun View.transformGeometry(origin: Vector2) =
    RectF(origin.x, origin.y, origin.x + width, origin.y + height)

internal fun View.doOnLayout(onLayout: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            onLayout()
        }
    })
}
