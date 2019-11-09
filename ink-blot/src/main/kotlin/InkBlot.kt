import org.openrndr.color.ColorRGBa
import org.openrndr.draw.DrawPrimitive
import org.openrndr.draw.Drawer
import org.openrndr.draw.VertexBuffer
import org.openrndr.draw.isolated
import org.openrndr.draw.vertexBuffer
import org.openrndr.draw.vertexFormat
import org.openrndr.extra.noise.perlinLinear
import org.openrndr.math.Vector3
import org.openrndr.math.map
import org.openrndr.shape.shape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

val geometry: VertexBuffer = vertexBuffer(
        vertexFormat {
            position(3)
        }, 3)

class Inkblot(private val drawer: Drawer, private var rMin: Double, private var rMax: Double, private var oscMin: Double, private var oscMax: Double, private var hue: ColorRGBa) {
    private var transX = drawer.width / 2.0
    private var transY = drawer.height / 2.0
    private var xoff = map(1.0, 5.0, 1000.0, 5000.0, Random.nextInt(1, 5).toDouble())
    private var yoff = 0.0

    fun drawInk() {
        drawer.fill = hue
        drawer.stroke = hue
        drawer.isolated {
            val osc2 = map(-1.0, 1.0, oscMin, oscMax, sin(yoff))
            translate(transX, transY + osc2)
            for (a in -PI / 2..(3 * PI) / 2 step da) {
                val n = perlinLinear(100, xoff, yoff)
                val r = map(0.0, 1.0, rMin, rMax + osc2, n)
                if (a <= PI / 2) {
                    xoff += dx
                } else {
                    xoff -= dx
                }
                geometry.put {
                    write(Vector3(r * cos(a), r * sin(a), 0.0))
                }
            }
            drawer.vertexBuffer(geometry, DrawPrimitive.TRIANGLES)
        }
        yoff += 0.03
    }

    private infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
        require(start.isFinite())
        require(endInclusive.isFinite())
        require(step > 0.0) { "Step must be positive, was: $step." }
        val sequence = generateSequence(start) { previous ->
            if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
            val next = previous + step
            if (next > endInclusive) null else next
        }
        return sequence.asIterable()
    }
}