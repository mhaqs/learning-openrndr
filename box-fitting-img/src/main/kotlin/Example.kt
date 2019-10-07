import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.loadImage
import org.openrndr.extra.noclear.NoClear

var num = 0
var maxnum = 2000
var dimx = 1020
var dim = 660
var dimborder = 20.0
var time = 0.0

var boxes = arrayOfNulls<Box>(maxnum)

// background image
lateinit var ab:ColorBuffer

internal fun makeNewBox() {
    if (num < maxnum) {
        boxes[num] = Box()
        num++
    }
}

fun readBackground(x: Double, y: Double): ColorRGBa {
    // translate into ba image dimensions
    val ax = x * (ab.width*1.0)/dimx;
    val ay = y * (ab.height*1.0)/dim;

    ab.shadow.download()
    return ab.shadow[ax.toInt(), ay.toInt()]
}

class Example : Program() {
    override fun setup() {
        extend(NoClear())
        // load background image
        ab = loadImage("file:data/images/sky.gif")

        resetAll();
    }

    override fun draw() {
        for (n in 0 until num) {
            boxes[n]!!.draw(drawer)
        }
    }

    fun resetAll() {
        // stop drawing
        num = 0
// clear screen, prepare canvas
        drawer.background(ColorRGBa.BLACK)
        drawWhiteBorder()

        // initialize just five starting boxes


        makeNewBox()
        makeNewBox()
        makeNewBox()
        makeNewBox()
        makeNewBox()
    }

    internal fun drawWhiteBorder() {
        // draw white border
        drawer.stroke = null
        drawer.fill = ColorRGBa.WHITE
        drawer.rectangle(0.0, 0.0, dimx.toDouble(), dimborder)
        drawer.rectangle(0.0, 0.0, dimborder, dim.toDouble())
        drawer.rectangle(0.0, dim - dimborder, dimx.toDouble(), dimborder)
        drawer.rectangle(dimx - dimborder, 0.0, dimborder, dim.toDouble())
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = dimx
        height = dim
        title = "Emotion Fractal"
    })
}