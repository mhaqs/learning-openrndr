import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.FontImageMap
import org.openrndr.extra.p5.PositionMode
import org.openrndr.extra.p5.distance
import org.openrndr.extra.p5.ellipse
import org.openrndr.extra.p5.from256
import org.openrndr.extra.p5.triangle
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.text.Writer
import kotlin.math.PI

var da: Double = 0.0 // delta angle
var dx: Double = 0.0 // noise increment value

var still = false // toggle still image on/off
var inkblots: MutableList<Inkblot> = arrayListOf()

lateinit var robotoFont: FontImageMap
lateinit var arialFontB: FontImageMap
lateinit var arialFontA: FontImageMap

class Example : Program() {
    lateinit var writer: Writer
    override fun setup() {
        robotoFont = FontImageMap.fromUrl("file:data/fonts/Roboto-Medium.ttf", 14.0)
        arialFontB = FontImageMap.fromUrl("file:data/fonts/IBMPlexMono-Bold.ttf", 28.0)
        arialFontA = FontImageMap.fromUrl("file:data/fonts/IBMPlexMono-Bold.ttf", 18.0)
        drawer.fontMap = arialFontB

        writer = Writer(drawer)

        da = PI / 100 // delta angle
        dx = 0.05 // noise increment value
        //initInkBlots() // initializing my inkBlots

        mouse.buttonDown.listen {
            touchStarted()
        }

        window.sized.listen {
            windowResized()
        }
    }

    override fun draw() {
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = ColorRGBa.WHITE
        //drawer.triangle(0.0, 0.0, 200.0, 200.0, -200.0, 200.0)
        drawer.triangle(PositionMode.CENTER, listOf(Vector3(640.0, 360.0, 0.0),  Vector3(840.0, 360.0, 0.0),  Vector3(540.0, 160.0, 0.0)))
//        if (!still) {
//            drawer.apply {
//                background(ColorRGBa.WHITE)
//                stroke = null
//                for (i in inkblots) {
//                    i.drawInk()
//                }
//                fill = ColorRGBa.BLACK
//                writer.newLine()
//                writer.move(width / 2.0, height / 2.0 - 30)
//                writer.text("Online")
//                writer.newLine()
//                writer.move(width / 2.0, height / 2.0)
//                writer.text("Rorschach's Test")
//                writer.drawStyle.fontMap = arialFontA
//                //writer.newLine()
//                writer.move(width / 2.0, height / 2.0 + 40)
//                writer.text("What do you see?")
//                writer.drawStyle.fontMap = robotoFont
//                fill = ColorRGBa.BLACK
//                //writer.newLine()
//                writer.move(width / 2.0, height - 40.0)
//                writer.text("Click / Touch")
//            }
//        }
    }


    private fun windowResized() {
        initInkBlots()
        still = false
    }


    private fun touchStarted() {
        still = !still
    }

    private fun initInkBlots() {
        inkblots.add(Inkblot(drawer, 100.0, 450.0, -20.0, 20.0, ColorRGBa.BLACK))
        inkblots.add(Inkblot(drawer, -300.0, -50.0, 100.0, -100.0, ColorRGBa.BLACK))
        inkblots.add(Inkblot(drawer, 50.0, 450.0, -200.0, 200.0, ColorRGBa.WHITE))
        inkblots.add(Inkblot(drawer, 70.0, 240.0, 130.0, -130.0, ColorRGBa.WHITE))
        inkblots.add(Inkblot(drawer, -400.0, 0.0, 20.0, -20.0, ColorRGBa.BLACK))
        inkblots.add(Inkblot(drawer, 50.0, 500.0, -10.0, 10.0, ColorRGBa.WHITE))
        inkblots.add(Inkblot(drawer, -400.0, -20.0, 100.0, -50.0, ColorRGBa.BLACK))
        inkblots.add(Inkblot(drawer, 0.0, 200.0, 0.0, -100.0, ColorRGBa.BLACK))
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 1280
        height = 720
        title = "Ink Blot"
    })
}