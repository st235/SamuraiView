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

interface SamuraiTooltip {
    fun getView(context: Context, parent: ViewGroup): View

    companion object {
        fun createForLayout(@LayoutRes layoutId: Int): SamuraiTooltip = ResourceTooltip(layoutId)

        fun createForView(view: View): SamuraiTooltip = ViewTooltip(view)
    }
}

internal class ViewTooltip(val view: View): SamuraiTooltip {
    override fun getView(context: Context, parent: ViewGroup): View = view
}

internal class ResourceTooltip(@LayoutRes val layoutId: Int): SamuraiTooltip {
    override fun getView(context: Context, parent: ViewGroup): View =
        LayoutInflater.from(context).inflate(layoutId, parent, false)
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
