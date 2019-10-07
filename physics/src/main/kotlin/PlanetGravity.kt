import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class PlanetGravity(private val drawer: Drawer) {

    private var mouseTimer = 0
    private var keyTimer = 0
    private var buttonTimer = 0
    private var numPlanets = 2
    private var planetList= ArrayList<MyPlanet>()
    private var paused = false
    private var useBlackHoles = false
    private var isOverButton = false
    private var t = SpecialText(drawer,14.0)

    init {
        planetList = ArrayList()
        for (i in 0 until numPlanets) {
            planetList.add(MyPlanet(drawer,0.0, 0.0, i*300+100.0, 340.0, 20.0, false))
        }
    }

    fun display() {
        keyTimer++
        if (keyPressed && key == "p" && keyTimer > 7) {
            paused = paused == false
            keyTimer = 0
        }
        for ( i in 0 until numPlanets) {
            val p = planetList[i]
            numPlanets = p.display(planetList, useBlackHoles, paused)
        }
        mouseTimer++
        if (mousePressed && mouseTimer > 7 && !isOverButton) {
            planetList.add(MyPlanet(drawer,0.0, 0.0, mouseX, mouseY, 30.0, false))
            numPlanets++
            mouseTimer = 0
        }
        drawer.apply {
            fill = colorRGBaFrom256(200, 206, 250)
            stroke = null
            rectangle(560.0, 40.0, 55.0, 35.0)//, 10)
            rectangle(510.0, 80.0, 150.0, 35.0)//, 10)
            rectangle(560.0, 120.0, 55.0, 35.0)//, 10)
            fill = ColorRGBa.BLACK
        }
        with(writer) {
            text("Reset", "file:data/fonts/Palatino-Linotype.ttf", 14.0, 572.0, 61.0)
            text("Toggle Collisions", 530.0, 101.0)
            text("Back", 573.0, 141.0)
            text("Click to add bodies", 530.0, 180.0)
            text("Press P to pause", 540.0, 200.0)
        }
        t.createVector("F", 40.0, 40.0)
        writer.text("=", 60.0, 40.0)
        t.createDivision("mass 1 Ã— mass 2", "distance^2", 130.0, 40.0)
        if (mouseX in 560.0..615.0 && mouseY >= 40 && mouseY <= 75 && mousePressed) {
            numPlanets = 0
            planetList.clear()
        }
        buttonTimer++
        if (mouseX in 510.0..660.0 && mouseY >= 80 && mouseY <= 115) {
            isOverButton = true
            if (mousePressed && buttonTimer > 7) {
                buttonTimer = 0
                useBlackHoles = !useBlackHoles
            }
        }
        else {
            isOverButton = false
        }
        if (mouseX in 560.0..615.0 && mouseY >= 120 && mouseY <= 155 && mousePressed) {
            ms.currentScreen = 999
        }
    }
}