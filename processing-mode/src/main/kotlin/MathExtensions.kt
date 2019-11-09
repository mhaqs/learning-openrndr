package org.openrndr.extra.p5

import kotlin.math.sqrt

fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    var xx1 = x1
    var yy1 = y1
    xx1 -= x2
    yy1 -= y2
    return sqrt(xx1 * xx1 + yy1 * yy1)
}

const val QUARTER_PI = 0.7853982
const val TWO_PI = 6.2831855
const val HALF_PI = 1.5707964
const val TAU = TWO_PI