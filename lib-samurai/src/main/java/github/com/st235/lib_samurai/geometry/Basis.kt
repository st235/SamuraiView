package github.com.st235.lib_samurai.geometry

import android.view.View

internal class Basis(originView: View) {

    private val origin = originView.toWindowBasis()

    fun moveViewInto(view: View): Vector2 = view.toWindowBasis() - origin

    private fun View.toWindowBasis(): Vector2 {
        val onScreenCoordinates = IntArray(2)
        getLocationInWindow(onScreenCoordinates)
        return Vector2(
            onScreenCoordinates[0].toFloat(),
            onScreenCoordinates[1].toFloat()
        )
    }
}
