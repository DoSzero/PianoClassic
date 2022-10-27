package com.har.pianoclassic.view.fragment

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.har.pianoclassic.interfaces.FragmentListener
import com.har.pianoclassic.R
import com.har.pianoclassic.utils.CustomToastUtils

class HomeFragment : Fragment(), View.OnClickListener {

    private var startGame: Button? = null
    private var settings: Button? = null
    private var exit: Button? = null
    private var toast: CustomToastUtils? = null
    private var fragmentListener: FragmentListener? = null
    private var song: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val toastView = inflater.inflate(R.layout.custom_toast, container, false)

        toast = CustomToastUtils(toastView, requireActivity().applicationContext)

        startGame = view.findViewById(R.id.btn_start)
        settings = view.findViewById(R.id.btn_settings)
        exit = view.findViewById(R.id.btn_exit)

        startGame!!.setOnClickListener(this)
        settings!!.setOnClickListener(this)
        exit!!.setOnClickListener(this)

        song = MediaPlayer.create(activity, R.raw.canon)
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

    override fun onResume() {
        super.onResume()
        song!!.start()
    }

    override fun onPause() {
        super.onPause()
        song!!.stop()
    }

    override fun onClick(v: View) {
        if (v === startGame) {
            fragmentListener!!.changePage(1)
        } else if (v === settings) {
            fragmentListener!!.changePage(4)
        } else if (v === exit) {
            fragmentListener!!.closeApplication()
        }
    }
}