import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.random.Random

internal open class Particle(l: Vector2) {
    protected var position: Vector2 = Vector2(l.x, l.y)
    protected var velocity: Vector2 = Vector2(Random.nextDouble(-1.0,1.0), Random.nextDouble(-2.0, 0.0))
    private var acceleration: Vector2 = Vector2(0.0, 0.05)
    protected var lifespan: Double = 1.0

    fun run(drawer: Drawer) {
        update()
        display(drawer)
    }

    // Method to update position
    protected open fun update() {
        velocity += acceleration
        position += velocity
        lifespan -= 0.01
    }

    // Method to display
    protected open fun display(drawer:Drawer) {
        drawer.stroke = ColorRGBa.WHITE.opacify(lifespan)
        drawer.fill = ColorRGBa.WHITE.opacify(lifespan)
        drawer.circle(position.x, position.y, 8.0)
    }

    // Is the particle still useful?
    val isDead: Boolean
        get() = lifespan < 0.0
}