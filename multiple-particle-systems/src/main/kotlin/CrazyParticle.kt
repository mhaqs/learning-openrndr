import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

// A subclass of Particle
internal class CrazyParticle(l: Vector2) : Particle(l) {
    // Just adding one new variable to a CrazyParticle
    // It inherits all other fields from "Particle", and we don't have to retype them!
    private var theta: Double = 0.0
    // Notice we don't have the method run() here; it is inherited from Particle

    // This update() method overrides the parent class update() method
    override fun update() {
        super.update()
        // Increment rotation based on horizontal velocity
        theta += velocity.x * velocity.squaredLength
    }

    // This display() method overrides the parent class display() method
    override fun display(drawer: Drawer) {
        // Render the ellipse just like in a regular particle
        super.display(drawer)

        // Then add a rotating line
        drawer.pushTransforms()
        drawer.translate(position.x, position.y)
        drawer.rotate(theta)
        drawer.stroke = ColorRGBa.WHITE.opacify(lifespan)
        drawer.lineSegment(0.0,0.0, 25.0, 0.0)
        drawer.popTransforms()
    }
}