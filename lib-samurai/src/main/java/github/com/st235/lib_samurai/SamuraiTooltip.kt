package github.com.st235.lib_samurai

import android.content.Context
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

internal enum class Fit {
    BOTTOM, TOP, NOWHERE
}

abstract class SamuraiTooltip(
        private val width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        private val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) {
    abstract fun getView(context: Context, parent: ViewGroup): View

    fun getLayoutParams(): ViewGroup.MarginLayoutParams =
            ViewGroup.MarginLayoutParams(width, height)

    companion object {
        fun createForLayout(
                @LayoutRes layoutId: Int,
                width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): SamuraiTooltip = ResourceTooltip(layoutId, width, height)

        fun createForView(
                view: View,
                width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): SamuraiTooltip = ViewTooltip(view, width, height)
    }
}

internal class ViewTooltip(val view: View, width: Int, height: Int): SamuraiTooltip(width, height) {
    override fun getView(context: Context, parent: ViewGroup): View = view
}

internal class ResourceTooltip(@LayoutRes val layoutId: Int, width: Int, height: Int): SamuraiTooltip(width, height) {
    private var v: View? = null

    override fun getView(context: Context, parent: ViewGroup): View {
        if (v == null) {
            v = LayoutInflater.from(context).inflate(layoutId, parent, false)
        }
        return v!!
    }
}

internal fun calculateFitModeForTooltip(v: View, into: RectF, showcase: RectF): Fit {
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
