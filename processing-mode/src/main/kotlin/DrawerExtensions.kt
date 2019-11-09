package org.openrndr.extra.p5

import org.openrndr.draw.DrawPrimitive
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.draw.vertexBuffer
import org.openrndr.draw.vertexFormat
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.Rectangle
import org.openrndr.shape.contour

fun Drawer.point(x: Double, y: Double) {
    circle(x, y, 1.0)
}

enum class PositionMode {
    CORNER,
    CENTER
}

fun Drawer.rectangle(mode: PositionMode, x: Double, y: Double, width: Double, height: Double) {
    rectangle(mode, Vector2(x, y), width, height)
}

fun Drawer.rectangle(mode: PositionMode, v: Vector2, width: Double, height: Double) {
    when(mode) {
        PositionMode.CORNER -> rectangle(v, width, height)
        PositionMode.CENTER -> rectangle(Rectangle.fromCenter(v, width, height))
    }
}

fun Drawer.ellipse(x: Double, y: Double, width: Double, height: Double) {
    val k = .5522848
    val ox = (width / 2) * k
    val oy = (height / 2) * k
    val xe = x + width
    val ye = y + height
    val xm = x + width / 2
    val ym = y + height / 2
    contour(
        contour {
            moveTo(x, ym)
            curveTo(x, ym - oy, xm - ox, y, xm, y)
            curveTo(xm + ox, y, xe, ym - oy, xe, ym)
            curveTo(xe, ym + oy, xm + ox, ye, xm, ye)
            curveTo(xm - ox, ye, x, ym + oy, x, ym)
            close()
        })
}

fun Drawer.triangle(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double) {
    triangle(Vector3(x1, y1, 0.0), Vector3(x2, y2, 0.0), Vector3(x3, y3, 0.0))
}

fun Drawer.triangle(v1: Vector3, v2: Vector3, v3: Vector3) {
    val geometry = vertexBuffer(vertexFormat {
        position(3)
    }, 3)

    geometry.put {
        write(v1, v2, v3)
    }

    vertexBuffer(geometry, DrawPrimitive.TRIANGLES)
}

fun Drawer.triangle(positionMode: PositionMode, points: List<Vector3>) {
    var center = Vector3(0.0, 0.0, 0.0)
    points.forEach {
        center += it
    }
    center /= points.size.toDouble()

    val geometry = vertexBuffer(vertexFormat {
        position(3)
    }, 3)

    geometry.put {
        write(Vector3(points[0].x - center.x, points[0].y - center.y, 0.0),
              Vector3(points[1].x - center.x, points[1].y - center.y, 0.0),
              Vector3(points[2].x - center.x, points[2].y - center.y, 0.0))
    }

    vertexBuffer(geometry, DrawPrimitive.TRIANGLES)
}

fun Drawer.noFill() {
    fill = null
}

fun Drawer.noStroke() {
    stroke = null
}