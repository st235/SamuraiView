package github.com.st235.lib_showcase.utils

import android.graphics.RectF

internal enum class Relation {
    INDEPENDENT, CONTAINS, INTERSECT
}

internal fun RectF.newWithMargins(another: RectF) =
    RectF(left + another.left, right + another.right, top + another.top, bottom + another.bottom)

internal fun RectF.multiplyAsVector(times: Float) =
    RectF(left * times, top * times, right * times, bottom * times)

internal fun RectF.applyMargins(margins: RectF) {
    left -= margins.left
    right += margins.right
    top -= margins.top
    bottom += margins.bottom
}

internal fun RectF.calculateRelation(another: RectF): Relation {
    if (this.contains(another)) {
        return Relation.CONTAINS
    }

    if (this.left < another.right && another.left < this.right
        && this.top < another.bottom && another.top < this.bottom) {
        return Relation.INTERSECT
    }

    return Relation.INDEPENDENT
}
