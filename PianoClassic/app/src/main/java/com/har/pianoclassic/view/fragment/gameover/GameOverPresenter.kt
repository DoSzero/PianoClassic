package com.har.pianoclassic.view.fragment.gameover

import android.annotation.SuppressLint
import com.har.pianoclassic.model.DBHandler
import com.har.pianoclassic.model.Score
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GameOverPresenter(
    private var score: Int,
    private var ui: IGameOverFragment,
    private var db: DBHandler?
) {
    private var isSaved = false
    fun loadData() {
        ui.setScore(score)
    }

    @SuppressLint("SimpleDateFormat")
    fun backToMenu(playerName: String?) {
        if (!isSaved) {
            val date = Calendar.getInstance().time
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
            val strDate = dateFormat.format(date)
            val score = Score(0, score, playerName, strDate)
            //db!!.addRecord(score)
        }
        ui.changePage(0)
    }

    @SuppressLint("SimpleDateFormat")
    fun playAgain(playerName: String?) {
        if (!isSaved) {
            val date = Calendar.getInstance().time
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
            val strDate = dateFormat.format(date)
            val score = Score(0, score, playerName, strDate)
            //db!!.addRecord(score)
        }
        ui.changePage(1)
    }

    interface IGameOverFragment {
        fun setScore(score: Int)
        fun changePage(page: Int)
    }
}