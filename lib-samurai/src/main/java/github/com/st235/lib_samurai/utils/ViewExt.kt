package github.com.st235.lib_samurai.utils

import android.graphics.RectF
import android.view.View
import android.view.ViewTreeObserver
import github.com.st235.lib_samurai.geometry.Vector2

internal fun View.transformGeometry(origin: Vector2) =
    RectF(origin.x, origin.y, origin.x + width, origin.y + height)

internal fun View.doOnLayout(onLayout: () -> Unit) {
    if (width != 0 || height != 0) {
        onLayout()
        return
    }

    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            onLayout()
        }
    })
}