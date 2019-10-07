import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.math.PI
import kotlin.random.Random

class ShowSystem(private val drawer: Drawer) {
    private val col = 30
    private val row = 30
    private val p = Array(col) { Array(row) { Particle(drawer) } }

    private var timer = 0  //  variables to determine the system's state
    private var avgKE = 0.0
    private var avgPressure = 0.0
    private var avgVelocity = 0.0
    private var initialAvgKE = 0.0
    private var initialMass = 0.0
    private var lidHeight = 0.0
    private var lidLength = 260
    var r = 0.0
    private var temp = 0.0
    private var totalVelocity = 0.0
    private var velocityMag = 0.0
    private var volume = 0.0

    var t: SpecialText = SpecialText(drawer, 14.0)

    init {
        for (i in 0 until col) {
            for (j in 0 until row) {
                p[i][j] = Particle(drawer, Random.nextDouble(130.0, 360.0), Random.nextDouble(220.0, 580.0))
                totalVelocity += p[i][j].velocity.length
            }
        }
        avgVelocity = totalVelocity / (col * row)
        initialMass = (p[0][0].r * p[0][0].r * PI)
        avgKE = .5 * initialMass * avgVelocity * avgVelocity  //  KE = 1/2 m v^2
        initialAvgKE = avgKE
    }

    fun display() {
        var totalParticleMomentum = 0.0
        var lidPos = p[0][0].displayOtherFeatures(totalParticleMomentum)
        velocityMag = p[0][0].velocitySlider()
        r = p[0][0].rSlider()
        totalVelocity = 0.0
        for (i in 0 until col) {
            for (j in 0 until row) {
                totalParticleMomentum += p[i][j].display(lidPos, velocityMag, r)
                totalVelocity += p[i][j].velocity.length
            }
        }
        lidPos = p[0][0].displayOtherFeatures(totalParticleMomentum)
        avgVelocity = totalVelocity / (col * row)
        timer++
        if (timer > 50) {
            //  pressure = (mass * velocity^2)/volume * (numberOfParticles / 2)
            avgPressure = ((r * r * PI * avgVelocity * avgVelocity) / volume) * ((col * row) / 2)
            lidHeight = 580 - lidPos
            volume = lidLength * lidHeight
            //  temperature = PV / nR
            temp = (avgPressure * volume) / (col * row * 8.31)
            avgKE = (r * r * PI * avgVelocity * avgVelocity) / 2
            timer = 0
        }

        drawer.apply {
           fill = ColorRGBa(0.784, 0.807, 0.980)
           stroke = null
           rectangle(560.0, 560.0, 55.0, 35.0)//, 10.0)
           fill = ColorRGBa.BLACK
        }
        t.apply {
            createSubscript("P", "i", 440.0, 200.0)
            createSubscript("V", "i", 452.0, 200.0)
            createSubscript("P", "f", 480.0, 200.0)
            createSubscript("V", "f", 495.0, 200.0)
        }
        with(writer) {
            text("       =         = nRT", 440.0, 200.0)
            val prettyPressure = avgPressure * 100 / 100
            text("P = $prettyPressure", 440.0, 230.0)
            text("V = $volume", 440.0, 260.0)
            text("n = 900", 440.0, 290.0)
            text("R = 8.31 (arbitrary)", 440.0, 320.0)
            val prettyTemp = temp * 100 / 100
            text("T = $prettyTemp", 440.0, 350.0)
            val prettyPV = avgPressure * volume * 100 / 100
            text("PV = $prettyPV", 440.0, 380.0)
            text("internal energy U = 3/2 PV = " + (3 / 2) * avgPressure * volume, 440.0, 410.0)
            text("average KE = 1/2 m v^2 = $avgKE", 440.0, 440.0)
            text(velocityMag.toString(), 510.0, 80.0)
            text((r * r * PI).toString(), 510.0, 120.0)
            text("Back", 558.0 + writer.textWidth("Back") / 2, 582.0)
        }
        drawer.apply {
            stroke = ColorRGBa.BLACK
            lineSegment(90.0, 200.0, 100.0, 200.0)
            lineSegment(90.0, 510.0, 100.0, 510.0)
            stroke = null
        }
        writer.text("max", 55.0, 204.0)
        writer.text("min", 55.0, 514.0)

        if (mouseX in 560.0..615.0 && mouseY >= 560 && mouseY <= 595 && mousePressed) {  //  BACK button
            ms.currentScreen = 999
        }
    }
}