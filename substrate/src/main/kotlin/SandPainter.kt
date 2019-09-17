
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
        val circles: ArrayList<Circle> = ArrayList()
        for (i in 0 until grains) {
            val a = 1.0 - i / (grains * 10.0)
            drawer.stroke = c.opacify(a)
            drawer.fill = null
            circles.add(Circle(Vector2(ox + (x - ox) * sin(sin(i * w)), oy + (y - oy) * sin(sin(i * w))), 1.0))
        }
        drawer.circles(circles)
    }
}