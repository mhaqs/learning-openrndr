import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.extra.noclear.NoClear
import org.openrndr.shape.Composition
import org.openrndr.shape.CompositionDrawer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


// Binary Ring
// j.tarbell   March, 2004
// Albuquerque, New Mexico
// complexification.net
// Processing 0085 Beta syntax update April, 2005

var num = 5000           // total number of particles
var dim = 500            // dimensions of rendering window

// blackout is production control of white or black filaments
var blackout = false

// kaons is array of path tracing particles
var kaons = arrayOfNulls<Particle>(num)
//val compositionDrawer = CompositionDrawer()
////lateinit var composition: Composition
class Example : Program() {
    override fun setup() {
        extend(NoClear())
        drawer.background(ColorRGBa.BLACK)
        //composition = compositionDrawer.composition
        // begin with particle sling-shot around ring origin
        for (i in 0 until num) {
            val emitx = (30 * sin((2 * PI) * i / num) + dim / 2).toInt()
            val emity = (30 * cos((2 * PI) * i / num) + dim / 2).toInt()
            val r = PI * i / num
            kaons[i] = Particle(emitx, emity, r)
        }
        mouse.buttonDown.listen {
            // manually switch blackout periods
            blackout = !blackout
        }
    }

    override fun draw() {
        for (i in 0 until num) {
            kaons[i]!!.move(drawer)
        }

        // randomly switch blackout periods
        if (Random.nextInt(10000) > 9950) {
            blackout = !blackout
        }
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 500
        height = 500
        title = "Binary Ring"
    })
}