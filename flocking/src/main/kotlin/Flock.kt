import org.openrndr.draw.Drawer
import java.util.*

internal class Flock {
    private var boids: ArrayList<Boid> = ArrayList()

    fun run(drawer: Drawer) {
        for (b in boids) {
            b.run(drawer, boids)  // Passing the entire list of boids to each boid individually
        }
    }

    fun addBoid(b: Boid) {
        boids.add(b)
    }
}