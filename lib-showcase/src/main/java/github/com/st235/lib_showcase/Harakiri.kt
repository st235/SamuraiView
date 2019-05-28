package github.com.st235.lib_showcase

import android.graphics.RectF
import android.view.View
import github.com.st235.lib_showcase.geometry.Basis
import github.com.st235.lib_showcase.utils.Relation
import github.com.st235.lib_showcase.utils.calculateRelation
import github.com.st235.lib_showcase.utils.doOnLayout
import github.com.st235.lib_showcase.utils.transformGeometry

class Harakiri(private val origin: SamuraiView) {

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

    private var highlightDuration = 0L

    private var collisionStrategy = CollisionStrategy.CRASH

    private val basis by lazy {
        Basis(origin)
    }


    fun collisionStrategy(strategy: CollisionStrategy = CollisionStrategy.CRASH): Harakiri {
        this.collisionStrategy = strategy
        return this
    }

    fun highlightDuration(highlightDuration: Long = 0L): Harakiri {
        this.highlightDuration = highlightDuration
        return this
    }

    fun capture(vararg views: View) {
        origin.doOnLayout {
            val resultRect = RectF(Float.MAX_VALUE, Float.MAX_VALUE, 0F, 0F)

            for (view in views) {
                val position = basis.moveViewInto(view)
                val bounds = view.transformGeometry(position)

                var isSkip = false
                if (origin.viewFrame.calculateRelation(bounds) != Relation.CONTAINS) {
                    isSkip = collisionStrategy.shouldSkipOnCollision(view)
                }

                if (!isSkip) {
                    if (bounds.left < resultRect.left) resultRect.left = bounds.left
                    if (bounds.right > resultRect.right) resultRect.right = bounds.right
                    if (bounds.top < resultRect.top) resultRect.top = bounds.top
                    if (bounds.bottom > resultRect.bottom) resultRect.bottom = bounds.bottom
                }

                origin.setShowcase(resultRect)
                if (highlightDuration > 0L) {
                    origin.highlight(highlightDuration)
                }
            }
        }
    }
}