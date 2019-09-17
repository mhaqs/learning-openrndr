import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Particle(Dx: Int, Dy: Int, r: Double) {
    var ox: Double = (dim / 2).toDouble()
    var oy: Double = (dim / 2).toDouble()
    var x: Double = ox - Dx
    var y: Double = oy - Dy
    var xx: Double = 0.0
    var yy: Double = 0.0

    var vx: Double = 2 * cos(r)
    var vy: Double = 2 * sin(r)
    var age = Random.nextInt(200)
    var i: ColorRGBa = if (blackout) {
        ColorRGBa.BLACK
    } else {
        ColorRGBa.WHITE
    }

    fun move(drawer: Drawer) {
        xx = x
        yy = y

        x += vx
        y += vy

        vx += (Random.nextDouble(100.0) - Random.nextDouble(100.0)) * 0.005
        vy += (Random.nextDouble(100.0) - Random.nextDouble(100.0)) * 0.005

        drawer.stroke = i.opacify(0.24)
        drawer.lineSegment(ox + xx, oy + yy, ox + x, oy + y)
        drawer.lineSegment(ox - xx, oy + yy, ox - x, oy + y)

        // grow old
        age++
        if (age > 200) {
            // die and be reborn
            val t = Random.nextDouble(2 * PI)
            x = 30 * sin(t)
            y = 30 * cos(t)
            xx = 0.0
            yy = 0.0
            vx = 0.0
            vy = 0.0
            age = 0
            i = if (blackout) {
                ColorRGBa.BLACK
            } else {
                ColorRGBa.WHITE
            }
        }
    }
}


