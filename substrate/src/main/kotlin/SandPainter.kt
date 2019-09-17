
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle
import kotlin.math.sin
import kotlin.random.Random

internal class SandPainter {
    private var c: ColorRGBa = goodcolor[Random.nextInt(numpal)]!!
    private var g: Double = Random.nextDouble(0.01, 0.1)

    fun render(drawer: Drawer, x: Double, y: Double, ox: Double, oy: Double) {
        // modulate gain
        g += Random.nextDouble(-0.050, 0.050)
        val maxg = 1.0
        if (g < 0) g = 0.0
        if (g > maxg) g = maxg

        // calculate grains by distance
        val grains = 64

        // lay down grains of sand (transparent pixels)
        val w = g / (grains - 1)
        drawer.circles((0 until grains).map {
            val a = 0.1 - it / grains //<-- (grains * 10.0)
            drawer.stroke = c.opacify(a)
            drawer.fill = null
            Circle(Vector2(ox + (x - ox) * sin(sin(it * w)), oy + (y - oy) * sin(sin(it * w))), 1.0) })
    }
}