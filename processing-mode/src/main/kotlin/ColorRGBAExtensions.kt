package org.openrndr.extra.p5

import org.openrndr.color.ColorRGBa

fun from256(r: Int, g: Int, b: Int) : ColorRGBa {
    return ColorRGBa(r / 255.0, g / 255.0, b / 255.0)
}