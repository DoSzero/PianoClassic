package com.har.pianoclassic.view.fragment.setings

import com.har.pianoclassic.SettingsPrefSaver
import com.har.pianoclassic.model.DBHandler

class SettingsPresenter(
    private var ui: ISettingsFragment,
    private var settingsPrefSaver: SettingsPrefSaver?,
    private var db: DBHandler?
) {
    private var health = 0
    fun loadHealth() {
        health = settingsPrefSaver!!.keyHealth
        ui.loadHealth(health)
    }

    @JvmName("setHealth1")
    fun setHealth(count: Int) {
        when (count) {
            1 -> {
                health = 1
                settingsPrefSaver!!.saveHealth(health)
                ui.loadHealth(health)
            }
            3 -> {
                health = 3
                settingsPrefSaver!!.saveHealth(health)
                ui.loadHealth(health)
            }
            5 -> {
                health = 5
                settingsPrefSaver!!.saveHealth(health)
                ui.loadHealth(health)
            }
        }
    }

    interface ISettingsFragment {
        fun loadHealth(count: Int)
    }
}