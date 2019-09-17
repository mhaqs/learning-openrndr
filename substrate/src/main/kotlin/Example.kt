
import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.RenderTarget
import org.openrndr.draw.loadImage
import org.openrndr.extra.noclear.NoClear
import org.openrndr.math.map
import org.openrndr.shape.Circle
import org.openrndr.shape.Composition
import org.openrndr.shape.CompositionDrawer
import kotlin.random.Random

// Substrate Watercolor
// j.tarbell   June, 2004
// Albuquerque, New Mexico
// complexification.net

// j.tarbell   April, 2005

var dimx = 900
var dimy = 900
var num = 0
var maxnum = 200

// grid of cracks
var crackGrid = IntArray(dimx * dimy)
internal var cracks = arrayOfNulls<Crack>(maxnum)

// color parameters
var maxpal = 512
var numpal = 0
var goodcolor = arrayOfNulls<ColorRGBa>(maxpal)
lateinit var image: ColorBuffer
class Example : Program() {
    override fun setup() {
        image = loadImage("file:data/images/pollockShimmering.gif")
        extend(NoClear()) {
            backdrop = {
                this@Example.drawer.background(ColorRGBa.WHITE)
            }
        }
        takeColor()
        begin()
        mouse.buttonDown.listen {
            begin()
        }
    }

    override fun draw() {
        // crack all cracks
        val circles: ArrayList<Circle> = ArrayList()
        for (n in 0 until num) {
            cracks[n]?.move(drawer, circles)
        }
        // draw black crack
        drawer.stroke = ColorRGBa.BLACK.opacify(0.85)
        drawer.fill = null
        drawer.circles(circles)
    }

    private fun begin() {
        // erase crack grid
        for (y in 0 until dimy) {
            for (x in 0 until dimx) {
                crackGrid[y * dimx + x] = 10001
            }
        }
        // make random crack seeds
        for (k in 0 until 16) {
            val i = Random.nextInt(dimx * dimy - 1)
            crackGrid[i] = Random.nextInt(360)
        }

        // make just three cracks
        num = 0
        for (k in 0 until 3) {
            if (num < maxnum) {
                // make a new crack instance
                cracks[num] = Crack()
                num++
            }
        }
    }

    private fun takeColor() {
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                image.shadow.download()
                val c = image.shadow[x,y]
                var exists = false
                for (n in 0 until numpal) {
                    if (c == goodcolor[n]) {
                        exists = true
                        break
                    }
                }
                if (!exists) {
                    // add color to pal
                    if (numpal < maxpal) {
                        goodcolor[numpal] = c
                        numpal++
                    }
                }
            }
        }
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 900
        height = 900
        title = "Substrate"
    })
}