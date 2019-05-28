package github.com.st235.lib_showcase

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.graphics.plus
import github.com.st235.lib_showcase.utils.*
import github.com.st235.lib_showcase.utils.applyMargins
import github.com.st235.lib_showcase.utils.multiplyAsVector
import github.com.st235.lib_showcase.utils.toFloatPx
import github.com.st235.lib_showcase.utils.toPx


class SamuraiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class Type {
        CIRCLE, RECT
    }

    private val showcaseBounds = RectF()
    private val showcaseMargins = RectF()

    internal val viewFrame = RectF()
    private val showcaseFrame = RectF()

    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val overlayPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
        style = Paint.Style.FILL
    }

    var tooltip: ShowCaseTooltip? = null

    @ColorInt
    var overlayColor = Color.TRANSPARENT
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var frameColor = Color.TRANSPARENT
        set(value) {
            field = value
            invalidate()
        }

    @Dimension(unit = Dimension.DP)
    var frameThickness = 1F
        set(value) {
            field = value
            invalidate()
        }

    var rectRoundRadius = 0F
        set(value) {
            field = value
            invalidate()
        }

    init {
        isFocusable = true
        isClickable = true
        setWillNotDraw(false)
    }

    fun highlight(
        duration: Long, extraMargins: RectF =
            RectF(12.toFloatPx(), 12.toFloatPx(), 12.toFloatPx(), 12.toFloatPx())
    ) {
        val animator = ValueAnimator.ofFloat(0F, 2F, 0F)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = duration
        animator.addUpdateListener {
            val multipliedMargins =
                showcaseMargins.newWithMargins(extraMargins.multiplyAsVector(it.animatedValue as Float))
            applyShowcaseFrame(multipliedMargins)
            invalidate()
        }
        animator.start()
    }

    fun setShowcase(bounds: RectF) {
        if (bounds.width() <= 0 ||
            bounds.height() <= 0
        ) {
            throw IllegalStateException("bounds are invalid")
        }

        showcaseBounds.set(bounds)
        applyShowcaseFrame()
        recalculateTooltip()
        invalidate()
    }

    fun setShowcaseMargins(
        @Dimension(unit = Dimension.DP) left: Int,
        @Dimension(unit = Dimension.DP) top: Int,
        @Dimension(unit = Dimension.DP) right: Int,
        @Dimension(unit = Dimension.DP) bottom: Int
    ) {
        if (left < 0 ||
            top < 0 ||
            right < 0 ||
            bottom < 0
        ) {
            throw IllegalStateException("margins should not be smaller than 0")
        }

        showcaseMargins.set(left.toFloatPx(), top.toFloatPx(), right.toFloatPx(), bottom.toFloatPx())

        if (showcaseBounds.isEmpty) {
            return
        }

        applyShowcaseFrame()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewFrame.set(0F, 0F, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (viewFrame.isEmpty || showcaseBounds.isEmpty) {
            return
        }

        drawOverlay(canvas)
        drawFrame(canvas)
    }

    private fun drawOverlay(canvas: Canvas?) {
        if (overlayColor == Color.TRANSPARENT) {
            return
        }

        overlayPaint.color = overlayColor
        val path = Path()
        path.addRect(viewFrame, Path.Direction.CW)

        path.addRoundRect(showcaseFrame, rectRoundRadius.toPx(), rectRoundRadius.toPx(), Path.Direction.CCW)
        canvas?.drawPath(path, overlayPaint)
    }

    private fun drawFrame(canvas: Canvas?) {
        if (frameColor == Color.TRANSPARENT) {
            return
        }

        framePaint.color = frameColor
        framePaint.strokeWidth = frameThickness.toPx()

        val path = Path()
        path.addRoundRect(showcaseFrame, rectRoundRadius.toPx(), rectRoundRadius.toPx(), Path.Direction.CW)
        canvas?.drawPath(path, framePaint)
    }

    private fun applyShowcaseFrame(margins: RectF = showcaseMargins) {
        showcaseFrame.set(showcaseBounds)
        showcaseFrame.applyMargins(margins)
    }

    private fun recalculateTooltip() {
        if (tooltip == null) {
            return
        }

        val v = LayoutInflater.from(context).inflate(tooltip!!.layoutId, this, false)
        val p = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)

        val fitSpec = tooltip!!.tryToFit(v, viewFrame, showcaseFrame)

        when(fitSpec) {
            Fit.TOP -> {
                p.topMargin = (showcaseFrame.top - v.measuredHeight).toInt()
            }
            Fit.BOTTOM -> {
                p.topMargin = showcaseFrame.bottom.toInt()
            }
        }

        addView(v, p)
    }
}
