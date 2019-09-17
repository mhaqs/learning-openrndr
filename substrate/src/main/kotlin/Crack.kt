
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

internal class Crack {
    private var x: Double = 0.0
    private var y: Double = 0.0
    private var directionOfTravel: Double = 0.0    // direction of travel in degrees
    // sand painter
    private var sandPainter: SandPainter = SandPainter()

    init {
        // find placement along existing crack
        findStart()
    }

    private fun findStart() {
        // pick random point
        var px = 0
        var py = 0

        // shift until crack is found
        var found = false
        var timeout = 0
        while (!found || timeout++ > 1000) {
            px = Random.nextInt(dimx)
            py = Random.nextInt(dimy)
            if (crackGrid[py * dimx + px] < 10000) {
                found = true
            }
        }

        if (found) {
            // start crack
            var a = crackGrid[py * dimx + px]
            if (Random.nextInt(100) < 50) {
                a -= 90 + Random.nextDouble(-2.0, 2.1).toInt()
            } else {
                a += 90 + Random.nextDouble(-2.0, 2.1).toInt()
            }
            startCrack(px, py, a)
        }
    }

    private fun startCrack(X: Int, Y: Int, T: Int) {
        x = X.toDouble()
        y = Y.toDouble()
        directionOfTravel = T.toDouble()//%360
        x += 0.61 * cos(directionOfTravel * PI / 180)
        y += 0.61 * sin(directionOfTravel * PI / 180)
    }

    fun move(drawer: Drawer, circles: ArrayList<Circle>) {
        // continue cracking
        x += 0.42 * cos(directionOfTravel * PI / 180)
        y += 0.42 * sin(directionOfTravel * PI / 180)

        // bound check
        val z = 0.33
        val cx = (x + Random.nextDouble(-z, z)).toInt()  // add fuzz
        val cy = (y + Random.nextDouble(-z, z)).toInt()

        // draw sand painter
        regionColor(drawer)

        circles.add(Circle(Vector2(x + Random.nextDouble(-z, z), y + Random.nextDouble(-z, z)), 1.0))

        if ((cx >= 0) && (cx < dimx) && (cy >= 0) && (cy < dimy)) {
            // safe to check
            if ((crackGrid[cy * dimx + cx] > 10000) || (abs(crackGrid[cy * dimx + cx] - directionOfTravel) < 5)) {
                // continue cracking
                crackGrid[cy * dimx + cx] = directionOfTravel.toInt()
            } else if (abs(crackGrid[cy * dimx + cx] - directionOfTravel) > 2) {
                // crack encountered (not self), stop cracking
                findStart()
                makeCrack()
            }
        } else {
            // out of bounds, stop cracking
            findStart()
            makeCrack()
        }
    }

    private fun makeCrack() {
        if (num < maxnum) {
            // make a new crack instance
            cracks[num] = Crack()
            num++
        }
    }

    private fun regionColor(drawer: Drawer) {
        // start checking one step away
        var rx = x
        var ry = y
        var openspace = true

        // find extents of open space
        while (openspace) {
            // move perpendicular to crack
            rx += 0.81 * sin(directionOfTravel * PI / 180)
            ry -= 0.81 * cos(directionOfTravel * PI / 180)
            val cx = rx.toInt()
            val cy = ry.toInt()
            if ((cx >= 0) && (cx < dimx) && (cy >= 0) && (cy < dimy)) {
                // safe to check
                if (crackGrid[cy * dimx + cx] > 10000) {
                } else {
                    openspace = false
                }
            } else {
                openspace = false
            }
        }
        // draw sand painter
        sandPainter.render(drawer, rx, ry, x, y)
    }
}