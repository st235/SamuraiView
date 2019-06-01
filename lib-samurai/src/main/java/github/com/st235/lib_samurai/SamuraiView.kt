package github.com.st235.lib_samurai

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import github.com.st235.lib_samurai.utils.applyMargins
import github.com.st235.lib_samurai.utils.toFloatPx
import github.com.st235.lib_samurai.utils.toPx


class SamuraiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class Type {
        CIRCLE,
        RECT
    }

    private val showcaseBounds = RectF()
    internal val showcaseMargins = RectF()

    internal val viewFrame = RectF()
    private val showcaseFrame = RectF()

    private val framePath = Path()
    private val overlayPath = Path()

    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val overlayPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
        style = Paint.Style.FILL
    }

    @ColorInt
    var overlayColor = Color.TRANSPARENT
        set(value) {
            field = value
            invalidate()
        }

    @ColorRes
    var overlayColorRes = 0
        set(value) {
            field = value
            overlayColor = context.resources.getColor(value)
        }

    @ColorInt
    var frameColor = Color.TRANSPARENT
        set(value) {
            field = value
            invalidate()
        }

    @ColorRes
    var frameColorRes = 0
        set(value) {
            field = value
            frameColor = context.resources.getColor(value)
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

    var type: Type = Type.RECT
        set(value) {
            field = value
            invalidate()
        }

    var tooltip: SamuraiTooltip? = null
        set(value) {
            if (field != null) {
                removeView(field?.getView(context, this))
            }
            field = value
        }

    init {
        isFocusable = true
        isClickable = true
        setWillNotDraw(false)
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
        overlayPath.rewind()

        overlayPath.addRect(viewFrame, Path.Direction.CW)

        if (type == Type.CIRCLE) {
            overlayPath.addCircle(
                showcaseFrame.centerX(),
                showcaseFrame.centerY(),
                Math.max(showcaseFrame.width(), showcaseFrame.height()) / 2,
                Path.Direction.CCW
            )
        } else {
            overlayPath.addRoundRect(
                showcaseFrame,
                rectRoundRadius.toPx(),
                rectRoundRadius.toPx(),
                Path.Direction.CCW
            )
        }


        canvas?.drawPath(overlayPath, overlayPaint)
    }

    private fun drawFrame(canvas: Canvas?) {
        if (frameColor == Color.TRANSPARENT) {
            return
        }

        framePaint.color = frameColor
        framePaint.strokeWidth = frameThickness.toPx()
        framePath.rewind()

        if (type == Type.CIRCLE) {
            framePath.addCircle(
                showcaseFrame.centerX(),
                showcaseFrame.centerY(),
                Math.max(showcaseFrame.width(), showcaseFrame.height()) / 2,
                Path.Direction.CW
            )
        } else {
            framePath.addRoundRect(
                showcaseFrame,
                rectRoundRadius.toPx(),
                rectRoundRadius.toPx(),
                Path.Direction.CW
            )
        }

        canvas?.drawPath(framePath, framePaint)
    }

    internal fun applyShowcaseFrame(margins: RectF = showcaseMargins) {
        showcaseFrame.set(showcaseBounds)
        showcaseFrame.applyMargins(margins)
    }

    private fun recalculateTooltip() {
        val currentTooltip = tooltip ?: return
        removeView(currentTooltip.getView(context, this))

        val v = currentTooltip.getView(context, this)
        val p = currentTooltip.getLayoutParams()

        when (calculateFitModeForTooltip(v, viewFrame, showcaseFrame)) {
            Fit.TOP -> {
                p.topMargin = (showcaseFrame.top - v.measuredHeight).toInt()
            }
            Fit.BOTTOM -> {
                p.topMargin = showcaseFrame.bottom.toInt()
            }
            else ->
                throw IllegalStateException("There is no space for tooltip")
        }

        addView(v, p)
    }
}
