import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import kotlin.math.sqrt
import kotlin.random.Random

class Example : Program() {
    var mass = ArrayList<Double>()
    var positionX = ArrayList<Double>()
    var positionY = ArrayList<Double>()
    var velocityX = ArrayList<Double>()
    var velocityY = ArrayList<Double>()

    override fun setup() {
        drawer.stroke = null
        drawer.fill = ColorRGBa(64.0, 255.0, 255.0, 192 * 1.0)
        mouse.buttonDown.listen {
            addNewParticle()
        }
        mouse.dragged.listen {
            addNewParticle()
        }
    }

    fun addNewParticle() {
        mass.add(Random.nextDouble(0.003, 0.03))
        positionX.add(mouse.position.y)
        positionY.add(mouse.position.y)
        velocityX.add(0.0)
        velocityY.add(0.0)
    }

    override fun draw() {
        //drawer.background(ColorRGBa(32.0, 32.0, 32.0))

        for (particleA in 0 until mass.size) {
            var accelerationX = 0.0
            var accelerationY = 0.0
            for (particleB in 0 until mass.size) {
                if (particleA != particleB) {
                    val distanceX = positionX[particleB] - positionX[particleA]
                    val distanceY = positionY[particleB] - positionY[particleA]

                    var distance = sqrt(distanceX * distanceX + distanceY * distanceY)
                    if (distance < 1) distance = 1.0

                    val force = (distance - 320) * mass[particleB] / distance
                    accelerationX += force * distanceX
                    accelerationY += force * distanceY
                }
            }

            velocityX[particleA] = velocityX[particleA] * 0.99 + accelerationX * mass[particleA]
            velocityY[particleA] = velocityY[particleA] * 0.99 + accelerationY * mass[particleA]
        }

        for (particle in 0 until mass.size) {
            positionX[particle] += velocityX[particle]
            positionY[particle] += velocityY[particle]

            drawer.stroke = ColorRGBa(64.0, 255.0, 255.0, .92)
            drawer.fill = ColorRGBa(64.0, 255.0, 255.0, .92)
            drawer.circle(positionX[particle], positionY[particle], mass[particle] * 1000)
        }
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 500
        height = 500
        title = "Wobbly Swarm"
    })
}