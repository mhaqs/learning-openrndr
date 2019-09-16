import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import java.util.ArrayList
import kotlin.random.Random

// An ArrayList is used to manage the list of Particles

internal class CrazyParticleSystem(num: Int, position:Vector2) {
    private var particles: ArrayList<Particle> = ArrayList()
    // An origin point for where particles are birthed
    private var origin: Vector2 = Vector2(position.x, position.y)

    init {
        for (i in 0..num) {
            particles.add(Particle(origin));    // Add "num" amount of particles to the arraylist
        }
    }

    fun run(drawer: Drawer) {
        // Cycle through the ArrayList backwards, because we are deleting while iterating
        for (p in particles.reversed()) {
            p.run(drawer)
            if (p.isDead) {
                particles.remove(p)
            }
        }
    }

    fun addParticle() {
        // Add either a Particle or CrazyParticle to the system
        val p = if (Random.nextInt(0, 2) == 0) {
            Particle(origin)
        }
        else {
            CrazyParticle(origin)
        }
        particles.add(p)
    }
}