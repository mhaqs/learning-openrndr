import org.openrndr.draw.DrawPrimitive
import org.openrndr.draw.Drawer
import org.openrndr.draw.VertexBuffer
import org.openrndr.draw.vertexBuffer
import org.openrndr.draw.vertexFormat
import org.openrndr.extra.noise.perlinLinear
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.random.Random

class RorschachShape(private var shapeSize: Double, private var noiseMagnitudeFactor: Double, vertexcount: Double = floor(0.75 * shapeSize), noisedistancescale: Double = shapeSize / 320.0, noisetimescale: Double = 0.0005) {
    var centerPosition = createVector()
    var rotationAngle = 0.0
    private var vertexCount = vertexcount
    val geometry: VertexBuffer = vertexBuffer(
            vertexFormat {
                position(3)
            }, vertexCount.toInt())
    private var noiseDistanceScale = noisedistancescale
    private var noiseTimeScale = noisetimescale
    private var xNoiseParameterOffset = createVector(Random.nextDouble(), Random.nextDouble()).times(1240.0)
    private var yNoiseParameterOffset = createVector(Random.nextDouble(), Random.nextDouble()).times(1240.0)
    private var noiseTime = 0.0
    private var reachedEndOfScreen = false

    init {
        if (isNotInitialized)
            initializeStatic()
    }

    fun step() {
        noiseTime += noiseTimeScale
    }

    fun draw(drawer: Drawer) {
        if (reachedEndOfScreen)
            return
        drawer.translate(centerPosition.x, centerPosition.y)
        drawer.rotate(rotationAngle)
        drawVertices(drawer, +1.0)
        drawVertices(drawer, -1.0)
        drawer.rotate(-rotationAngle)
        drawer.translate(-centerPosition.x, -centerPosition.y)
    }

    fun drawVertices(drawer: Drawer, yScaleFactor: Double) {
        val noiseMagnitude = noiseMagnitudeFactor * 0.5 * shapeSize
        var currentBasePositionX = -0.5 * shapeSize
        val basePositionIntervalDistance = shapeSize / vertexCount
        val progressRatio = frameCounter.getProgressRatio()
        geometry.put {
            for (i in 0 until vertexCount.toInt()) {
                val distanceFactor = progressRatio * sin((i / vertexCount) * PI) * (frameCounter.sin((i / vertexCount) * PI))
                val noiseX = (2 * perlinLinear(0, xNoiseParameterOffset.x + noiseDistanceScale * currentBasePositionX, xNoiseParameterOffset.y + noiseTime) - 1) * noiseMagnitude
                val noiseY = (2 * perlinLinear(0, yNoiseParameterOffset.x + noiseDistanceScale * currentBasePositionX, yNoiseParameterOffset.y + noiseTime) - 1) * noiseMagnitude
                val vertexPositionX = currentBasePositionX + distanceFactor * noiseX
                val vertexPositionY = yScaleFactor * distanceFactor * (0.3 * shapeSize + noiseY)
                write(Vector3(vertexPositionX, vertexPositionY, 0.0))
                var rotatedVertexPosition = Vector2(vertexPositionX, vertexPositionY)
                rotatedVertexPosition = rotate(rotatedVertexPosition, rotationAngle)
                checkScreen(drawer, centerPosition.x + rotatedVertexPosition.x, centerPosition.y + rotatedVertexPosition.y)
                currentBasePositionX += basePositionIntervalDistance
            }
        }
        drawer.vertexBuffer(geometry, DrawPrimitive.TRIANGLES)
    }

    fun checkScreen(drawer: Drawer, absolutePositionX: Double, absolutePositionY: Double) {
        val xMargin = 0.01 * drawer.width
        val yMargin = 0.05 * drawer.height
        if (absolutePositionX < xMargin || absolutePositionX > drawer.width - xMargin ||
                absolutePositionY < yMargin || absolutePositionY > drawer.height - yMargin) {
            reachedEndOfScreen = true
        }
    }

    fun createVector(x: Double = 0.0, y: Double = 0.0): Vector2 {
        return Vector2(x, y)
    }

    fun rotate(vector2: Vector2, theta: Double): Vector2 {
        return Vector2(vector2.x * cos(theta) - vector2.y * sin(theta), vector2.x * sin(theta) + vector2.y * cos(theta))
    }

    companion object {
        @JvmStatic
        var isNotInitialized: Boolean = true
        @JvmStatic
        var temporalVector = Vector2(0.0, 0.0)

        @JvmStatic
        fun initializeStatic() {
            temporalVector = Vector2(0.0, 0.0)
            isNotInitialized = false
        }
    }
}