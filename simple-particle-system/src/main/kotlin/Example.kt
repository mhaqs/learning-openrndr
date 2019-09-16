import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.math.Vector2

class Example: Program() {
    private lateinit var ps: ParticleSystem

    override fun setup() {
        ps = ParticleSystem(Vector2(width / 2.0, 50.0))
    }

    override fun draw() {
        drawer.background(ColorRGBa.BLACK)
        ps.addParticle()
        ps.run(drawer)
    }
}

fun main() {
    Application.run(Example(), configuration{
        width = 640
        height = 360
        title = "Simple Particle System"
    })
}