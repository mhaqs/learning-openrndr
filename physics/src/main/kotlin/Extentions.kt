import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.FontMap
import org.openrndr.math.Vector2
import org.openrndr.text.Writer
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.sqrt

/**
    Part of the Processing project - http://processing.org
    Copyright (c) 2012-16 The Processing Foundation
    Copyright (c) 2008-12 Ben Fry and Casey Reas
    Copyright (c) 2008 Dan Shiffman

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License version 2.1 as published by the Free Software Foundation.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General
    Public License along with this library; if not, write to the
    Free Software Foundation, Inc., 59 Temple Place, Suite 330,
    Boston, MA  02111-1307  USA
 */
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

fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    var xx1 = x1
    var yy1 = y1
    xx1 -= x2
    yy1 -= y2
    return sqrt(xx1 * xx1 + yy1 * yy1)
}

fun Writer.text(text:String, fontMap: FontMap?, x: Double = 0.0, y: Double= 0.0) {
    with(this) {
        drawStyle.fontMap = fontMap
        move(x, y)
        text(text)
        newLine()
    }
}

fun Writer.text(text: String, position: Vector2) {
    text(text, drawStyle.fontMap, position.x, position.y)
}

fun Writer.text(text: String, x: Int, y: Int) {
    text(text, drawStyle.fontMap, x.toDouble(), y.toDouble())
}

fun Writer.text(text: String, x: Double, y: Double) {
    text(text, drawStyle.fontMap, x, y)
}

fun Writer.text(text: String, fontUrl: String, textSize: Double, x: Double, y: Double) {
    text(text, FontImageMap.fromUrl(fontUrl, textSize), x, y)
}

fun Writer.text(text: String, fontUrl: String, textSize: Double, position: Vector2) {
    text(text, FontImageMap.fromUrl(fontUrl, textSize), position.x, position.y)
}

fun colorRGBaFrom256(r: Int, g: Int, b: Int) : ColorRGBa {
    return ColorRGBa(r / 255.0, g / 255.0, b / 255.0)
}