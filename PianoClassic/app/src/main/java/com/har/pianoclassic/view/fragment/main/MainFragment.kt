package com.har.pianoclassic.view.fragment.main

import android.annotation.SuppressLint
import com.har.pianoclassic.SettingsPrefSaver
import android.content.Context
import com.har.pianoclassic.R
import com.har.pianoclassic.interfaces.FragmentListener
import android.os.Bundle
import com.har.pianoclassic.view.fragment.main.MainPresenter.IMainFragment
import android.view.View.OnTouchListener
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.SoundPool
import android.hardware.SensorEvent
import android.graphics.*
import android.hardware.Sensor
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.har.pianoclassic.utils.CustomToastUtils
import java.lang.ClassCastException

class MainFragment : Fragment(), IMainFragment, View.OnClickListener, OnTouchListener,

    SensorEventListener {
    
    private var fragmentListener: FragmentListener? = null
    private var mainPresenter: MainPresenter? = null
    private var startButton: Button? = null
    private var ivCanvas: ImageView? = null
    private var canvas: Canvas? = null

    private var bitmap: Bitmap? = null
    private var score: Button? = null
    private var health: Button? = null
    private var initiated: Boolean? = null
    private var settingsPrefSaver: SettingsPrefSaver? = null
    private var toast: CustomToastUtils? = null
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private lateinit var accelerometerReading: FloatArray
    private lateinit var magnetometerReading: FloatArray
    private var soundPool: SoundPool? = null
    private var pianoA = 0
    private var pianoB = 0
    private var pianoC = 0
    private var pianoD = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val toastView = inflater.inflate(R.layout.custom_toast, container, false)

        toast = CustomToastUtils(toastView, requireActivity().applicationContext)
        ivCanvas = view.findViewById(R.id.ivCanvas)
        startButton = view.findViewById(R.id.start_btn)
        score = view.findViewById(R.id.score)
        health = view.findViewById(R.id.health)

        settingsPrefSaver = SettingsPrefSaver(this.activity)
        startButton?.setOnClickListener(this)
        health?.setOnClickListener(this)
        ivCanvas?.setOnTouchListener(this)

        mainPresenter = MainPresenter(this, toast, settingsPrefSaver)
        initiated = false
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        accelerometerReading = FloatArray(3)
        magnetometerReading = FloatArray(3)
        soundPool = SoundPool.Builder().setMaxStreams(10).build()
        pianoA = soundPool!!.load(this.activity, R.raw.piano_a, 1)
        pianoB = soundPool!!.load(this.activity, R.raw.piano_b, 1)
        pianoC = soundPool!!.load(this.activity, R.raw.piano_c, 1)
        pianoD = soundPool!!.load(this.activity, R.raw.piano_d, 1)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            fragmentListener = context
        } else {
            throw ClassCastException("$context must implement FragmentListener")
        }
    }

    override fun updateCanvas(canvas: Canvas?) {
        this.canvas = canvas
        ivCanvas!!.postInvalidate()
    }

    override fun initiateCanvas(bitmap: Bitmap?, canvas: Canvas?) {
        this.bitmap = bitmap
        ivCanvas!!.setImageBitmap(this.bitmap)
        this.canvas = canvas
    }

    @SuppressLint("SetTextI18n")
    override fun updateScore(score: Int) {
        this.score!!.text = score.toString()
    }

    @SuppressLint("SetTextI18n")
    override fun updateHealth(health: Int) {
        if (health > 0) {
            this.health!!.text = health.toString()
        }
    }

    override fun gameOver(score: Int) {
        fragmentListener!!.setScore(score)
        fragmentListener!!.changePage(2)
    }

    override fun registerSensor() {
        if (accelerometer != null) {
            sensorManager!!.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (magnetometer != null) {
            sensorManager!!.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun unregisterSensor() {
        sensorManager!!.unregisterListener(this)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun playNotes(pos: Int) {
        when (pos) {
            1 -> {
                soundPool!!.play(pianoA, 1f, 1f, 1, 0, 1f)
            }
            2 -> {
                soundPool!!.play(pianoB, 1f, 1f, 1, 0, 1f)
            }
            3 -> {
                soundPool!!.play(pianoC, 1f, 1f, 1, 0, 1f)
            }
            4 -> {
                soundPool!!.play(pianoD, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    override fun onClick(v: View) {
        if (v === startButton) {
            if (!initiated!!) {
                mainPresenter!!.initiateCanvas(ivCanvas)
                startButton!!.visibility = View.INVISIBLE
                score!!.visibility = View.VISIBLE
                health!!.visibility = View.VISIBLE
                initiated = true
            }
        } else if (v === health) {
            for (i in 0 until settingsPrefSaver!!.keyHealth) {
                mainPresenter!!.removeHealth()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mainPresenter!!.checkScore(PointF(event.x, event.y))
        }
        return true
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerReading = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerReading = event.values.clone()
        }

        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(
            rotationMatrix, null, accelerometerReading, magnetometerReading
        )

        val orientationAngles = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        val roll = orientationAngles[2]
        if (roll > 0.8f || roll < -0.8f) {
            mainPresenter!!.checkSensor(roll)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}