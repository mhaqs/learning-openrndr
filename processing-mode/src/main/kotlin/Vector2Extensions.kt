package org.openrndr.extra.p5

import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun rotate(vector: Vector2, theta: Double): Vector2 {
    return Vector2(vector.x * cos(theta) - vector.y * sin(theta), vector.x * sin(theta) + vector.y * cos(theta))
}

fun angleBetween(v1: Vector2, v2: Vector2): Double {
    // We get NaN if we pass in a zero vector which can cause problems
    // Zero seems like a reasonable angle between a (0,0,0) vector and something else
    if (v1.x == 0.0 && v1.y == 0.0) return 0.0
    if (v2.x == 0.0 && v2.y == 0.0) return 0.0
    val dot: Double = v1.x * v2.x + v1.y * v2.y
    val v1mag = sqrt(v1.x * v1.x + v1.y * v1.y)
    val v2mag = sqrt(v2.x * v2.x + v2.y * v2.y)

    // This should be a number between -1 and 1, since it's "normalized"
    val amt = dot / (v1mag * v2mag)

    if (amt <= -1) {
        return PI
    } else if (amt >= 1) {
        return 0.0
    }

    return acos(amt)
}

fun distance(v1: Vector2, v2: Vector2): Double {
    var xx1 = v1.x
    var yy1 = v1.y
    xx1 -= v2.x
    yy1 -= v2.y
    return sqrt(xx1 * xx1 + yy1 * yy1)
}