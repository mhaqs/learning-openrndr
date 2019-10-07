import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MyIncline(private val drawer: Drawer) {

    private var iHeightScale = 340.0
    private var frictionScale = 340.0
    private var massScale = 380.0
    private var iHeight = 250.0
    private var inclineAngle = 0.0
    private var rotateAngle = 0.0
    private var x = 650.0
    private var y = 600.0
    private var timer = 0.0

    private var mass = 1700.0

    private var frictionLevel = 0.0
    private var frictionLine = 0.0

    private var grav = -.07 * mass  //  -.07 is the acceleration due to gravity
    private var velocity = Vector2(0.0, 0.0)
    private var position = Vector2(x, y)

    private var gravPerp = Vector2(-sin(Math.toRadians(36.3)) * grav * cos(Math.toRadians(36.3)), cos(Math.toRadians(36.3)) * grav * cos(Math.toRadians(36.3)))
    private var gravParallel = Vector2(cos(Math.toRadians(36.3)) * grav * sin(Math.toRadians(36.3)), sin(Math.toRadians(36.3)) * grav * sin(Math.toRadians(36.3)))
    private var gravForce = Vector2(gravPerp.x + gravParallel.x, gravPerp.y + gravParallel.y)
    private var normalForce = Vector2(gravPerp.x * -1, gravPerp.y * -1)
    private var friction = Vector2(gravParallel.x * -1, gravParallel.y * -1)

    private var t = SpecialText(drawer, 14.0)

    fun display() {
        iHeightScale = slider(iHeightScale, 340.0, 40.0, "Incline Height")  //  sliding scales and their relative values
        frictionScale = slider(frictionScale, 340.0, 80.0, "Friction")
        massScale = slider(massScale, 340.0, 120.0, "Mass of Block")

        //  add acceleration
        velocity = Vector2(gravForce.x / mass, gravForce.y / mass)
        position += velocity

        inclineAngle = Math.toDegrees(atan(iHeight / 340))  //  find the angle of the incline
        gravPerp = Vector2(-sin(Math.toRadians(inclineAngle)) * grav * cos(Math.toRadians(inclineAngle)), cos(Math.toRadians(inclineAngle)) * grav * cos(Math.toRadians(inclineAngle)))
        normalForce = Vector2(gravPerp.x * -1, gravPerp.y * -1)
        gravParallel = Vector2(cos(Math.toRadians(inclineAngle)) * grav * sin(Math.toRadians(inclineAngle)), sin(Math.toRadians(inclineAngle)) * grav * sin(Math.toRadians(inclineAngle)))
        friction = Vector2(gravParallel.x * -1, gravParallel.y * -1).normalized
        friction *= (frictionLevel)
        if (friction.length > gravParallel.length) {  //  we don't want friction to move the block UP the ramp!
            friction = friction.normalized
            friction *= gravParallel.length
        }
        gravForce = Vector2(gravPerp.x + gravParallel.x, gravPerp.y + gravParallel.y)

        drawer.apply {
            fill = colorRGBaFrom256(200, 206, 250)  //  RESET button
            stroke = null
            rectangle(22.5, 220.0, 55.0, 35.0)//, 10)
            rectangle(22.5, 170.0, 55.0, 35.0)//, 10)
            if (mouseX in 22.5..77.5 && mouseY >= 220 && mouseY <= 255 && mousePressed) {
                init()
            }
            if (mouseX in 22.5..77.5 && mouseY >= 170 && mouseY <= 192 && mousePressed) {
                init()
                ms.currentScreen = 999
            }
            fill = ColorRGBa.BLACK
        }
        with(writer) {
            //textSize(14)
            text("Reset", 34.0, 242.0)
            text("Back", 36.0, 192.0)
            text(iHeight.toString(), 390.0, 40.0)
            text(frictionLevel.toString(), 390.0, 80.0)
            text(mass.toString(), 390.0, 120.0)
        }
        grav = -.07 * mass
        drawer.fill = ColorRGBa.BLACK
        drawer.stroke = ColorRGBa.BLACK
        writer.text("Force of gravity     (where    = -.07)", 100.0, 300.0)
        t.createVectorWithSubscript("F", "g", 100.0 + writer.textWidth("Force of gravity "), 300.0)
        t.createVector("a", 100 + writer.textWidth("Force of gravity     (where "), 300.0)
        drawer.fill = ColorRGBa(1.0, 0.0, 0.0)
        drawer.stroke = ColorRGBa(1.0, 0.0, 0.0)
        writer.text("Perpendicular component =     cos Ó¨", 100.0, 420.0)
        t.createVectorWithSubscript("F", "g", 100 + writer.textWidth("Perpendicular component = "), 420.0)
        drawer.fill = ColorRGBa(0.0, 0.0, 1.0)
        drawer.stroke = ColorRGBa(0.0, 0.0, 1.0)
        writer.text("Parallel component =     sin Ó¨", 100.0, 460.0)
        t.createVectorWithSubscript("F", "g", 100 + writer.textWidth("Parallel component = "), 460.0)
        drawer.fill = colorRGBaFrom256(150, 40, 180)
        writer.text("Normal Force", 100.0, 500.0)
        drawer.fill = ColorRGBa(0.0, 1.0, 0.0)
        writer.text("Force of Friction", 100.0, 540.0)
        drawer.fill = ColorRGBa.BLACK
        val prettyAngle = inclineAngle * 100 / 100
        writer.text("Incline Angle (Ó¨) = " + prettyAngle + "Â°", 100.0, 340.0)
        writer.text("Ó¨", 153.0, 158.0)
        val prettyNum = friction.length / normalForce.length
        writer.text("Coefficient of friction Î¼ =                                 = $prettyNum", 100.0, 380.0)
        t.createDivision("|force of friction|", "|normal force|", 100 + writer.textWidth("Coefficient of static friction =          "), 380.0)
        drawer.pushModel()
        drawer.scale(1.0, -1.0)  //  sets coordinates to standard system (origin at bottom left)
        drawer.translate(0.0, -drawer.height.toDouble())

        // this follows the format: lowerBound + ((whateverScale + 40 - x)/80)*(upperBound - lowerBound)
        iHeight = 0 + ((iHeightScale + 40 - 340) / 80) * (500 - 0)
        frictionLevel = 0 + ((frictionScale + 40 - 340) / 80) * (60 - 0)
        frictionLine = frictionLevel / 25
        mass = 800 + ((massScale + 40 - 340) / 80) * (1700 - 800)

        if (position.y < (iHeight / 340) * (position.x - 340)) {  //  if the block hits the incline
            if (timer < 1) {  //  resets vertical velocity to zero
                velocity = Vector2(velocity.x, 0.0)
                timer++
            }
            velocity += Vector2(normalForce.x / mass, normalForce.y / mass)
            velocity += Vector2(friction.x / mass, friction.y / mass)

            if (rotateAngle < inclineAngle) {
                rotateAngle += 5
            }
        }

        drawer.apply {
            pushModel()  //  draw falling block
            translate(position.x, position.y)
            rotate(Math.toRadians(rotateAngle))
            fill = ColorRGBa.BLACK
            rectangle(-50.0, 0.0, 50.0, 50.0)
            popModel()
            stroke = ColorRGBa.BLACK
            fill = null
            rectangle(x - 50.0, 600.0, 50.0, 50.0)

            pushModel()  //  draw force of gravity vector and components
            translate(150.0, 550.0)
            lineSegment(0.0, 0.0, gravForce.x, gravForce.y)
            lineSegment(gravForce.x, gravForce.y, gravForce.x - 5, gravForce.y + 5)
            lineSegment(gravForce.x, gravForce.y, gravForce.x + 5, gravForce.y + 5)
            stroke = ColorRGBa(150.0, 40.0, 180.0)
            lineSegment(0.0, 0.0, normalForce.x, normalForce.y)
            stroke = ColorRGBa(255.0, 0.0, 0.0)
            lineSegment(0.0, 0.0, gravPerp.x, gravPerp.y)
            stroke = ColorRGBa(0.0, 0.0, 1.0)
            pushModel()
            translate(gravPerp.x, gravPerp.y)
            lineSegment(0.0, 0.0, gravParallel.x, gravParallel.y)
            stroke = ColorRGBa(0.0, 1.0, 0.0)
            lineSegment(0.0, 0.0, friction.x, friction.y)
            popModel()
            popModel()

            stroke = ColorRGBa.BLACK//  draw incline
            fill = ColorRGBa.BLACK
            val s = org.openrndr.shape.shape {
                contour {
                    moveOrLineTo(340.0, 0.0)
                    moveOrLineTo(680.0, 0.0)
                    moveOrLineTo(680.0, iHeight)
                    moveOrLineTo(340.0, 0.0)
                }
            }
            shape(s)
            for (i in 0 until sqrt(iHeight * iHeight + 340 * 340.0).toInt() step 3) {
                val yBump = (iHeight / 340) * (i - 340)
                lineSegment(680.0 - i, -yBump, 680.0 - i, -yBump + frictionLine)
            }

            popModel()
        }
    }

    private fun init() {  //  reset to beginning conditions
        rotateAngle = 0.0
        timer = 0.0
        mass = 1700.0
        grav = -.07 * mass
        velocity = Vector2(0.0, 0.0)
        position = Vector2(x, y)
    }

    private fun slider(a: Double, x: Double, y: Double, title: String): Double {  //  creates sliding scales where 'a' is the location of the slider
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
            circle(a, y + 1, 10.0)
        }
        return aShadow
    }
}