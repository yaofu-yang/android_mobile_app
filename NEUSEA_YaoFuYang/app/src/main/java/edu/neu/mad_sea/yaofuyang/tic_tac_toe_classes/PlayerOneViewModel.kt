package edu.neu.mad_sea.yaofuyang.tic_tac_toe_classes

import androidx.lifecycle.ViewModel

private const val TAG = "PlayerOneViewModel"
class PlayerOneViewModel : ViewModel() {

    // Keeps track of the current player score.
    var score = 0;

    // Returns the player score as an Int
    val getScore : Int
        get() = score

    // Increments the player score.
    fun incrementScore() {
        score++
    }

    // Resets the player score to 0
    fun resetScore() {
        score = 0
    }

    fun updateScore(value: Int) {
        score = value
    }
}