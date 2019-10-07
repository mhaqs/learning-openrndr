import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.shape.shape

class MainScreen(private var drawer: Drawer, private val tSize: Double) {
    private val boxTitle = arrayOf(
            "Projectiles", "Friction and Incline", "Springs", "Curved Motion", "Gravity", "Thermodynamics"
    )
    var currentScreen = 999
    private var numBoxes = boxTitle.size
    private var clicked = ArrayList<Boolean>(numBoxes)
    private var mouseIsHovering = ArrayList<Boolean>(numBoxes)
    private var d = Descriptions(drawer)

    init {
        for (i in boxTitle.indices) {
            clicked.add(false)
            mouseIsHovering.add(false)
        }
    }

    private fun menuBox(x: Double, y: Double, w: Double, h: Double, label: String): Boolean {  //  method to draw each menu box
        var selected = false
        if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY < y + h) {
            drawer.fill = ColorRGBa(200.0, 206.0, 250.0)
            drawer.stroke = null
            drawer.rectangle(380.0, 130.0, 250.0, 450.0)//, 10)
            val triangle = shape {
                contour {
                    moveOrLineTo(x + w, y+ h /2)
                    moveOrLineTo(381.0, y+ h /2 - 22)
                    moveOrLineTo(381.0, y+ h /2 + 22)
                }
            }
            drawer.shape(triangle)
            if (mousePressed) {
                selected = true
            }
        }
        else {
            drawer.fill = null
        }
        drawer.stroke = ColorRGBa.BLACK
        drawer.rectangle(x, y, w, h)

        drawer.fill = ColorRGBa.BLACK
        writer.text(label, "file:data/fonts/Palatino-Linotype.ttf", 24.0, x + w/2, y + h/2 + 8)

        return selected
    }

    private fun hovering(x: Double, y: Double, w: Double, h: Double): Boolean {  //  method to show the descriptions if mouse over
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY < y + h
    }

    fun display() {
        //textAlign(CENTER)
        writer.text("Physics", "file:data/fonts/Palatino-Linotype.ttf", 30.0, 340.0, 70.0)
        for (i in 0 until numBoxes) {
            mouseIsHovering[i] = hovering(100.0, i * 80 + 130.0, 250.0, 50.0)
            clicked[i] = menuBox(100.0, i*80 + 130.0, 250.0, 50.0, boxTitle[i])
            if (clicked[i]) {
                currentScreen = i
            }
        }
        if (mouseIsHovering[0]) {
            d.projectilesDescription()
        }
        if (mouseIsHovering[1]) {
            d.inclineDescription()
        }
        if (mouseIsHovering[2]) {
            d.springsDescription()
        }
        if (mouseIsHovering[3]) {
            d.curvedDescription()
        }
        if (mouseIsHovering[4]) {
            d.gravityDescription()
        }
        if (mouseIsHovering[5]) {
            d.thermoDescription()
        }
        //textAlign(CENTER)
        writer.text("TrevPhil", "file:data/fonts/Palatino-Linotype.ttf", tSize, 630.0, 665.0)
        //textAlign(LEFT)
    }
}
