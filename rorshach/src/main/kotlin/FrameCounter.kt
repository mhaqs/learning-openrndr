import kotlin.math.PI
import kotlin.math.sin

open class FrameCounter {
    protected var count: Int = 0

    init {
        if (isNotInitialized)
            println("FrameCounter is not initialized.")
    }

    fun resetCount(cnt: Int = 0) {
        count = cnt
    }

    open fun step() {
        count += 1
    }

    fun mod(divisor: Int): Int {
        return count % divisor
    }

    /**
     * Returns ratio from 0 to 1 according to current frame count and given frequency per second.
     * @param frequency {number} - frequency per second
     */
    fun getCycleProgressRatio(frequency: Double): Double {
        return ((frequency * count) % frameRate) / frameRate
    }

    /**
     * Returns sine value (from 0 to 1)according to
     * current frame count and given frequency per second.
     * @param frequency {number} - frequency per second
     */
    fun sin(frequency: Double = 1.0): Double {
        return kotlin.math.sin(getCycleProgressRatio(frequency) * (2 * PI))
    }

    companion object {
        @JvmStatic var isNotInitialized: Boolean = true
        @JvmStatic var frameRate = 0.0

        @JvmStatic fun initializeStatic(frameRates: Double) {
            frameRate = frameRates
            isNotInitialized = false
        }
    }
}