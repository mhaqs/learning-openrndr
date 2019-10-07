import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.shape.shape
import kotlin.math.PI
import kotlin.random.Random

class Particle(private val drawer: Drawer, var x: Double = 0.0, var y: Double=0.0) {
    private var rScale = 632.727
    var r = 0.0
    private var velocityScale = 592.94118
    private var velocityMag = 0.0
    var velocity =  Vector2(0.0, 0.0)
    private var initialVx = 0.0
    private var initialVy = 0.0
    private var position = Vector2(x, y)

    private var lidMassScale = 580.0
    private var lidMass = 1000.0
    private var grav = .07
    private var lidForce = 0.0
    private var lidAccel = 0.0
    private var lidVelocity = 0.0
    private var lidPos = 220.0
    private var lidMomentum = 0.0

    init {
        val randx = Random.nextDouble(-5.0, 5.0)
        val randy = Random.nextDouble(-5.0, 5.0)
        initialVx = randx
        initialVy = velocity.y
        velocity = Vector2(randx, randy)
    }

    fun display(lidTop: Double, vMag: Double, r: Double): Double {
        velocity = Vector2(initialVx * vMag, initialVy * vMag)
        this.r = r
        var particleMomentum = 0.0
        drawer.fill = ColorRGBa.BLACK
        drawer.circle(position.x, position.y, r)

        position += velocity
        if (position.x-r < 120) {
            position = Vector2(r+120, position.y)
            velocity = Vector2(velocity.x * -1, velocity.y)
            initialVx *= -1
        }
        if (position.x+r > 380) {
            position = Vector2(380 -r, position.y)
            velocity = Vector2(velocity.x * -1, velocity.y)
            initialVx *= -1
        }
        if (position.y-r < lidTop-20) {
            position = Vector2(position.x, r+lidTop-20)
            particleMomentum = (PI*r*r) * velocity.y
            velocity = Vector2(velocity.x, velocity.y * -1)
            initialVy *= -1
        }
        if (position.y+r > 580) {
            position = Vector2(position.x, 580-r)
            velocity = Vector2(velocity.x, velocity.y * -1)
            initialVy *= -1
        }
        return particleMomentum
    }

    fun displayOtherFeatures(totalParticleMomentum: Double):Double {
        lidForce = lidMass * grav
        lidAccel = lidForce / lidMass
        lidVelocity += lidAccel
        lidMomentum = lidVelocity * lidMass
        lidMomentum += totalParticleMomentum
        lidVelocity = lidMomentum / lidMass
        lidPos += lidVelocity
        if (lidPos < 220.0) {
            lidPos = 220.0
            lidVelocity = 0.0
        }
        if (lidPos > 530) {
            lidPos = 530.0
            lidVelocity = 0.0
        }
        drawer.fill = ColorRGBa.BLACK
        drawer.rectangle(100.0, 200.0, 20.0, 400.0)
        drawer.rectangle(100.0, 580.0, 300.0, 20.0)
        drawer.rectangle(380.0, 200.0, 20.0, 400.0)
        val s = shape {
            contour {
                moveOrLineTo(120.0, lidPos - 20)
                moveOrLineTo(380.0, lidPos - 20)
                moveOrLineTo(350.0, lidPos - 50)
                moveOrLineTo(150.0, lidPos - 50)
                moveOrLineTo(120.0, lidPos - 20)
            }
        }
        drawer.shape(s)
        drawer.circle(250.0, lidPos-70.0, 50.0)
        drawer.fill = ColorRGBa.WHITE
        drawer.circle(250.0, lidPos-70.0, 30.0)

        lidMassScale = slider(lidMassScale, 600.0, 40.0, "Lid Mass")
        // this follows the format: lowerBound + ((whateverScale + 40 - x)/80)*(upperBound - lowerBound)
        lidMass = 1000.0 + ((lidMassScale + 40.0 - 600)/80)*(2000 - 1000)
        drawer.stroke = ColorRGBa.BLACK
        drawer.fill = ColorRGBa.BLACK
        writer.text(lidMass.toString(), "file:data/fonts/Palatino-Linotype.ttf", 14.0, 515.0, 40.0)

        return lidPos
    }

    fun velocitySlider(): Double {
        velocityScale = slider(velocityScale, 600.0, 80.0, "Velocity Scalar")
        velocityMag = .3 + ((velocityScale + 40 - 600)/80)*(2 - .3)
        drawer.stroke = ColorRGBa.BLACK
        return velocityMag
    }
    fun rSlider(): Double {
        rScale = slider(rScale, 600.0, 120.0, "Particle Mass")
        r = .3 + ((rScale + 40 - 600)/80)*(2.5 - .3)
        drawer.stroke = ColorRGBa.BLACK
        return r
    }

    fun slider(a: Double, x: Double, y: Double, title: String): Double {  //  creates sliding scales where 'a' is the location of the slider
        var aShadow = a
        drawer.apply {
            stroke = ColorRGBa.BLACK
            lineSegment(x - 40, y, x + 40, y)
            lineSegment(x - 40, y - 5, x - 40, y + 5)
            lineSegment(x + 40, y - 5, x + 40, y + 5)
            stroke = ColorRGBa.TRANSPARENT
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