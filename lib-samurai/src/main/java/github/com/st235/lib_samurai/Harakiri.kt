package github.com.st235.lib_samurai

import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import github.com.st235.lib_samurai.geometry.Basis
import github.com.st235.lib_samurai.utils.*

class Harakiri(private val into: SamuraiView) {

    private var highlightDuration = NO_ANIMATION

    private var collisionStrategy = CollisionStrategy.CRASH

    private var tooltip: SamuraiTooltip? = null

    private var type: SamuraiView.Type = SamuraiView.Type.RECT

    @ColorInt
    private var overlayColor: Int = Color.TRANSPARENT

    @ColorInt
    private var frameColor: Int = Color.TRANSPARENT

    @Dimension(unit = Dimension.DP)
    private var thickness: Float = 0F

    @Dimension(unit = Dimension.DP)
    private var roundRadius: Float = 0F

    private val margins = Rect()

    private val basis by lazy {
        Basis(into)
    }

    fun overlayColor(@ColorInt color: Int): Harakiri {
        this.overlayColor = color
        return this
    }

    fun overlayColorRes(@ColorRes color: Int): Harakiri {
        this.overlayColor = ContextCompat.getColor(into.context, color)
        return this
    }

    fun frameColor(@ColorInt color: Int): Harakiri {
        this.frameColor = color
        return this
    }

    fun frameColorRes(@ColorRes color: Int): Harakiri {
        this.frameColor = ContextCompat.getColor(into.context, color)
        return this
    }

    fun frameThickness(@Dimension(unit = Dimension.DP) thickness: Float): Harakiri {
        this.thickness = thickness
        return this
    }

    fun rect(@Dimension(unit = Dimension.DP) roundRadius: Float = 0F): Harakiri {
        type = SamuraiView.Type.RECT
        this.roundRadius = roundRadius
        return this
    }

    fun circle(): Harakiri {
        type = SamuraiView.Type.CIRCLE
        return this
    }

    fun withTooltip(
            @LayoutRes layoutId: Int,
            width: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            height: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ): Harakiri {
        this.tooltip = SamuraiTooltip.createForLayout(layoutId, width, height)
        return this
    }

    fun withTooltip(
            view: View,
            width: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            height: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT
    ): Harakiri {
        this.tooltip = SamuraiTooltip.createForView(view, width, height)
        return this
    }

    fun collisionStrategy(strategy: CollisionStrategy = CollisionStrategy.CRASH): Harakiri {
        this.collisionStrategy = strategy
        return this
    }

    fun highlightAnimationWithDuration(highlightDuration: Long = NO_ANIMATION): Harakiri {
        this.highlightDuration = highlightDuration
        return this
    }

    fun margins(
            @Dimension(unit = Dimension.DP) left: Int,
            @Dimension(unit = Dimension.DP) top: Int,
            @Dimension(unit = Dimension.DP) right: Int,
            @Dimension(unit = Dimension.DP) bottom: Int
    ): Harakiri {
        margins.set(left, top, right, bottom)
        return this
    }

    fun capture(vararg views: View) {
        initOrigin()

        into.doOnLayout {
            val resultRect = RectF(Float.MAX_VALUE, Float.MAX_VALUE, 0F, 0F)

            for (view in views) {
                val position = basis.moveViewInto(view)
                val bounds = view.transformGeometry(position)

                var isSkip = false
                if (into.viewFrame.calculateRelation(bounds) != Relation.CONTAINS) {
                    isSkip = collisionStrategy.shouldSkipOnCollision(view)
                }

                if (!isSkip) {
                    if (bounds.left < resultRect.left) resultRect.left = bounds.left
                    if (bounds.right > resultRect.right) resultRect.right = bounds.right
                    if (bounds.top < resultRect.top) resultRect.top = bounds.top
                    if (bounds.bottom > resultRect.bottom) resultRect.bottom = bounds.bottom
                }

                into.setShowcase(resultRect)
                if (highlightDuration > 0L) {
                    into.highlight(highlightDuration)
                }
            }
        }
    }

    private fun initOrigin() {
        if (into.visibility != View.VISIBLE) {
            into.visibility = View.VISIBLE
        }
        into.overlayColor = overlayColor
        into.frameColor = frameColor
        into.frameThickness = thickness
        into.tooltip = tooltip
        into.type = type
        into.rectRoundRadius = roundRadius
        into.setShowcaseMargins(margins.left, margins.top, margins.right, margins.bottom)
    }

    enum class CollisionStrategy {
        CRASH {
            override fun shouldSkipOnCollision(view: View): Boolean {
                throw IllegalStateException("$view has intersect or out of the overlay bound")
            }
        },
        SKIP {
            override fun shouldSkipOnCollision(view: View): Boolean = true
        },
        FORCE_ADD {
            override fun shouldSkipOnCollision(view: View): Boolean = false
        };

        abstract fun shouldSkipOnCollision(view: View): Boolean
    }

    companion object {
        const val NO_ANIMATION = 0L
    }
}