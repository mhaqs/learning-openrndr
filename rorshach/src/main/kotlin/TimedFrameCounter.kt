class TimedFrameCounter(on: Boolean, duration: Double = 0.0, private var completeBehavior: () -> Unit): FrameCounter() {
    private var durationFrameCount = duration
    private var isCompleted = false
    private var isOn = on
    
    fun on(duration: Double = 0.0) {
        isOn = true
        if (duration > 0)
            durationFrameCount = duration
    }
    fun off() {
        isOn = false
    }

    override fun step() {
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
            durationFrameCount > 0 -> (count / durationFrameCount).coerceIn(0.0, 1.0)
            else -> 0.0
        }
    }
}