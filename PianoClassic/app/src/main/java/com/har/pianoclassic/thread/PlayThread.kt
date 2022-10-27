package com.har.pianoclassic.thread

import android.graphics.PointF
import java.util.*

class PlayThread(viewSize: PointF, threadHandler: ThreadHandler) : Thread() {

    private var viewSize: PointF
    private var isStopped: Boolean
    private var threadHandler: ThreadHandler
    private var random: Random = Random()
    private var array: IntArray

    init {
        this.viewSize = viewSize
        this.threadHandler = threadHandler
        isStopped = false
        array = IntArray(42)
        for (i in 0..9) {
            array[i] = 1
        }
        for (i in 10..19) {
            array[i] = 2
        }
        for (i in 20..29) {
            array[i] = 3
        }
        for (i in 30..39) {
            array[i] = 4
        }
        array[40] = 5
        array[41] = 6
    }

    fun stopThread() {
        isStopped = true
    }

    override fun run() {
        var exclude = random.nextInt(6) + 1
        while (!isStopped) {
            val arrayExclude: IntArray = when (exclude) {
                1 -> {
                    intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                }
                2 -> {
                    intArrayOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                }
                3 -> {
                    intArrayOf(20, 21, 22, 23, 24, 25, 26, 27, 28, 29)
                }
                4 -> {
                    intArrayOf(30, 31, 32, 33, 34, 35, 36, 37, 38, 39)
                }
                5 -> {
                    intArrayOf(40)
                }
                else -> {
                    intArrayOf(41)
                }
            }
            val num = getRandomWithExclusion(1, 41, *arrayExclude)
            try {
                sleep((viewSize.y * 0.40265277).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            threadHandler.generate(array[num])
            exclude = array[num]
        }
        return
    }

    private fun getRandomWithExclusion(start: Int, end: Int, vararg exclude: Int): Int {
        var random = start + random.nextInt(end - start + 1 - exclude.size)
        for (ex in exclude) {
            if (random < ex) {
                break
            }
            random++
        }
        return random
    }
}