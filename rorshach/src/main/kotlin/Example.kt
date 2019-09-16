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

class Example : Program() { 
    private val idealFrameRAte = 60.0
    private var unitLength = min(width, height) / 640.0
    var unitSpeed = unitLength / idealFrameRAte
    private val fontPath = "Bellefair-Regular.ttf"
    private var currentFontSize = 14 * unitLength
    var bgColor = ColorRGBa.WHITE
    var frameCounter = TimedFrameCounter(true, 13 * idealFrameRAte) {}
    private lateinit var rorschachShape: RorschachShape
    private lateinit var rorschachShapeColor: NoFillShapeColor
    private lateinit var currentFont: FontImageMap
    override fun setup() {
        extend(NoClear()) {
            backdrop = {
                this@Example.drawer.background(this@Example.bgColor)
            }
        }
        //drawer.frameRate(IDEAL_FRAME_RATE)
        drawer.strokeWeight = max(1.0, 1.0 * unitLength)
        FrameCounter.initializeStatic(idealFrameRAte)
        currentFont = FontImageMap.fromUrl("file:data/fonts/$fontPath", currentFontSize)
        initialize()
        mouse.buttonDown.listen {
            initialize()
        }
    }

    override fun draw() {
        rorschachShape.step()
        rorschachShapeColor.aply(drawer)
        rorschachShape.draw(drawer)
        createSignFunction(200 * unitLength, 20 * unitLength, currentFontSize, NoStrokeShapeColor(ColorRGBa.BLACK),  NoStrokeShapeColor(bgColor), "Rorschach " + Date().toString() + "  -  FAL")
        frameCounter.step()
    }

    private fun createSignFunction(xMargin: Double, yMargin: Double, textSize: Double, textColor: NoStrokeShapeColor, textBackgroundColor: NoStrokeShapeColor, titleText: String){
        val textAreaHeight = yMargin + textSize * 1.1
        val leftX = width - xMargin
        val topY = height - textAreaHeight
        val baseLineY = height - yMargin
        textBackgroundColor.aply(drawer)
        drawer.rectangle(leftX, topY, xMargin, textAreaHeight)
        textColor.aply(drawer)
        drawer.text(titleText, leftX, baseLineY)
    }

    private fun initialize() {
        //drawer.background(bgColor)
        val rorschachShapeSize = 480 * unitLength
        rorschachShape = RorschachShape(
            rorschachShapeSize,
            floor(1.5 * rorschachShapeSize),
            Random.nextDouble(0.005, 0.04),
            Random.nextDouble(1.0, 4.0),
            0.0005
        )
        rorschachShape.centerPosition = Vector2(0.5 * width, 0.48 * height)
        rorschachShape.rotationAngle = PI + PI / 2
        rorschachShapeColor = NoFillShapeColor(ColorRGBa(0.0, Random.nextDouble(8.0, 48.0), 0.0))
        frameCounter.resetCount()
        frameCounter.on()
        // loop()
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 500
        height = 500
        title = "Rorschach"
    })
}