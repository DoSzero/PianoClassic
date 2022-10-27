package com.har.pianoclassic.thread

import android.graphics.PointF

open class MainThread(
    private var threadHandler: ThreadHandler,
    private var position: Int,
    private var viewSize: PointF?
) : Thread() {
    private var start: PointF = PointF()
    private var isClicked = false

    init {
        setPoint()
    }

    private fun setPoint() {
        when (position) {
            1 -> {
                start[0f] = -viewSize!!.y / 4
            }
            2 -> {
                start[viewSize!!.x / 4] = -viewSize!!.y / 4
            }
            3 -> {
                start[viewSize!!.x / 2] = -viewSize!!.y / 4
            }
            4 -> {
                start[viewSize!!.x / 4 * 3] = -viewSize!!.y / 4
            }
        }
    }

    override fun run() {
        while (check(start.y)) {
            try {
                sleep(16)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            threadHandler.clearRect(PointF(start.x, start.y))
            start[start.x] = start.y + viewSize!!.y * 0.005f
            threadHandler.drawRect(PointF(start.x, start.y), isClicked)
        }
        threadHandler.popOut()
        if (!isClicked) {
            threadHandler.removeHealth()
        }
        return
    }

    fun checkScore(tap: PointF) {
        if (isClicked) return
        if (tap.x >= start.x && tap.x <= start.x + viewSize!!.x / 4) {
            if (tap.y >= start.y && tap.y <= start.y + viewSize!!.y / 4) {
                threadHandler.addScore(position)
                threadHandler.clearRect(PointF(start.x, start.y))
                isClicked = true
            }
        }
    }

    private fun check(y: Float): Boolean {
        return y < viewSize!!.y
    }
}