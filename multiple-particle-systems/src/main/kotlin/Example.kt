
import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.FontImageMap
import org.openrndr.math.Vector2
import java.util.*

class Example : Program() {

    private var particleSystems = ArrayList<CrazyParticleSystem>()
    lateinit var font: FontImageMap

    override fun setup() {
        font = FontImageMap.fromUrl("file:data/fonts/Roboto-Medium.ttf", 16.0)
        mouse.buttonDown.listen {
            particleSystems.add(CrazyParticleSystem(1, Vector2(mouse.position.x, mouse.position.y)))
        }
    }

    override fun draw() {
        drawer.background(ColorRGBa.BLACK)
        drawer.fontMap = font

        for (ps in particleSystems) {
            ps.run(drawer)
            ps.addParticle()
        }
        if (particleSystems.isEmpty()) {
            drawer.fill = ColorRGBa.WHITE
            drawer.text("click mouse to add particle systems", width / 2.0, height / 2.0)
        }
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 640
        height = 360
        title = "Multiple Particle Systems"
    })
}