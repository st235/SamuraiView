package github.com.st235.lib_showcase.geometry

internal data class Vector2(val x: Float, val y: Float) {

    operator fun minus(another: Vector2): Vector2 {
        return Vector2(x - another.x, y - another.y)
    }
}
