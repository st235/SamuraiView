package github.com.st235.lib_samurai

import android.graphics.RectF
import android.view.View
import github.com.st235.lib_samurai.geometry.Basis
import github.com.st235.lib_samurai.utils.Relation
import github.com.st235.lib_samurai.utils.calculateRelation
import github.com.st235.lib_samurai.utils.doOnLayout
import github.com.st235.lib_samurai.utils.transformGeometry

class Harakiri(private val into: SamuraiView) {

    private var highlightDuration = NO_ANIMATION

    private var collisionStrategy = CollisionStrategy.CRASH

    private val basis by lazy {
        Basis(into)
    }


    fun collisionStrategy(strategy: CollisionStrategy = CollisionStrategy.CRASH): Harakiri {
        this.collisionStrategy = strategy
        return this
    }

    fun highlightAnimationWithDuration(highlightDuration: Long = NO_ANIMATION): Harakiri {
        this.highlightDuration = highlightDuration
        return this
    }

    fun capture(vararg views: View) {
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