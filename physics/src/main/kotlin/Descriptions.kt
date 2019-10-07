import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.text.Writer

class Descriptions(drawer: Drawer) {  //  this class simply provides the descriptions when you mouse over each menu box

    var t: SpecialText
    private val writer = Writer(drawer)

    init {
        drawer.fill = ColorRGBa.BLACK
        t = SpecialText(drawer, 14.0)
    }

    fun projectilesDescription() {
        //textAlign(LEFT)
        writer.apply {
            text("Observe the path of a projectile", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 400.0, 160.0)
            text("as it is launched at different", 400.0, 178.0)
            text("angles, initial velocities, and", 400.0, 196.0)
            text("heights. Also change the height", 400.0, 214.0)
            text("of the landing site and examine", 400.0, 232.0)
            text("the projectile's air time, maximum", 400.0, 250.0)
            text("height, and displacement.", 400.0, 268.0)
        }
        t.apply {
            createDivision("Assumptions", " ", 505.0, 340.0)
            createBullet("No wind resistance", 400.0, 360.0)
            createBullet("Force applied to center of mass", 400.0, 378.0)
            createBullet("Acceleration due to gravity = -.1", 400.0, 396.0)
            createBullet("No energy lost to heat", 400.0, 414.0)
        }
    }

    fun inclineDescription() {
        //textAlign(LEFT)
        writer.apply {
            text("Examine the physics behind a ", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 40.0, 160.0)
            text("block sliding down an incline.", 40.0, 178.0)
            text("Change the block's mass, the angle", 40.0, 196.0)
            text("of the incline, and the friction.", 40.0, 214.0)
            text("A visual diagram breaks down the", 40.0, 232.0)
            text("force of gravity into its parallel", 40.0, 250.0)
            text("and perpendicular components", 40.0, 268.0)
            text("with respect to the incline.", 40.0, 286.0)
        }
        t.apply {
            createDivision("Assumptions", " ", 505.0, 340.0)
            createBullet("No wind resistance", 40.0, 360.0)
            createBullet("Force applied to center of mass", 40.0, 378.0)
            createBullet("No energy lost to heat", 40.0, 396.0)
        }
    }

    fun springsDescription() {
        //textAlign(LEFT)
        writer.apply {
            text("Observe the motion of a spring", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 40.0, 160.0)
            text("and change its K value, resting", 40.0, 178.0)
            text("position, and mass. Also examine", 40.0, 196.0)
            text("the effects of these variables", 40.0, 214.0)
            text("on the restorative force of the", 40.0, 232.0)
            text("spring. Drag blocks to move them.", 40.0, 250.0)
        }
        t.apply {
            createDivision("Assumptions", " ", 505.0, 340.0)
            createBullet("No wind resistance", 40.0, 360.0)
            createBullet("Force applied to center of mass", 40.0, 378.0)
            createBullet("No energy lost to heat", 40.0, 396.0)
            createBullet("Zero friction", 40.0, 414.0)
            createBullet("Vectors represented as +/- values", 40.0, 432.0)
            createBullet("Spring itself has no mass", 40.0, 450.0)
        }
    }

    fun curvedDescription() {
        //textAlign(LEFT)
        writer.apply {
            text("Watch as motion along a curved", 40.0, 160.0)
            text("path changes angular velocity", 40.0, 178.0)
            text("and centripetal acceleration.", 40.0, 196.0)
            text("Adjust the speed of rotation and", 40.0, 214.0)
            text("use the mouse to swing the object.", 40.0, 232.0)
            text("The \"tether\" uses verlet integration.", 40.0, 250.0)
        }
        t.apply {
            createDivision("Assumptions", " ", 505.0, 340.0)
            createBullet("No wind resistance", 395.0, 360.0)
            createBullet("Force applied to center of mass", 395.0, 378.0)
            createBullet("No energy lost to heat", 395.0, 396.0)
            createBullet("No gravity", 395.0, 414.0)
            createBullet("Angular velocity is a pseudovector", 395.0, 432.0)
            createBullet("r = distance from center of circle", 395.0, 450.0)
        }
    }

    fun gravityDescription() {
        //textAlign(LEFT)
        writer.apply {
            text("Examine the effects of Newton's", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 40.0, 160.0)
            text("Law of Universal Gravitation on", 40.0, 178.0)
            text("circular moving bodies. Bodies", 40.0, 196.0)
            text("can be added to the system by", 40.0, 214.0)
            text("clicking. Optionally, bodies", 40.0, 232.0)
            text("form larger masses upon collision", 40.0, 250.0)
            text("until a black hole is created.", 40.0, 268.0)
        }
        t.apply {
            createDivision("Assumptions", " ", 505.0, 340.0)
            createBullet("No wind resistance", 40.0, 360.0)
            createBullet("Force applied to center of mass", 40.0, 378.0)
            createBullet("No energy lost to heat", 40.0, 396.0)
            createBullet("The black hole is just for fun", 40.0, 414.0)
            createBullet("G = 5.0 Ã— 10^-8", 40.0, 432.0)
        }
    }

    fun thermoDescription() {
        //textAlign(LEFT)
        writer.apply {
            text("Observe the relationship between", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 40.0, 160.0)
            text("pressure, volume, and temperature", 40.0, 178.0)
            text("in a closed chamber full of", 40.0, 196.0)
            text("particles. Particle velocity and", 40.0, 214.0)
            text("mass are adjustable, as is the mass", 40.0, 232.0)
            text("of the chamber's lid. These", 40.0, 250.0)
            text("variables determine internal", 40.0, 268.0)
            text("energy and kinetic energy.", 40.0, 286.0)
        }
        t.apply {
            createDivision("Assumptions", " ", 505.0, 340.0)
            createBullet("No wind resistance", 40.0, 360.0)
            createBullet("Force applied to center of mass", 40.0, 378.0)
            createBullet("No energy lost to heat", 40.0, 396.0)
            createBullet("Constant number of particles", 40.0, 414.0)
            createBullet("Ideal gas constant = 8.31", 40.0, 432.0)
            createBullet("Acceleration due to gravity = -.07", 40.0, 450.0)
            createBullet("100% elastic particle collisions", 40.0, 468.0)
            createBullet("Particles do not interact", 40.0, 486.0)
        }
    }
}