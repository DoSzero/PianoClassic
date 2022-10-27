package com.har.pianoclassic.view.fragment.main

import com.har.pianoclassic.SettingsPrefSaver
import com.har.pianoclassic.utils.CustomToastUtils
import kotlin.Throws
import android.graphics.*
import android.util.Log
import android.widget.*
import com.har.pianoclassic.thread.MainThread
import com.har.pianoclassic.thread.PlayThread
import com.har.pianoclassic.thread.SensorThread
import com.har.pianoclassic.thread.ThreadHandler
import java.util.*

class MainPresenter(
    private var ui: IMainFragment,
    toast: CustomToastUtils?,
    settingsPrefSaver: SettingsPrefSaver?
) {
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var paint: Paint? = null
    private var transparent: Paint? = null
    private var threadHandler: ThreadHandler = ThreadHandler(this)
    private var threads: LinkedList<MainThread> = LinkedList()
    private var sensorThreads: LinkedList<SensorThread> = LinkedList()
    private var playThread: PlayThread? = null
    private var viewSize: PointF? = null
    private var endPointY = 0f
    private var endPointX = 0f
    private var score = 0
    private var health = 0
    private var settingsPrefSaver: SettingsPrefSaver?

    //CountDownTimer toastCountDown;
    private var toast: CustomToastUtils?

    init {
        this.toast = toast
        this.settingsPrefSaver = settingsPrefSaver
    }

    fun initiateCanvas(ivCanvas: ImageView?) {
        if (ivCanvas != null) {
            bitmap = Bitmap.createBitmap(ivCanvas.width, ivCanvas.height, Bitmap.Config.ARGB_8888)
        }
        canvas = bitmap?.let { Canvas(it) }

        paint = Paint()
        paint!!.color = Color.BLACK
        transparent = Paint()
        transparent!!.color = Color.TRANSPARENT
        transparent!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        if (ivCanvas != null) {
            viewSize = PointF(ivCanvas.width.toFloat(), ivCanvas.height.toFloat())
        }
        ui.initiateCanvas(bitmap, canvas)
        endPointY = viewSize!!.y / 5
        endPointX = viewSize!!.x / 4
        score = 0
        ui.updateScore(score)

        health =settingsPrefSaver!!.keyHealth

        ui.updateHealth(health)
        if (ivCanvas != null) {
            playThread = PlayThread(
                PointF(
                    ivCanvas.width.toFloat(), ivCanvas.height.toFloat()
                ), threadHandler
            )
        }
        playThread!!.start()
        ui.registerSensor()
        Log.d("TAG", "initiateCanvas: " + viewSize!!.y)
    }

    fun drawRect(point: PointF) {
        canvas!!.drawRect(point.x, point.y, point.x + endPointX, point.y + endPointY, paint!!)
        ui.updateCanvas(canvas)
    }

    fun clearRect(point: PointF) {
        canvas!!.drawRect(point.x, point.y, point.x + endPointX, point.y + endPointY, transparent!!)
        ui.updateCanvas(canvas)
    }

    @Throws(InterruptedException::class)
    fun generate(pos: Int) {
        if (pos < 5) {
            val newThread = MainThread(threadHandler, pos, viewSize)
            newThread.start()
            threads.addLast(newThread)
        } else if (pos == 5) {
            toast!!.setText("Tilt Left!")
            val sensorThread = SensorThread(threadHandler, true)
            sensorThread.start()
            sensorThreads.addLast(sensorThread)
            toast!!.show()
        } else if (pos == 6) {
            toast!!.setText("Tilt Right!")
            val sensorThread = SensorThread(threadHandler, false)
            sensorThread.start()
            sensorThreads.addLast(sensorThread)
            toast!!.show()
        }
    }

    fun stop() {
        playThread!!.stopThread()
    }

    fun checkScore(tap: PointF) {
        for (i in threads.indices) {
            threads[i].checkScore(tap)
        }
    }

    fun checkSensor(roll: Float) {
        for (i in sensorThreads.indices) {
            sensorThreads[i].changeRoll(roll)
        }
    }

    fun popOut() {
        threads.removeFirst()
    }

    fun popSensorOut() {
        sensorThreads.removeFirst()
    }

    fun addScore(pos: Int) {
        score++
        ui.playNotes(pos)
        ui.updateScore(score)
    }

    fun addSensorScore() {
        score++
        ui.updateScore(score)
    }

    fun removeHealth() {
        health--
        if (health == 0) {
            playThread!!.stopThread()
            ui.gameOver(score)
            ui.unregisterSensor()
        }
        ui.updateHealth(health)
    }

    interface IMainFragment {
        fun updateCanvas(canvas: Canvas?)
        fun initiateCanvas(bitmap: Bitmap?, canvas: Canvas?)
        fun updateScore(score: Int)
        fun updateHealth(health: Int)
        fun gameOver(score: Int)
        fun registerSensor()
        fun unregisterSensor()
        fun playNotes(pos: Int)
    }
}