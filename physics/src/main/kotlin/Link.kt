import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.sqrt

class Link(private val drawer: Drawer, private var p: ArrayList<Point> = ArrayList(0)) {
    private var restingDistance = 2  //  how far apart each link wants to be
    private var diffX = Array(p.size) { 0.0 }
    private var diffY = Array(p.size) { 0.0 }
    private var distance = Array(p.size) { 0.0 }
    private var difference = Array(p.size) { 0.0 }
    private var translateX = Array(p.size) { 0.0 }
    private var translateY = Array(p.size) { 0.0 }
    private var velVector = Vector2(0.0, 0.0)
    private var centripAccel = Vector2(0.0, 0.0)
    private var deltaTheta = 0.0
    private var omega = 0.0
    private var prettyOmega = 0.0
    private var mouseTimer = 0.0
    private var t: SpecialText = SpecialText(drawer, 14.0)

    private fun solve() {
        for (i in 0 until p.size - 1) {
            // calculate the distance
            diffX[i] = p[i].x - p[i + 1].x
            diffY[i] = p[i].y - p[i + 1].y
            distance[i] = sqrt(diffX[i] * diffX[i] + diffY[i] * diffY[i])

            // difference scalar
            difference[i] = (restingDistance - distance[i]) / distance[i]

            // translation for each Point. They'll be pushed 1/2 the required distance to match their resting distances.
            translateX[i] = diffX[i] * 0.5 * difference[i]
            translateY[i] = diffY[i] * 0.5 * difference[i]

            p[i].x += translateX[i]
            p[i].y += translateY[i]

            p[i + 1].x -= translateX[i]
            p[i + 1].y -= translateY[i]
        }
    }

    fun display() {
        p[0].move(true)  //  pin = true for the first point only
        for (i in 1 until p.size) {
            p[i].move(false)
        }
        solve()
        for (i in 0 until p.size - 1) {
            drawer.apply {
                stroke = ColorRGBa.BLACK
                lineSegment(p[i].x, p[i].y, p[i + 1].x, p[i + 1].y)
                stroke = null
                fill = ColorRGBa.BLACK
                circle(p[i].x, p[i].y, 2.0)
                circle(p[i + 1].x, p[i + 1].y, 2.0)
            }
        }
        drawer.stroke = null
        drawer.circle(p[p.size - 1].x, p[p.size - 1].y, 20.0)

        val last = Vector2(p[p.size - 1].lastX - drawer.width / 2, p[p.size - 1].lastY - drawer.height / 2)  //  angle between last and next locations (from the screen's center)
        val next = Vector2(p[p.size - 1].nextX - drawer.width / 2, p[p.size - 1].nextY - drawer.height / 2)
        deltaTheta = angleBetween(last, next)
        omega = deltaTheta * 60.0  //  angular velocity = rad/sec, frameRate is 60 loops per sec, so the angle changes by 60*theta radians per sec
        prettyOmega = omega / PI * 100
        if (!p[0].followMouse) {
            writer.text("Angular velocity Ï‰ = " + prettyOmega / 100 + "Ï€ radians/sec","file:data/fonts/Palatino-Linotype.ttf", 14.0, 40.0, 120.0)
            val prettyCA = centripAccel.length * 100 / 100
            writer.text("Centripetal acceleration      =          = $prettyCA", 40.0, 80.0)
            t.createVectorWithSubscript("a", "c", 192.0, 80.0)
            t.createDivision("v^2", "r", 237.0, 80.0)
            drawer.apply {
                stroke = ColorRGBa(1.0, 0.0, 0.0)
                lineSegment(350.0, 60.0, 350.0, 100.0)
                lineSegment(350.0, 60.0, 345.0, 65.0)
                lineSegment(350.0, 60.0, 355.0, 65.0)
            }
        }
        drawer.apply {
            stroke = ColorRGBa(0.0, 1.0, 0.0)
            lineSegment(190.0, 20.0, 190.0, 60.0)
            lineSegment(190.0, 20.0, 185.0, 25.0)
            lineSegment(190.0, 20.0, 195.0, 25.0)
            val prettyV = velVector.length * 100 / 100
            writer.text("Velocity = $prettyV", 40.0, 40.0)
            fill = colorRGBaFrom256(200, 206, 250)
            stroke = null
            rectangle(500.0, 70.0, 170.0, 35.0)//, 10)
            rectangle(560.0, 120.0, 55.0, 35.0)//, 10)
        }
        if (mouseX in 560.0..615.0 && mouseY >= 120 && mouseY <= 155 && mousePressed) {  //  BACK button
            ms.currentScreen = 999
        }
        mouseTimer++
        if (mouseX in 500.0..670.0 && mouseY >= 70 && mouseY <= 105 && mousePressed) {  //  TOGGLE MOUSE CONTROL button
            if (!p[0].followMouse && mouseTimer > 7) {
                p[0].followMouse = true
                mouseTimer = 0.0
            }
            if (p[0].followMouse && mouseTimer > 7) {
                p[0].followMouse = false
                mouseTimer = 0.0
            }
        }
        drawer.fill = ColorRGBa.BLACK
        writer.text("Toggle Mouse Control", 515.0, 92.0)
        writer.text("Back", 560 + writer.textWidth("Back") / 2, 142.0)

        drawer.apply {
            pushModel()  //  draw the velocity vector
            translate(p[p.size - 1].x, p[p.size - 1].y)
            strokeWeight = (2.0)
            stroke = ColorRGBa(0.0, 1.0, 0.0)
        }
        velVector = Vector2(p[p.size - 1].nextX - p[p.size - 1].lastX, p[p.size - 1].nextY - p[p.size - 1].lastY).normalized
        velVector *= 8 * sqrt(p[p.size - 1].velX * p[p.size - 1].velX + p[p.size - 1].velY * p[p.size - 1].velY)
        drawer.lineSegment(0.0, 0.0, velVector.x, velVector.y)

        if (!p[0].followMouse) {
            drawer.stroke = ColorRGBa(1.0, 0.0, 0.0)  //  centripetal acceleration magnitude = V^2 / r
            centripAccel = Vector2(velVector.x, velVector.y).normalized
            centripAccel *= (velVector.length * velVector.length / distance(drawer.width / 2.0, drawer.height / 2.0, p[p.size - 1].x, p[p.size - 1].y))
            drawer.apply {
                pushModel()
                rotate(PI / 2)
                lineSegment(0.0, 0.0, centripAccel.x, centripAccel.y)
                popModel()

                pushModel()
                stroke = ColorRGBa(1.0, 0.0, 0.0)  //  the arrowhead for the centripetal acceleration vector
                rotate(PI / 2)
                translate(centripAccel.x, centripAccel.y)
                centripAccel /= 8.0
                rotate(3 * PI / 4)
                lineSegment(0.0, 0.0, centripAccel.x, centripAccel.y)
                rotate(PI / 2)
                lineSegment(0.0, 0.0, centripAccel.x, centripAccel.y)
                popModel()
            }
        }

        drawer.apply {
            pushModel()
            stroke = ColorRGBa(0.0, 1.0, 0.0)  //  the arrowhead for the velocity vector
            translate(velVector.x, velVector.y)
            velVector /= 8.0
            rotate(3 * PI / 4)
            lineSegment(0.0, 0.0, velVector.x, velVector.y)
            rotate(PI / 2)
            lineSegment(0.0, 0.0, velVector.x, velVector.y)
            popModel()

            popModel()
            strokeWeight = 1.0
        }
    }
}
