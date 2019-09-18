import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.FontImageMap
import org.openrndr.extra.noclear.NoClear
import org.openrndr.math.Vector2
import java.util.*
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
lateinit var frameCounter: TimedFrameCounter
var looping = true
class Example : Program() { 
    private val idealFrameRAte = 60.0
    private var unitLength: Double = 0.0
    var unitSpeed = unitLength / idealFrameRAte
    private val fontPath = "Bellefair-Regular.ttf"
    private var currentFontSize = 14 * unitLength
    var bgColor = ColorRGBa(0.98, 0.98, 0.98)

    private lateinit var rorschachShape: RorschachShape
    private lateinit var currentFont: FontImageMap
    override fun setup() {
        extend(NoClear()) {
            backdrop = {
                this@Example.drawer.background(this@Example.bgColor)
            }
        }
        //drawer.frameRate(IDEAL_FRAME_RATE)
        unitLength = min(width, height) / 640.0
        drawer.strokeWeight = max(1.0, 1.0 * unitLength)
        TimedFrameCounter.initializeStatic(idealFrameRAte)
        frameCounter = TimedFrameCounter(true, 13 * idealFrameRAte, completeBehavior = { looping = false })
        currentFont = FontImageMap.fromUrl("file:data/fonts/$fontPath", currentFontSize)
        initialize()
        mouse.buttonDown.listen {
            initialize()
        }
    }

    override fun draw() {
        if (looping) {
            rorschachShape.step()
            drawer.stroke = ColorRGBa.GRAY.opacify(0.22)
            rorschachShape.draw(drawer)
            frameCounter.step()
        }
    }

    private fun initialize() {
        val rorschachShapeSize = 480 * unitLength
        rorschachShape = RorschachShape(
            shapeSize = rorschachShapeSize,
            vertexcount = floor(1.5 * rorschachShapeSize),
            noisedistancescale = Random.nextDouble(0.005, 0.04),
            noiseMagnitudeFactor = Random.nextDouble(1.0, 4.0),
            noisetimescale = 0.0005
        )
        rorschachShape.centerPosition = Vector2(0.5 * width, 0.48 * height)
        rorschachShape.rotationAngle = PI + PI / 2
        frameCounter.resetCount()
        frameCounter.on()
        looping = true
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 1000
        height = 1000
        title = "Rorschach"
    })
}