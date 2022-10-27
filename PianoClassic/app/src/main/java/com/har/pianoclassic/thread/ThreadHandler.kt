package com.har.pianoclassic.thread

import android.graphics.PointF
import android.os.Handler
import android.os.Message
import com.har.pianoclassic.view.fragment.main.MainPresenter

open class ThreadHandler(private var mainPresenter: MainPresenter) : Handler() {
    override fun handleMessage(message: Message) {
        when (message.what) {
            DRAW_RECT -> {
                val param = message.obj as PointF
                mainPresenter.drawRect(param)
            }
            CLEAR_RECT -> {
                val param = message.obj as PointF
                mainPresenter.clearRect(param)
            }
            ADD_SCORE -> {
                val param = message.obj as Int
                mainPresenter.addScore(param)
            }
            POP_OUT -> {
                mainPresenter.popOut()
            }
            GENERATE -> {
                val param = message.obj as Int
                try {
                    mainPresenter.generate(param)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            REMOVE_HEALTH -> {
                mainPresenter.removeHealth()
            }
            ADD_SENSOR_SCORE -> {
                mainPresenter.addSensorScore()
            }
            POP_SENSOR_OUT -> {
                mainPresenter.popSensorOut()
            }
        }
    }

    fun drawRect(point: PointF?, isClicked: Boolean) {
        if (isClicked) return
        val msg = Message()
        msg.what = DRAW_RECT
        msg.obj = point
        sendMessage(msg)
    }

    fun clearRect(point: PointF?) {
        val msg = Message()
        msg.what = CLEAR_RECT
        msg.obj = point
        sendMessage(msg)
    }

    fun addScore(pos: Int) {
        val msg = Message()
        msg.what = ADD_SCORE
        msg.obj = pos
        sendMessage(msg)
    }

    fun addSensorScore() {
        val msg = Message()
        msg.what = ADD_SENSOR_SCORE
        sendMessage(msg)
    }

    fun removeHealth() {
        val msg = Message()
        msg.what = REMOVE_HEALTH
        sendMessage(msg)
    }

    fun popOut() {
        val msg = Message()
        msg.what = POP_OUT
        sendMessage(msg)
    }

    fun popSensorOut() {
        val msg = Message()
        msg.what = POP_SENSOR_OUT
        sendMessage(msg)
    }

    fun generate(pos: Int) {
        val msg = Message()
        msg.what = GENERATE
        msg.obj = pos
        sendMessage(msg)
    }

    companion object {
        protected const val DRAW_RECT = 1
        protected const val CLEAR_RECT = 2
        protected const val ADD_SCORE = 3
        protected const val POP_OUT = 4
        protected const val GENERATE = 5
        protected const val REMOVE_HEALTH = 6
        protected const val ADD_SENSOR_SCORE = 7
        protected const val POP_SENSOR_OUT = 8
    }
}