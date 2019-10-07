import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

class ShowSprings(private val drawer: Drawer) {
    private val numCoils = 10  //  horizontal spring variables
    private var kScale = 70.0
    private var restScale = 210.0
    private var massScale = 250.0
    private var w = 0.0
    private var time = 0.0
    private var restPos = 50.0
    private var initialPos = 300.0
    private var A = abs(initialPos - restPos)
    private var K = .7
    private var dampen = .99
    private var mass = 100.0
    private var displacement = 0.0
    private var restorativeForce = 0.0
    var mouseIsDragging = false

    private var vh = 0.0  //  vertical spriing variables
    private var vkScale = 400.0
    private var vrestScale = 400.0
    private var vmassScale = 380.0
    private var vtime = 0.0
    private var vacceleration = 2.0
    private var vmass = 100.0
    private var vdisplacement = 0.0
    private var vrestorativeForce = 0.0
    private var vrestPos = vmass*vacceleration
    private var vinitialPos = 250
    private var vA = abs(vinitialPos - vrestPos)
    private var vK = .7
    private var vdampen = .99
    var vmouseIsDragging = false

    fun displayHorizontal() {
        //rectMode(CENTER)
        kScale = slider(kScale, 70.0, 500.0, "k Value")  //  sliding scales and their relative values
        restScale = slider(restScale, 170.0, 500.0, "Rest Position")
        massScale = slider(massScale, 270.0, 500.0, "Mass of Block")
        // this follows the format: lowerBound + ((whateverScale + 40 - x)/80)*(upperBound - lowerBound)
        K = .1 + ((kScale + 40 - 70)/80)*(.9 - .1)
        restPos = -100 + ((restScale + 40 - 170)/80)*(200 - -100)
        mass = 10 + ((massScale + 40 - 270)/80)*(500 - 10)
        drawer.apply {
            fill = colorRGBaFrom256(200, 206, 250)  //  BACK button
            stroke = null
            rectangle(50.0, 400.0, 55.0, 35.0)//, 10)
            fill = ColorRGBa.BLACK
            stroke = ColorRGBa.BLACK
        }
        //textAlign(CENTER)  //  screen text, equations, and real-time values
        with(writer) {
            text(K.toString(), 70, 470)
            text(restPos.toString(), 170, 470)
            text(mass.toString(), 270, 470)
            text("0", 340, 473)
            //textAlign(CORNER)
            text("Displacement = A cos( âˆš(k / mass) Ã— time) = ","file:data/fonts/Palatino-Linotype.ttf", 14.0, 30.0, 200.0)
            val prettyVD = vdisplacement * 100 / 100
            text("(vertical spring) $prettyVD", 30, 225)
            val prettyD = displacement * 100 / 100
            text("(horizontal spring) $prettyD", 30, 250)
            text("Restorative force = -k Ã— displacement = ", 30, 290)
            val prettyVR = vrestorativeForce * 100 / 100
            text("(vertical spring) " + -prettyVR, 30, 315)
            val prettyR = restorativeForce * 100 / 100
            text("(horizontal spring) $prettyR", 30, 340)
            text("Click and drag to move blocks", 40, 650)
            text("Back", 50 - textWidth("Back") / 2, 404.0)
            text("Given one-dimensional motion, assume", "file:data/fonts/Palatino-Linotype.ttf", 12.0,40.0, 40.0)
            text("that positive values represent vector", 40, 65)
            text("directions to the right/downwards and", 40, 90)
            text("negative values to the left/upwards", 40, 115)
        }
        if (mouseX in 22.5..77.5 && mouseY >= 382.5 && mouseY <= 417.5 && mousePressed) {  //  BACK button
            ms.currentScreen = 999
        }

        if (!mouseIsDragging) {  //  the amplitude decreases when the user isn't dragging blocks
            time++
            if (abs(A) < .1) {
                A = 0.0
            } else {
                A *= dampen
            }
            displacement = A* cos(sqrt(K/mass) *time)
            restorativeForce = -1 * K * displacement
        }

        drawer.apply {
            pushModel()
            translate(340.0, 580.0)
            rectangle(0.0, 10.0, 680.0, 20.0)
            lineSegment(0.0, 0.0, 0.0, -100.0)
            lineSegment(restPos, 60.0, restPos, -60.0)

            text("Rest Position", restPos - writer.textWidth("Rest Position") / 2, 75.0)
            //rectMode(CORNER)
            w = displacement + restPos
            fill = null
            for ( i in 0 until numCoils) {
            //  draws the spring (for aesthetics only)
                lineStrip(listOf(Vector2(i * (w / numCoils), -15.0), Vector2(i * (w / numCoils) + 5, -45.0), Vector2((i + 1) * (w / numCoils) - 5, -45.0), Vector2((i + 1) * (w / numCoils), -15.0)))
            }
            //rectMode(CENTER)

            translate(restPos, 0.0)
            fill = ColorRGBa.BLACK
            if (mouseX >= displacement + restPos - 25 + 340 && mouseX <= displacement + restPos + 25 + 340 && mouseY >= -50 + 600 && mouseY <= 0 + 600 && mousePressed) {
                mouseIsDragging = true
            }
            if (mouseIsDragging) {  //  when the user drags the blocks
                time = 0.0
                displacement = mouseX - 340 - restPos
                A = displacement
            }
            rectangle(displacement, -25.0, 50.0, 50.0)  //  draw the block
            popModel()
        }
    }

