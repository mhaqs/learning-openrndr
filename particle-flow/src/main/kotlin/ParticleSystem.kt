import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import java.util.*

internal class ParticleSystem(position: Vector2) {
    private var particles: ArrayList<Particle> = ArrayList()
    private var origin: Vector2 = Vector2(position.x, position.y)

    fun addParticle() {
        particles.add(Particle(origin))
    }

    fun run(drawer: Drawer) {
        for (p in particles.reversed()) {
            p.run(drawer)
            if (p.isDead) {
                particles.remove(p)
            }
        }
    }
}