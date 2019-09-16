import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

/**
 * Forces (Gravity and Fluid Resistence) with Vectors
 * by Daniel Shiffman.
 *
 * Demonstration of multiple force acting on bodies (Mover class)
 * Bodies experience gravity continuously
 * Bodies experience fluid resistance when in "water"
 */
// Liquid class 
internal class Liquid(
// Liquid is a rectangle
        var x: Double,
        var y: Double,
        var w: Double,
        var h: Double, // Coefficient of drag
        var c: Double
) {

    // Is the Mover in the Liquid?
    operator fun contains(m: Mover): Boolean {
        val l: Vector2 = m.position
        return l.x > x && l.x < x + w && l.y > y && l.y < y + h
    }

    // Calculate drag force
    fun drag(m: Mover): Vector2 {
        // Magnitude is coefficient * speed squared

        val speed: Double = m.velocity.length
        val dragMagnitude = c * speed * speed

        // Direction is inverse of velocity
        var drag: Vector2 = m.velocity.copy()
        drag *= -1.0

        // Scale according to magnitude
        drag = drag.normalized
        drag *= dragMagnitude
        return drag
    }

    fun display(drawer: Drawer) {
        drawer.stroke = null
        drawer.fill = ColorRGBa.GRAY
        drawer.rectangle(x, y, w, h)
    }
}