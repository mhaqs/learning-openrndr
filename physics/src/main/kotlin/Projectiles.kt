import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.shape.shape
import kotlin.math.cos
import kotlin.math.sin

class Projectiles(private val drawer: Drawer) {

    private var y = 0.0
    private var angle = 0.0
    private var landingY = 0.0
    private var initialV = 0.0
    private var cannonHeightScale = 540.0
    private var cannonAngleScale = 520.0
    private var landingYScale = 540.0
    private var initialVScale = 580.0
    private var impact = false
    private var accel = Vector2(0.0, 1.0)
    private var velocity = Vector2(0.0, 0.0)
    private var position = Vector2(0.0, 680.0)
    private var time = 0.0

    private var t: SpecialText

    init {
        y = 480.0
        t = SpecialText(drawer, 14.0)
    }

    fun display() {
        if (!impact) {  //  the ball moves as long as it doesn't impact anything
            velocity += accel
            position += velocity
            time++
        }

        drawer.fill = ColorRGBa.BLACK
        drawer.circle(position, 20.0)

        drawer.fill = colorRGBaFrom256(200, 206, 250)  //  FIRE button
        drawer.stroke = null
        drawer.rectangle(22.5, 220.0, 55.0, 35.0)//, 10)
        drawer.rectangle(22.5, 170.0, 55.0, 35.0)//, 10)
        drawer.fill = ColorRGBa.BLACK
        writer.text("Fire", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 39.0, 242.0)
        writer.text("Back", 36, 192)
        if (mouseX in 22.5..77.5 && mouseY >= 220 && mouseY <= 255 && mousePressed) {
            velocity = Vector2(-cos(Math.toRadians(angle - 450)) * initialV, -sin(Math.toRadians(angle - 450)) * initialV)
            position = Vector2(50.0, y - 62.5)
            impact = false
            time = 0.0
        }
        if (mouseX in 22.5..77.5 && mouseY >= 170 && mouseY <= 192 && mousePressed) {
            ms.currentScreen = 999
        }

        val rc = shape {
            contour {
                //  right cannon support arm
                moveOrLineTo(100.0, y + 10.0)
                moveOrLineTo(100.0, y - 70.0)
                moveOrLineTo(60.0, y - 70.0)
                moveOrLineTo(60.0, y - 55.0)
                moveOrLineTo(85.0, y - 55.0)
                moveOrLineTo(85.0, y + 10.0)
            }
        }
        drawer.shape(rc)

        cannonHeightScale = slider(cannonHeightScale, 540.0, 40.0, "Cannon Height")  //  sliding scales and their relative values
        cannonAngleScale = slider(cannonAngleScale, 540.0, 80.0, "Cannon Angle")
        landingYScale = slider(landingYScale, 540.0, 120.0, "Platform Height")
        drawer.fill = ColorRGBa.BLACK
        t.createVectorWithSubscript("v", "i", 504.0, 150.0)
        with(writer) {
            text((680 - y + 62.5).toString(), 590.0, 40.0)
            text((270 - angle).toString() + "Â°", 590.0, 80.0)
            text((680 - landingY).toString(), 590.0, 120.0)
            text(initialV.toString(), 590.0, 160.0)
        }
        initialVScale = slider(initialVScale, 540.0, 160.0, "     Magnitude")
        // this follows the format: lowerBound + ((whateverScale + 40 - x)/80)*(upperBound - lowerBound)
        y = 660 + ((cannonHeightScale + 40 - 540) / 80) * (380 - 660)
        angle = 255 + ((cannonAngleScale + 40 - 540) / 80) * (20 - 75)
        landingY = 660 + ((landingYScale + 40 - 540) / 80) * (380 - 660)
        initialV = 3 + ((initialVScale + 40 - 540) / 80) * (6.2 - 3)

        drawer.apply {
            stroke = ColorRGBa.BLACK
            for (pointX in 0 until 630) {
                val beforePointY = sin(Math.toRadians(angle - 450)) * initialV * (pointX - 1) + (-.1 / 2) * (pointX - 1) * (pointX - 1)  //  points used to determine maximum height
                val pointY = sin(Math.toRadians(angle - 450)) * initialV * pointX + (-.1 / 2) * pointX * pointX
                val afterPointY = sin(Math.toRadians(angle - 450)) * initialV * (pointX + 1) + (-.1 / 2) * (pointX + 1) * (pointX + 1)
                drawer.circle(pointX * initialV * -cos(Math.toRadians(angle - 450)) + 50, y - 62.5 - pointY, 1.0)
                if (pointY > beforePointY && pointY > afterPointY) {
                    fill = ColorRGBa(0.0, 1.0, 0.0)
                    circle(pointX * initialV * -cos(Math.toRadians(angle - 450)) + 50, y - 62.5 - pointY, 10.0)
                    fill = ColorRGBa.BLACK
                    writer.text((pointY + 62.5 + (680 - y)).toString(), "file:data/fonts/Palatino-Linotype.ttf", 14.0, 40 + writer.textWidth("Max Height =    =  (     sin Ó¨ )^2  /  2    = "), 120.0)
                }
                fill = ColorRGBa(0.588, 0.588, 0.588)
            }

            pushModel()  //  draw cannon
            stroke = ColorRGBa.BLACK
            translate(50.0, y - 62.5)
            rotate(Math.toRadians(angle))
            val c = shape {
                contour {
                    moveOrLineTo(-10.0, 15.0)
                    moveOrLineTo(10.0, 15.0)
                    moveOrLineTo(10.0, 20.0)
                    moveOrLineTo(-10.0, 20.0)
                    moveOrLineTo(-15.0, -40.0)
                    curveTo(-10.0, -60.0, 10.0, -60.0, 15.0, -40.0)
                    moveOrLineTo(10.0, 20.0)
                }
            }
            shape(c)
            stroke = null
            popModel()

            fill = ColorRGBa.BLACK
            rectangle(0.0, y, 100.0, 680 - y)  //  draw the lefthand platform
            rectangle(0.0, 660.0, 680.0, 20.0)  //  draw the bottom rectangle
            rectangle(380.0, landingY, 300.0, 680 - landingY)  //  draw the righthand platform
        }

        //  collision tests
        if (position.x + 10 >= 380 && position.y - 5 >= landingY && position.y + 10 <= 660) {  //  if righthand side of ball hits wall
            impact = true
            position = Vector2(370.0, position.y)
        }
        if (position.x - 5 >= 380 && position.x <= 680 && position.y + 10 >= landingY) {  //  if bottom of ball hits top of landing
            impact = true
            position = Vector2(position.x, landingY - 10)
        }
        if (position.y >= 660) {  //  if bottom of ball hits base
            impact = true
            position = Vector2(position.x, 650.0)
        }

        val s = shape {
            contour {
                //  left cannon support arm
                moveOrLineTo(0.0, y + 10)
                moveOrLineTo(0.0, y - 70)
                moveOrLineTo(40.0, y - 70.0)
                moveOrLineTo(40.0, y - 55.0)
                moveOrLineTo(15.0, y - 55.0)
                moveOrLineTo(15.0, y + 10.0)
            }
        }
        drawer.shape(s)

        //  ball's flight statistics
        writer.text("Displacement = (    +  .5    Î”t ) Î”t =  ", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 40.0, 40.0)
        t.createVectorWithSubscript("v", "i", 40 + writer.textWidth("Displacement = ( "), 40.0)
        t.createVector("a", 40 + writer.textWidth("Displacement = (    +  .5 "), 40.0)
        if (position.x > 50) {
            writer.text(distance(position.x, position.y, 50.0, y - 62.5).toString(), 40 + writer.textWidth("Displacement = (    +  .5    Î”t ) Î”t = "), 40.0)
        } else {
            time = 0.0
        }
        writer.text("Air Time = ", 40, 80)
        writer.text(time.toString(), 40 + writer.textWidth("Air Time = "), 80.0)
        writer.text("Max Height =    =  (     sin Ó¨ )^2  /  2    =  ", 40, 120)
        t.createVectorWithSubscript("s", "y", 40 + writer.textWidth("Max Height = "), 120.0)
        t.createVectorWithSubscript("v", "i", 40 + writer.textWidth("Max Height =    =  ( "), 120.0)
        t.createVector("a", 40 + writer.textWidth("Max Height =    =  (     sin Ó¨ )^2  /  2 "), 120.0)
    }

    fun slider(a: Double, x: Double, y: Double, title: String): Double {  //  creates sliding scales where 'a' is the location of the slider
        var aShadow = a
        drawer.apply {
            stroke = ColorRGBa.BLACK
            lineSegment(x - 40, y, x + 40, y)
            lineSegment(x - 40, y - 5, x - 40, y + 5)
            lineSegment(x + 40, y - 5, x + 40, y + 5)
            stroke = null
            fill = ColorRGBa.BLACK
            //textAlign(CENTER)
            writer.text(title, "file:data/fonts/Palatino-Linotype.ttf", 12.0, x, y - 10)
            //textAlign(LEFT)
            if (mouseX >= x - 40 && mouseX <= x + 40 && mouseY >= y - 5 && mouseY <= y + 5 && mousePressed) {
                aShadow = mouseX
                if (aShadow < x - 40) {
                    aShadow = x - 40
                }
                if (aShadow > x + 40) {
                    aShadow = x + 40
                }
            }
            fill = ColorRGBa(0.588, 0.588, 0.588)
            circle(aShadow, y + 1, 10.0)
        }
        return aShadow
    }
}