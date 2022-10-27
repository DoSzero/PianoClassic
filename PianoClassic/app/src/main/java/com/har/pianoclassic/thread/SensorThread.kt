package com.har.pianoclassic.thread

class SensorThread(
    private var threadHandler: ThreadHandler,
    private var isLeft: Boolean
    ) : Thread() {

    private var roll = 0f
    private var counter = 0

    fun changeRoll(roll: Float) {
        this.roll = roll
    }

    override fun run() {
        while (counter < 2000) {
            if (checkValid()) {
                threadHandler.addSensorScore()
                return
            }
            try {
                sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        threadHandler.popSensorOut()
        return
    }

    private fun checkValid(): Boolean {
        if (isLeft) {
            if (roll < -0.8f) return true
        } else if (!isLeft) {
            if (roll > 0.8f) {
                return true
            }
        }
        return false
    }
}