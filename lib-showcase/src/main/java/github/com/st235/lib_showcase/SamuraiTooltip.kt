package github.com.st235.lib_showcase

import android.graphics.RectF
import android.view.View
import androidx.annotation.LayoutRes

internal enum class Fit {
    BOTTOM, TOP, NOWHERE
}

data class ShowCaseTooltip(@LayoutRes val layoutId: Int) {
    internal fun tryToFit(v: View, into: RectF, showcase: RectF): Fit {
        v.measure(
            View.MeasureSpec.makeMeasureSpec(into.width().toInt(), View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(into.height().toInt(), View.MeasureSpec.AT_MOST)
        )

        val height = v.measuredHeight

        val fromTop = showcase.top - into.top
        val fromBottom = into.bottom - showcase.bottom

        return when {
            height < fromTop -> Fit.TOP
            height < fromBottom -> Fit.BOTTOM
            else -> Fit.NOWHERE
        }
    }
}
