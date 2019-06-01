package github.com.st235.lib_samurai.utils

import android.animation.ValueAnimator
import android.graphics.RectF
import android.view.animation.AccelerateDecelerateInterpolator
import github.com.st235.lib_samurai.SamuraiView

internal fun SamuraiView.highlight(
        duration: Long, extraMargins: RectF =
                RectF(12.toFloatPx(), 12.toFloatPx(), 12.toFloatPx(), 12.toFloatPx())
) {
    val animator = ValueAnimator.ofFloat(0F, 0.5F, 0F)
    animator.repeatCount = ValueAnimator.INFINITE
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.duration = duration
    animator.addUpdateListener {
        val multipliedMargins =
                showcaseMargins.offsetFor(extraMargins.multiply(it.animatedValue as Float))
        applyShowcaseFrame(multipliedMargins)
        invalidate()
    }
    animator.start()
}