    fun displayVertical() {  //  this is exactly the same as displayHorizontal() except inverted visually (x, y) -> (y, x)
        //rectMode(CENTER)
        vkScale = slider(vkScale, 400.0, 40.0, "k Value")  //  sliding scales and their relative values
        vrestScale = slider(vrestScale, 400.0, 80.0, "Rest Position")
        vmassScale = slider(vmassScale, 400.0, 120.0, "Mass of Block")
        // this follows the format: lowerBound + ((whateverScale + 40 - x)/80)*(upperBound - lowerBound) (or does it?)
        vK = -.9 + ((vkScale + 40 - 480)/80)*(.1 - .9)
        vrestPos = 300 + ((vrestScale + 40 - 480)/80)*(550 - 300) - vmass/30  //  the mass slightly affects the resting position (aesthetics only)
        vmass = -500 + ((vmassScale + 40 - 480)/80)*(10 - 500)

        drawer.fill = ColorRGBa.BLACK
        drawer.stroke = ColorRGBa.BLACK
        //textAlign(CENTER)
        writer.text((-vK).toString(), 335, 40)
        writer.text(vrestPos.toString(), 335, 80)
        writer.text((-vmass).toString(), 335, 120)
        //textAlign(CORNER)

        if (!vmouseIsDragging) {
            vtime++
            if (abs(vA) < .1) {
                vA = 0.0
            } else {
                vA *= vdampen
            }
            vdisplacement = vA*cos(sqrt(vK/vmass)*vtime)
            vrestorativeForce = -1 * vK * vdisplacement
        }

        drawer.lineSegment(490.0, vrestPos, 610.0, vrestPos)
        writer.text("Rest", 620.0, vrestPos-10)
        writer.text("Position", 620.0, vrestPos+10)
        //rectMode(CORNER)
        vh = vdisplacement + vrestPos
        drawer.fill = null
        for ( i in 0 until numCoils) {
            drawer.lineStrip(listOf(Vector2(540.0, i*(vh/numCoils)), Vector2(555.0, i*(vh/numCoils)+5), Vector2(585.0, (i+1)*(vh/numCoils)-5), Vector2(540.0, (i+1)*(vh/numCoils))))
        }
        //rectMode(CENTER)

        drawer.translate(0.0, vrestPos)
        drawer.fill = ColorRGBa.BLACK
        if (mouseY >= vdisplacement+vrestPos-25 && mouseY <= vdisplacement+vrestPos+25 && mouseX >= 550-25 && mouseX <= 550+25 && mousePressed) {
            vmouseIsDragging = true
        }
        if (vmouseIsDragging) {
            vtime = 0.0
            vdisplacement = mouseY - vrestPos
            vA = vdisplacement
        }
        drawer.rectangle(550.0, vdisplacement, 50.0, 50.0)
        //rectMode(CORNER)
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
            fill = colorRGBaFrom256(150, 150, 150)
            circle(a, y + 1, 10.0)
        }
        return aShadow
    }
}