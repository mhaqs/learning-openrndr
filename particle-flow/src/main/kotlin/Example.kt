import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.math.map
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Example: Program() {
    var circle = 200.0
    var rot = 0.0
    var col= 0.0
    var freq = 0.000005
    var cont = 0
    var r = 0.0

    override fun draw() {
        drawer.background(ColorRGBa(242.0, 242.0, 242.0))
        drawer.translate(300.0, 300.0)
        drawer.rotate(rot)
        for (i in 0 until 500) {
            circle = 200 + 50 * sin(System.currentTimeMillis() * freq * i)
            col = map(150.0, 250.0, 255.0, 60.0, circle)
            r = map(150.0, 250.0, 5.0, 2.0, circle)
            drawer.fill = ColorRGBa(col / 255, 0.0, 0.74)
            drawer.stroke = ColorRGBa(col / 255, 0.0, 0.74)
            drawer.circle(circle * cos(i.toDouble()), circle * sin(i.toDouble()), r)
            rot += 0.00005
        }
    }
}

fun main() {
    Application.run(Example(), configuration{
        width = 600
        height = 600
        title = "Particle Flow"
    })
}