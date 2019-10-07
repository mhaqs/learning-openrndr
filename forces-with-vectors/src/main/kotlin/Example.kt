import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.FontImageMap
import org.openrndr.math.Vector2
import kotlin.random.Random


class Example: Program() {
    // Five moving bodies
    private var movers = ArrayList<Mover>()

    // Liquid
    private lateinit var liquid: Liquid
    lateinit var font: FontImageMap

    override fun setup() {
        font = FontImageMap.fromUrl("file:data/fonts/Roboto-Medium.ttf", 16.0)
        drawer.fontMap = font
        liquid = Liquid(0.0, (height / 2).toDouble(), width.toDouble(), (height / 2).toDouble(), 0.1)
        mouse.buttonDown.listen {
            reset();
        }
    }

    override fun draw() {
        drawer.background(ColorRGBa.BLACK)
        // Draw water
        liquid.display(drawer)

        for (mover in movers) {
            // Is the Mover in the liquid?
            if (liquid.contains(mover)) {
                // Calculate drag force
                val drag: Vector2 = liquid.drag(mover)
                // Apply drag force to Mover
                mover.applyForce(drag)
            }
            // Gravity is scaled by mass here!
            val gravity = Vector2(0.0, 0.1 * mover.mass)
            // Apply gravity
            mover.applyForce(gravity)
            // Update and display
            mover.update()
            mover.display(drawer)
            mover.checkEdges(drawer)
        }

        drawer.fill = ColorRGBa.WHITE
        drawer.text("click mouse to reset", 10.0, 30.0)
    }

    // Restart all the Mover objects randomly
    private fun reset() {
        movers.clear()
        for (i in 0..10) {
            movers.add(Mover(Random.nextDouble(0.5, 3.0), (40.0 + i * 70.0), 0.0))
        }
    }
}

fun main() {
    Application.run(Example(), configuration{
        width = 640
        height = 360
        title = "Forces with Vectors"
    })
}