
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
                this@Example.drawer.image(image)
            }
        }
        takeColor()
        begin()
        drawer.background(ColorRGBa.WHITE)
        mouse.buttonDown.listen {
            begin()
            drawer.background(ColorRGBa.WHITE)
        }
    }

    override fun draw() {
        // crack all cracks
        for (n in 0 until num) {
            cracks[n]?.move(drawer)
        }
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
                val c = ColorRGBa(Random.nextDouble(255.0), Random.nextDouble(255.0), Random.nextDouble(255.0))
                //val c = image.shadow[x,y] <-- need a way to get pixel data in openrndr
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