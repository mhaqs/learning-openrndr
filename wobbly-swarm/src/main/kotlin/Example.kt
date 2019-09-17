import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.math.Vector2
import kotlin.math.sqrt
import kotlin.random.Random

class Example : Program() {
    var mass = ArrayList<Double>()
    var position = ArrayList<Vector2>()
    var velocity = ArrayList<Vector2>()

    override fun setup() {
        drawer.stroke = null
        drawer.fill = ColorRGBa(64 / 255.0, 1.0, 1.0, 192 / 255.0)
        mouse.buttonDown.listen {
            addNewParticle()
        }
        mouse.dragged.listen {
            addNewParticle()
        }
    }

    fun addNewParticle() {
        mass.add(Random.nextDouble(0.003, 0.03))
        position.add(Vector2(mouse.position.x, mouse.position.y))
        velocity.add(Vector2.ZERO)
    }

    override fun draw() {
        drawer.background(ColorRGBa(32.0 / 255.0, 32.0 / 255.0, 32.0 / 255.0))
        for (particleA in 0 until mass.size) {
            var accelerationX = 0.0
            var accelerationY = 0.0
            for (particleB in 0 until mass.size) {
                if (particleA != particleB) {
                    val distanceX = position[particleB].x - position[particleA].x
                    val distanceY = position[particleB].y - position[particleA].y

                    var distance = sqrt(distanceX * distanceX + distanceY * distanceY)
                    if (distance < 1) distance = 1.0

                    val force = (distance - 320) * mass[particleB] / distance
                    accelerationX += force * distanceX
                    accelerationY += force * distanceY
                }
            }

            velocity[particleA] = Vector2(velocity[particleA].x * 0.99 + accelerationX * mass[particleA], velocity[particleA].y * 0.99 + accelerationY * mass[particleA])
        }

        for (particle in 0 until mass.size) {
            position[particle] += velocity[particle]
        }
        drawer.stroke = null
        drawer.fill = ColorRGBa(0.64, 1.0, 1.0, 0.92)
        drawer.circles(position, mass.map{ it.times(1000)})
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 600
        height = 600
        title = "Wobbly Swarm"
    })
}