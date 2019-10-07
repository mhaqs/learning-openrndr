import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.red
import kotlin.random.Random


// space filling box
class Box {

    var x: Double = 0.0
    var y: Double = 0.0
    var d: Double = 0.0
    lateinit var myc: ColorRGBa
    var okToDraw: Boolean = false
    var chaste = true

    init {
        // random initial conditions
        selfinit()
    }

    fun selfinit() {
        // position
        okToDraw = false
        x = Random.nextDouble(dim.toDouble())
        y = Random.nextDouble(dim.toDouble())
        d = 0.0
        myc = readBackground(x, y)
    }

    fun draw(drawer: Drawer) {
        expand()
        if (okToDraw) {
                drawer.fill = myc
                drawer.rectangle(x, y, d, d)
        }
    }

    fun expand()
    {
        // assume expansion is ok
        d += 2

        // look for obstructions around perimeter at width d
        var obstructions = 0
        for (j in (x - d / 2 - 1).toInt() until (x + d / 2).toInt()) {
            var k = (y - d / 2 - 1)
            obstructions += checkPixel(j, k.toInt())
            k = (y + d / 2)
            obstructions += checkPixel(j, k.toInt())
        }
        for (k in (y - d / 2 - 1).toInt() until (y + d / 2).toInt()) {
        var j = (x - d / 2 - 1)
        obstructions += checkPixel(j.toInt(), k)
        j = (x + d / 2)
        obstructions += checkPixel(j.toInt(), k)
    }

        if (obstructions > 0) {
            // reset
            selfinit()
            if (chaste) {
                makeNewBox()
                chaste = false
            }
        } else {
            okToDraw = true
        }
    }

    private fun checkPixel(x: Int, y: Int): Int {
        if (x > dimborder && x < dimx - dimborder || x == 0 || x == dimx - 1) {
            if (y > dimborder && y < dim - dimborder || y == 0 || y == dim - 1) {
                ab.shadow.download()
                val c = ab.shadow[x, y]
                return if (c.r + c.g + c.b > 0) {
                    // a lit pixel has been found
                    1
                } else {
                    0
                }
            }
        }
        return 0
    }

}