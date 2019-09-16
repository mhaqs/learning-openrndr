import kotlin.math.PI
import kotlin.math.sin

open class FrameCounter {
    protected var count: Int

    init {
        if (isNotInitialized)
            println("FrameCounter is not initialized.")
        this.count = 0
    }

    fun resetCount(count: Int = 0) {
        this.count = count
    }

    open fun step() {
        this.count += 1
    }

    fun mod(divisor: Int): Int {
        return this.count % divisor
    }

    /**
     * Returns ratio from 0 to 1 according to current frame count and given frequency per second.
     * @param frequency {number} - frequency per second
     */
    fun getCycleProgressRatio(frequency: Int): Double {
        return ((frequency * this.count) % frameRate) / frameRate
    }

    /**
     * Returns sine value (from 0 to 1)according to
     * current frame count and given frequency per second.
     * @param frequency {number} - frequency per second
     */
    fun sin(frequency: Int = 1): Double {
        return sin(this.getCycleProgressRatio(frequency) * (2 * PI))
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