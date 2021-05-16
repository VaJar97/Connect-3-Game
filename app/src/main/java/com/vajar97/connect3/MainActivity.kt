package com.vajar97.connect3


import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // yellow = 0, red = 1, empty = -1
    var color: Int = 0  // flag after turn, red will be first
    var count: IntArray = intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1)

    private val winningPositions: Array<IntArray> = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4 , 6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_next.setOnClickListener {
            playAgain()
        }

        setTextColor()
    }

    fun clicked(view: View) {
        val resImage: ImageView = view as ImageView
        resImage.translationY=-1500f                    // hide image above frame
        resImage.setImageResource(setColor())
        resImage.noteTag()                              // write current cell & check status
        makeSound()
        resImage.animate().translationYBy(1500f).duration=500   // set image
        setTextColor()
    }

    private fun makeSound() {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.fart)
        mediaPlayer.start()
    }

    private fun setColor(): Int {
        return when(color) {
            0 -> {
                color = 1
                R.drawable.red
            }
            1 -> {
                color = 0
                R.drawable.yellow
            }
            else -> {
                color = 0
                R.drawable.yellow
            }
        }
    }

    private fun ImageView.noteTag() {
        val tap = (tag.toString()).toInt()
        count[tap] = color

        for(position in winningPositions) {
            if (count[position[0]] != -1 && count[position[0]] == count[position[1]] && count[position[1]]==count[position[2]]) {
                when (count[position[0]]) {
                    0 -> finishGame("Yellow")
                    1 -> finishGame("Red")
                }
            }
        }
    }

    private fun playAgain() {
        top_layout.visibility = View.INVISIBLE
        text_turn.visibility = View.VISIBLE
        for (i in 0..grid.childCount) {
            val image = grid.getChildAt(i) as ImageView?
            image?.setImageDrawable(null)
        }
    }

    private fun finishGame(name: String) {
        text_turn.visibility = View.INVISIBLE
        top_layout.visibility = View.VISIBLE
        text_result.text = "$name is win!"
        color = 0
        count = intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1)
    }

    private fun setTextColor() {
        val word: Spannable
        if (color == 1) {
            word = SpannableString("Yellow")
            word.setSpan(ForegroundColorSpan(Color.YELLOW), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            word = SpannableString("Red")
            word.setSpan(ForegroundColorSpan(Color.RED), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text_turn.text = word
        text_turn.append(" turn")
        text_turn.animate().alpha(1f).duration = 2000
    }
}


