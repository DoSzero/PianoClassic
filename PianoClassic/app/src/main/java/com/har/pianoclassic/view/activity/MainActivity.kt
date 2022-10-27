package com.har.pianoclassic.view.activity

import com.har.pianoclassic.R
import com.har.pianoclassic.interfaces.FragmentListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.fragment.app.FragmentManager
import com.har.pianoclassic.databinding.ActivityMainBinding
import com.har.pianoclassic.view.fragment.gameover.GameOverFragment
import com.har.pianoclassic.view.fragment.HomeFragment
import com.har.pianoclassic.view.fragment.main.MainFragment
import com.har.pianoclassic.view.fragment.setings.SettingsFragment

class MainActivity : AppCompatActivity(), FragmentListener {

    private var bind: ActivityMainBinding? = null
    private var fragmentManager: FragmentManager? = null
    private var mainFragment: MainFragment? = null
    private var gameOverFragment: GameOverFragment? = null
    private var homeFragment: HomeFragment? = null
    private var settingsFragment: SettingsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        val view: View = bind!!.root
        setContentView(view)

        fragmentManager = this.supportFragmentManager
        homeFragment = HomeFragment()
        mainFragment = MainFragment()
        gameOverFragment = GameOverFragment.newInstance(0)

        settingsFragment = SettingsFragment()
        changePage(0)
    }

    override fun changePage(i: Int) {
        val ft = fragmentManager!!.beginTransaction()
        when (i) {
            0 -> {
                ft.replace(R.id.fragment_container, homeFragment!!).addToBackStack(null)
            }
            1 -> {
                ft.replace(R.id.fragment_container, mainFragment!!).addToBackStack(null)
            }
            2 -> {
                ft.replace(R.id.fragment_container, gameOverFragment!!).addToBackStack(null)
            }
            4 -> {
                ft.replace(R.id.fragment_container, settingsFragment!!).addToBackStack(null)
            }
        }
        ft.commit()
    }

    override fun setScore(score: Int) {
        gameOverFragment = GameOverFragment.newInstance(score)
    }

    override fun closeApplication() {
        moveTaskToBack(true)
        finish()
    }
}