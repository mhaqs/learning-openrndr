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
internal class Mover(// Mass is tied to size
        internal var mass: Double,
        x: Double,
        y: Double
) {
    // position, velocity, and acceleration 
    var position: Vector2 = Vector2(x, y)
    var velocity: Vector2 = Vector2(0.0, 0.0)
    private var acceleration: Vector2 = Vector2(0.0, 0.0)

    // Newton's 2nd law: F = M * A
    // or A = F / M
    fun applyForce(force: Vector2) {
        // Divide by mass
        val f: Vector2 = force.div(mass)

        // Accumulate all forces in acceleration
        acceleration += f
    }

    fun update() {
        // Velocity changes according to acceleration
        velocity += acceleration

        // position changes by velocity
        position += velocity

        // We must clear acceleration each frame
        acceleration *= 0.0
    }

    // Draw Mover
    fun display(drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 2.0
        drawer.fill = ColorRGBa.GRAY
        drawer.circle(position.x, position.y, mass * 16)
    }

    // Bounce off bottom of window
    fun checkEdges(drawer: Drawer) {
        if (position.y > drawer.height) {
            velocity = Vector2(velocity.x, velocity.y *-0.9)
            position = Vector2(position.x, drawer.height.toDouble())
        }
    }
}