import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DrawPrimitive
import org.openrndr.draw.Drawer
import org.openrndr.draw.RenderTarget
import org.openrndr.draw.VertexBuffer
import org.openrndr.draw.colorBuffer
import org.openrndr.draw.depthBuffer
import org.openrndr.draw.isolated
import org.openrndr.draw.renderTarget
import org.openrndr.draw.vertexBuffer
import org.openrndr.draw.vertexFormat
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform
import org.openrndr.shape.shape
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

val geometry: VertexBuffer = vertexBuffer(
        vertexFormat {
            position(3)
        }, 3)

internal class Boid(x: Double, y: Double) {
    private var position: Vector2
    private var velocity: Vector2
    private var acceleration: Vector2 = Vector2(0.0, 0.0)
    private var r: Double
    private var maxforce: Double // Maximum Steering Force
    private var maxspeed: Double // Maximum speed

    fun run(drawer: Drawer, boids: ArrayList<Boid>) {
        flock(boids)
        update()
        borders(drawer)
        render(drawer)
    }

    fun applyForce(force: Vector2) {
        // We could add mass here if we want A = F / M
        acceleration += force
    }

    // We accumulate a new acceleration each time based on three rules
    fun flock(boids: ArrayList<Boid>) {
        var sep: Vector2 = separate(boids)   // Separation
        var ali: Vector2 = align(boids)      // Alignment
        var coh: Vector2 = cohesion(boids)   // Cohesion

        // Arbitrarily weight these forces
        sep *= 1.5
        ali *= 1.0
        coh *= 1.0

        // Add the force vectors to acceleration
        applyForce(sep)
        applyForce(ali)
        applyForce(coh)
    }

    // Method to update position
    fun update() {
        // Update velocity
        velocity += acceleration

        // Limit speed
        velocity = limit(velocity, maxspeed)
        position += velocity

        // Reset acceleration to 0 each cycle
        acceleration *= 0.0
    }

    // A method that calculates and applies a steering force towards a target
    // STEER = DESIRED MINUS VELOCITY
    fun seek(target: Vector2): Vector2 {
        var desired: Vector2 = target.minus(position)  // A vector pointing from the position to the target
        // Scale to maximum speed
        desired = desired.normalized
        desired *= maxspeed

        // Above two lines of code below could be condensed with new Vector2 setMag() method
        // Not using this method until Processing.js catches up
        // desired.setMag(maxspeed)

        // Steering = Desired minus Velocity
        var steer: Vector2 = desired.minus(velocity)
        steer = limit(steer, maxforce)  // Limit to maximum steering force

        return steer
    }

    fun render(drawer: Drawer) {
        // Draw a triangle rotated in the direction of velocity
        val theta = heading2D(velocity)

        drawer.fill = ColorRGBa.GRAY
        drawer.stroke = ColorRGBa.WHITE
        drawer.isolated {
            translate(position.x, position.y)
            rotate(theta)                 //todo: fix boid rotation
            geometry.put {
                val v1 = Vector3(0.0, -r * 2, 0.0)
                val v2 = Vector3(-r, r * 2, 0.0)
                val v3 = Vector3(r, r * 2, 0.0)
                write(v1, v2, v3)
            }
            drawer.vertexBuffer(geometry, DrawPrimitive.TRIANGLES)
        }
    }

    // Wraparound
    fun borders(drawer: Drawer) {
        if (position.x < -r) position = Vector2(drawer.width + r, position.y)
        if (position.y < -r) position = Vector2(position.x, drawer.height + r)
        if (position.x > drawer.width + r) position = Vector2(-r, position.y)
        if (position.y > drawer.height + r) position = Vector2(position.x, -r)
    }

    // Separation
    // Method checks for nearby boids and steers away
    fun separate(boids: ArrayList<Boid>): Vector2 {
        val desiredseparation = 25.0
        var steer = Vector2(0.0, 0.0)
        var count = 0.0
        // For every boid in the system, check if it's too close

        for (other in boids) {
            val d: Double = distance(position, other.position)
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)

            if (d > 0 && d < desiredseparation) {
                // Calculate vector pointing away from neighbor

                var diff: Vector2 = position.minus(other.position)
                diff = diff.normalized
                diff /= d        // Weight by distance

                steer += (diff)
                count++            // Keep track of how many
            }
        }
        // Average -- divide by how many


        if (count > 0) {
            steer /= count
        }

        // As long as the vector is greater than 0
        if (steer.length > 0) {
            // First two lines of code below could be condensed with new Vector2 setMag() method
            // Not using this method until Processing.js catches up
            // steer.setMag(maxspeed)

            // Implement Reynolds: Steering = Desired - Velocity

            steer = steer.normalized
            steer *= maxspeed
            steer -= velocity
            steer = limit(steer, maxforce)
        }
        return steer
    }

    // Alignment
    // For every nearby boid in the system, calculate the average velocity
    fun align(boids: ArrayList<Boid>): Vector2 {
        val neighbordist = 50.0
        var sum = Vector2(0.0, 0.0)
        var count = 0.0
        for (other in boids) {
            val d = distance(position, other.position)
            if (d > 0 && d < neighbordist) {
                sum += other.velocity
                count++
            }
        }
        return if (count > 0) {
            sum /= count
            // First two lines of code below could be condensed with new Vector2 setMag() method
            // Not using this method until Processing.js catches up
            // sum.setMag(maxspeed)

            // Implement Reynolds: Steering = Desired - Velocity


            sum = sum.normalized
            sum *= maxspeed
            var steer: Vector2 = sum.minus(velocity)
            steer = limit(steer, maxforce)
            steer
        } else {
            Vector2(0.0, 0.0)
        }
    }

    // Cohesion
    // For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
    fun cohesion(boids: ArrayList<Boid>): Vector2 {
        val neighbordist = 50.0
        var sum = Vector2(0.0, 0.0)   // Start with empty vector to accumulate all positions

        var count = 0.0
        for (other in boids) {
            val d = distance(position, other.position)
            if (d > 0 && d < neighbordist) {
                sum += other.position // Add position
                count++
            }
        }
        return if (count > 0) {
            sum /= count
            seek(sum)  // Steer towards the position
        } else {
            Vector2(0.0, 0.0)
        }
    }

    private fun distance(vec1: Vector2, vec2:Vector2): Double {
        val v = Vector2(vec1.x - vec2.x, vec1.y - vec2.y)
        return v.length
    }

    private fun heading2D(vec: Vector2): Double {
        return atan2(vec.y, vec.x)
    }

    fun limit(vector: Vector2, max: Double): Vector2 {
        var vec = vector
        if (vec.squaredLength > max * max) {
            vec = vec.normalized
            vec *= max
        }
        return vec
    }

    init {
        val angle = Random.nextDouble(2 * PI)
        velocity = Vector2(cos(angle), sin(angle))
        position = Vector2(x, y)
        r = 2.0
        maxspeed = 2.0
        maxforce = 0.03
    }
}