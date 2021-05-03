package edu.neu.mad_sea.yaofuyang.tic_tac_toe_classes

import androidx.lifecycle.ViewModel
import edu.neu.mad_sea.yaofuyang.App
import edu.neu.mad_sea.yaofuyang.R

class PlayerStatusViewModel : ViewModel() {
    var status = App.getRes().getString(R.string.empty_string)

    fun clearStatus() {
        status = App.getRes().getString(R.string.empty_string)
    }

    fun updateStatus(scoreOne: Int, scoreTwo: Int) {
        status = if (scoreOne > scoreTwo) {
            App.getRes().getString(R.string.player_one_leads)
        } else if (scoreTwo > scoreOne) {
            App.getRes().getString(R.string.player_two_leads)
        } else {
            App.getRes().getString(R.string.empty_string)
        }
    }
}