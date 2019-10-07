import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.random.Random

class MyPlanet(private val drawer: Drawer, velX: Double, velY: Double, x: Double, y: Double, radius: Double, blackHole: Boolean) {
    private var gravity = 0.00000005
    private var r = 0.0
    private var mass = 0.0
    private var criticalMass = 320
    private var totalX = 0.0
    private var totalY = 0.0
    private var totalDist = 0.0
    private var avgDist = 0.0
    private var totalMass = 0.0
    private var avgMass = 0.0
    private var force = Vector2(0.0, 0.0)
    private var accel = Vector2(0.0, 0.0)
    private var velocity = Vector2(0.0, 0.0)
    private var position = Vector2(0.0, 0.0)
    private var c: ColorRGBa = ColorRGBa.BLACK
    private var hit = false
    private var isBlackHole = false
    private var rotateAngle = 0.0

    init {
        velocity = Vector2(velX, velY)
        position = Vector2(x, y)
        r = radius
        mass = r * r * PI
        isBlackHole = blackHole
        c = when {
            !isBlackHole -> ColorRGBa(Random.nextDouble(0.0, 0.705), Random.nextDouble(0.0, 0.705), Random.nextDouble(0.0, 0.705), 0.705)
            else -> ColorRGBa.BLACK
        }
    }

    fun display(planetList: ArrayList<MyPlanet>, useBlackHoles: Boolean, paused: Boolean): Int {
        if (!isBlackHole) {
            if (planetList.size > 1) {
                setForce(planetList, position.x, position.y)
                accel = Vector2(force.x / mass, force.y / mass)
                velocity += accel
            }
            if (!paused) {
                position += velocity
            }
            for (p in planetList) {
                if (p != this) {
                    hit = checkCollision(this, p, useBlackHoles)
                }
                if (hit) {
                    val newVelX = (mass * velocity.x + p.mass * p.velocity.x) / (mass + p.mass)
                    val newVelY = (mass * velocity.y + p.mass * p.velocity.y) / (mass + p.mass)
                    val newPosX = (position.x + p.position.x) / 2
                    val newPosY = (position.y + p.position.y) / 2
                    val newR = r + p.r
                    if (!p.isBlackHole) {
                        planetList.add(MyPlanet(drawer, newVelX, newVelY, newPosX, newPosY, newR, false))
                        planetList.remove(this)
                        planetList.remove(p)
                    } else {
                        planetList.remove(this)
                    }
                }
            }
        }
        if (position.x - r < 0) {
            position = Vector2(r, position.y)
            velocity = Vector2(velocity.x - 1, velocity.y)
        }
        if (position.x + r > drawer.width) {
            position = Vector2(drawer.width - r, position.y)
            velocity = Vector2(velocity.x - 1, velocity.y)
        }
        if (position.y - r < 0) {
            position = Vector2(position.x, r)
            velocity = Vector2(velocity.x, -1.0)
        }
        if (position.y + r > drawer.height) {
            position = Vector2(position.x, drawer.height - r)
            velocity = Vector2(velocity.x, -1.0)
        }
        if (r >= criticalMass && !isBlackHole) {
            createBlackHole(planetList)
        }
        if (isBlackHole) {
            lineAnimation()
        }
        drawer.fill = c
        drawer.circle(position, 2 * r)

        return planetList.size
    }

    private fun setForce(planetList: ArrayList<MyPlanet>, x: Double, y: Double) {
        totalX = 0.0
        totalY = 0.0
        totalDist = 0.0
        totalMass = 0.0
        force *= 0.0
        for (p in planetList) {
            if (p != this) {
                totalX += p.position.x - x
                totalY += p.position.y - y
                totalDist += distance(x, y, p.position.x, p.position.y)
                if (!p.isBlackHole) {
                    totalMass += p.mass
                } else {
                    totalMass = 100000.0
                }
            }
        }
        force = Vector2(totalX / (planetList.size - 1), totalY / (planetList.size - 1))  //  -1 because we aren't including THIS planet in the calculation
        avgDist = when {
            avgDist > .1 -> totalDist / (planetList.size - 1)
            else -> .1
        }
        avgMass = totalMass / (planetList.size - 1)
        force = force.normalized
        force *= ((gravity * mass * avgMass) / (avgDist * avgDist))
    }

    private fun checkCollision(a: MyPlanet, b: MyPlanet, useBlackHoles: Boolean): Boolean {
        var hit = false
        if (distance(a.position.x, a.position.y, b.position.x, b.position.y) < a.r + b.r && useBlackHoles) {
            hit = true
        }
        return hit
    }

    private fun createBlackHole(planetList: ArrayList<MyPlanet>) {
        val bh = MyPlanet(drawer, 0.0, 0.0, position.x, position.y, 10.0, true)
        planetList.add(bh)
        planetList.remove(this)
    }

    private fun lineAnimation() {
        rotateAngle += .05
        drawer.apply {
            stroke = ColorRGBa.BLACK
            fill = null
            for (i in 0 until 22) {
                pushModel()
                translate(position.x, position.y)
                rotate(i * 10.0)
                rotate(rotateAngle)
                lineStrip(listOf(Vector2(0.0, 0.0), Vector2(15.0, -25.0), Vector2(55.0, -25.0), Vector2(70.0, 0.0)))
                popModel()
            }
            fill = ColorRGBa.BLACK
            stroke = null
        }
    }
}