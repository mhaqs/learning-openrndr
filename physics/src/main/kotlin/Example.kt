import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.draw.FontImageMap
import org.openrndr.text.Writer

const val tSize = 18.0

lateinit var t: SpecialText

lateinit var ms: MainScreen
lateinit var projectiles: Projectiles
lateinit var myIncline: MyIncline
lateinit var showSprings: ShowSprings
lateinit var pg: PlanetGravity
lateinit var ss: ShowSystem
val p = ArrayList<Point>(30)
lateinit var link: Link

var mouseX: Double = 0.0
var mouseY: Double = 0.0
var mousePressed: Boolean = false
var keyPressed: Boolean = false
var key: String = "n"
lateinit var writer: Writer

class Example : Program() {
    lateinit var font: FontImageMap
    override fun setup() {
        font = FontImageMap.fromUrl("file:data/fonts/Palatino-Linotype.ttf", tSize)
        drawer.fill = ColorRGBa.BLACK
        drawer.fontMap = font
        t = SpecialText(drawer, tSize)
        drawer.stroke = ColorRGBa.BLACK

        writer = Writer(drawer)

        ms = MainScreen(drawer, tSize)
        projectiles = Projectiles(drawer)
        myIncline = MyIncline(drawer)
        showSprings = ShowSprings(drawer)
        pg = PlanetGravity(drawer)
        ss = ShowSystem(drawer)

        for (i in 0 until 30) {
            p.add(Point(drawer))
        }
        link = Link(drawer, p)

        mouse.buttonUp.listen {
            mousePressed = false
            if (showSprings.mouseIsDragging) {
                showSprings.mouseIsDragging = false
            }
            if (showSprings.vmouseIsDragging) {
                showSprings.vmouseIsDragging = false
            }
        }
        
        mouse.buttonDown.listen {
            mousePressed = true
        }
        
        keyboard.keyDown.listen {
            keyPressed = true
            key = it.name
        }

        keyboard.keyUp.listen {
            keyPressed = false
        }
    }

    override fun draw() {
        mouseX = application.cursorPosition.x
        mouseY = application.cursorPosition.y
        
        drawer.background(ColorRGBa.WHITE)
        drawer.fill = ColorRGBa.BLACK
        if (ms.currentScreen == 999) {
            ms.display()
        }
        if (ms.currentScreen == 0) {
            projectiles.display()
        }
        if (ms.currentScreen == 1) {
            myIncline.display()
        }
        if (ms.currentScreen == 2) {
            showSprings.displayHorizontal()
            showSprings.displayVertical()
        }
        if (ms.currentScreen == 3) {
            link.display()
        }
        if (ms.currentScreen == 4) {
            pg.display()
        }
        if (ms.currentScreen == 5) {
            ss.display()
        }
    }
}

fun main() {
    Application.run(Example(), configuration {
        width = 680
        height = 680
        title = "Physics"
    })
}