package com.har.pianoclassic.view.fragment.gameover

import android.annotation.SuppressLint
import com.har.pianoclassic.model.DBHandler
import android.content.Context
import com.har.pianoclassic.R
import com.har.pianoclassic.interfaces.FragmentListener
import android.os.Bundle
import com.har.pianoclassic.view.fragment.gameover.GameOverPresenter.IGameOverFragment
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.har.pianoclassic.utils.CustomToastUtils
import java.lang.ClassCastException

class GameOverFragment : Fragment(), IGameOverFragment, View.OnClickListener {

    private var fragmentListener: FragmentListener? = null
    private var score: TextView? = null
    private var playAgain: Button? = null
    private var backMenu: Button? = null
    private var gameOverPresenter: GameOverPresenter? = null
    private var db: DBHandler? = null
    private var toast: CustomToastUtils? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gameover, container, false)
        val toastView = inflater.inflate(R.layout.custom_toast, container, false)

        toast = CustomToastUtils(toastView, requireActivity().applicationContext)
        score = view.findViewById(R.id.score)
        playAgain = view.findViewById(R.id.play_again)
        backMenu = view.findViewById(R.id.back_menu)

        db = DBHandler(this.activity)

        gameOverPresenter = GameOverPresenter(
            this.requireArguments().getInt("score", 0), this, db
        )

        playAgain!!.setOnClickListener(this)
        backMenu!!.setOnClickListener(this)

        gameOverPresenter!!.loadData()
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

    @SuppressLint("SetTextI18n")
    override fun setScore(score: Int) {
        this.score!!.text = "Твой счет: $score"
    }

    override fun changePage(page: Int) {
        fragmentListener!!.changePage(page)
    }

    override fun onClick(v: View) {
        val playerName = "Player"
        if (v === playAgain) {
            gameOverPresenter!!.playAgain(playerName)
        } else if (v === backMenu) {
            gameOverPresenter!!.backToMenu(playerName)
        }
    }

    companion object {
        fun newInstance(score: Int): GameOverFragment {
            val fragment = GameOverFragment()
            val args = Bundle()
            args.putInt("score", score)
            fragment.arguments = args
            return fragment
        }
    }
}