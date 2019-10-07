import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Point(private val drawer: Drawer) {
    var x: Double = 0.0
    var y: Double = 0.0
    var lastX: Double = 0.0
    var lastY: Double = 0.0
    var nextX: Double = 0.0
    var nextY: Double = 0.0
    var velX: Double = 0.0
    var velY: Double = 0.0
    private var t: Double = 0.0
    private var deltaTScale = 570.0
    private var deltaT = .05
    var followMouse = false

    fun move(pin: Boolean) {
        if (!pin) {
            velX = x - lastX  //  this is where velocity is calculated with verlet integration
            velY = y - lastY

            velX *= .97
            velY *= .97

            nextX = x + velX
            nextY = y + velY  //  add .1 to add gravity

            lastX = x
            lastY = y

            x = nextX
            y = nextY
        } else {
            if (!followMouse) {  //  the tether travels in a circle, normally
                circlePath()
                deltaTScale = slider(deltaTScale, 600.0, 40.0, "Orbit Speed")
                // this follows the format: lowerBound + ((whateverScale + 40 - x)/80)*(upperBound - lowerBound)
                deltaT = .01 + ((deltaTScale + 40 - 600) / 80) * (.2 - .01)
            } else {
                x = mouseX  //  ...or it follows the mouse
                y = mouseY
            }
        }
    }

    private fun circlePath() {  //  create the circular path
        drawer.apply {
            strokeWeight = 1.0
            stroke = ColorRGBa.BLACK
            circle(340.0, 340.0, 10.0)
            fill = null
            circle(340.0, 340.0, 260.0)
        }
        t += deltaT
        x = cos(t) * 130 + 340
        y = sin(t) * 130 + 340
        if (t > 2 * PI) {
            t = 0.0
        }
    }

    private fun slider(a: Double, x: Double, y: Double, title: String): Double {  //  creates sliding scales where 'a' is the location of the slider
        drawer.apply {
            stroke = ColorRGBa.BLACK
            lineSegment(x - 40, y, x + 40, y)
            lineSegment(x - 40, y - 5, x - 40, y + 5)
            lineSegment(x + 40, y - 5, x + 40, y + 5)
            stroke = null
            fill = ColorRGBa.BLACK
            //textAlign(CENTER)
            writer.text(title, "file:data/fonts/Palatino-Linotype.ttf", 12.0, x, y - 10)
        }
        //textAlign(LEFT)
        var aShadow = a
        if (mouseX >= x - 40 && mouseX <= x + 40 && mouseY >= y - 5 && mouseY <= y + 5 && mousePressed) {
            aShadow = mouseX
            if (aShadow < x - 40) {
                aShadow = x - 40
            }
            if (aShadow > x + 40) {
                aShadow = x + 40
            }
        }
        drawer.fill = ColorRGBa(0.588, 0.588, 0.588)
        drawer.circle(aShadow, y + 1, 10.0)
        return aShadow
    }
}