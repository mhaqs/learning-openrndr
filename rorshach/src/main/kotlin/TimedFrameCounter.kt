class TimedFrameCounter(on: Boolean, duration: Double = 0.0, private var completeBehavior: () -> Unit) {
    private var durationFrameCount = duration
    private var isCompleted = false
    private var isOn = on
    private var count: Int = 0

    init {
        if (isNotInitialized)
            println("FrameCounter is not initialized.")
    }

    fun on(duration: Double = 0.0) {
        isOn = true
        if (duration > 0)
            durationFrameCount = duration
    }
    fun off() {
        isOn = false
    }

    fun resetCount(cnt: Int = 0) {
        count = cnt
    }

    fun step() {
        if (!isOn)
            return
        count += 1
        if (count > durationFrameCount) {
            isCompleted = true
            isOn = false
            completeBehavior()
        }
    }

    fun getProgressRatio(): Double {
        return when {
            durationFrameCount > 0 -> (count / durationFrameCount)
            else -> 0.0
        }
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