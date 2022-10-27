package com.har.pianoclassic.view.fragment.setings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.har.pianoclassic.model.DBHandler
import com.har.pianoclassic.R
import com.har.pianoclassic.SettingsPrefSaver
import com.har.pianoclassic.interfaces.FragmentListener
import com.har.pianoclassic.view.fragment.setings.SettingsPresenter.ISettingsFragment

class SettingsFragment : Fragment(), View.OnClickListener, ISettingsFragment {

    private var btnToHome: Button? = null
    private var healthCount: RadioGroup? = null
    private var health1: RadioButton? = null
    private var health3: RadioButton? = null
    private var health5: RadioButton? = null

    private var db: DBHandler? = null
    private var settingsPrefSaver: SettingsPrefSaver? = null
    private var settingsPresenter: SettingsPresenter? = null
    private var fragmentListener: FragmentListener? = null

    //private var binding: SettingsPageBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding = SettingsPageBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        val view = inflater.inflate(R.layout.settings_page, container, false)

        healthCount = view.findViewById(R.id.health_count)
        health1 = view.findViewById(R.id.health_1)
        health3 = view.findViewById(R.id.health_3)
        health5 = view.findViewById(R.id.health_5)

        btnToHome = view.findViewById(R.id.btnToHome1)


        db = DBHandler(this.activity)
        settingsPrefSaver = SettingsPrefSaver(this.activity)
        settingsPresenter = SettingsPresenter(this, settingsPrefSaver, db)

        health1!!.setOnClickListener(this)
        health3!!.setOnClickListener(this)
        health5!!.setOnClickListener(this)
        btnToHome!!.setOnClickListener(this)


        settingsPresenter!!.loadHealth()
        return view
    }

    override fun onClick(v: View) {
        if (v === health1) {
            settingsPresenter!!.setHealth(1)
        } else if (v === health3) {
            settingsPresenter!!.setHealth(3)
        } else if (v === health5) {
            settingsPresenter!!.setHealth(5)
        } else if (v === btnToHome){
            // nav
            //fragmentListener!!.changePage(3)
        }
    }

    override fun loadHealth(count: Int) {
        when (count) {
            1 -> {
                health1!!.isChecked = true
            }
            3 -> {
                health3!!.isChecked = true
            }
            5 -> {
                health5!!.isChecked = true
            }
        }
    }
}