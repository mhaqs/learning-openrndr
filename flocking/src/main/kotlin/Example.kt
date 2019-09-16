import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration

class Example: Program() {
    private var flock: Flock = Flock()

    override fun setup() {
        mouse.buttonDown.listen {
            flock.addBoid(Boid(mouse.position.x, mouse.position.y))
        }
        for (i in 0..149) {
            flock.addBoid(Boid((width / 2.0), (height / 2.0)))
        }
    }

    override fun draw() {
        drawer.background(ColorRGBa.BLACK)
        flock.run(drawer)
    }
}

fun main() {
    Application.run(Example(), configuration{
        width = 640
        height = 360
        title = "Flocking"
    })
}