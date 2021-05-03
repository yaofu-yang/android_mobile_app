package edu.neu.mad_sea.yaofuyang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import edu.neu.mad_sea.yaofuyang.tic_tac_toe_classes.*

/**
 * Create Board fragment
 * This will contain 9 buttons
 * tictactoe xml should be replaced with the fragment container.
 */
private const val TAG = "TicTacToeActivity"
class DisplayTicTacToeActivity : AppCompatActivity(), BoardFragment.Callbacks {
    /**
     * UI Components:
     * - playerOneScore: number of games won by player one
     * - playerTwoScore: number of games won by player two
     * - playerStatus: displays the current overall game leader
     * - resetGame: button used to reset the entire game, player scores, and playerStatus
     */
    private lateinit var playerOneScore: TextView
    private lateinit var playerTwoScore: TextView
    private lateinit var playerStatus: TextView
    private lateinit var resetGame : Button
    private lateinit var gameController: GameControllerModel
    private lateinit var ackButton: Button

    // Initialize Player 1 View Model
    private val playerOne by lazy {
        ViewModelProvider(this).get(PlayerOneViewModel::class.java)
    }

    // Initialize Player 2 View Model
    private val playerTwo by lazy {
        ViewModelProvider(this).get(PlayerTwoViewModel::class.java)
    }

    // Initialize Player Status view model
    private val statusModel by lazy {
        ViewModelProvider(this).get(PlayerStatusViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.tic_tac_toe_title)
        setContentView(R.layout.activity_tic_tac_toe)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val boardFragment = BoardFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, boardFragment)
                .commit()
        }

        // Set views for player scores, status, and reset game button.
        playerOneScore = findViewById(R.id.playerOneScore)
        playerTwoScore = findViewById(R.id.playerTwoScore)
        playerStatus = findViewById(R.id.playerStatus)
        resetGame = findViewById(R.id.resetGame)
        gameController = GameControllerModel()
        updatePlayerScore()

        ackButton = findViewById(R.id.ack_button)

        ackButton.setOnClickListener{
            // Display all of the acknowledgements
            val intent = Intent(this, DisplayTicAckActivity::class.java)
            startActivity(intent)
        }
    }

    // A listener for one of the game boxes being selected
    override fun onBoxSelected(v: View?) {
        Log.d(TAG, "TicTacToeBox: $String")
        if ((v as Button).text.toString() != getString(R.string.empty_string)) {
            return
        }

        // Below finds the button ID that was clicked and updates the game state.
        val buttonID = v.getResources().getResourceEntryName(v.getId())
        val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()

        // Player markings depending on turn.
        if (gameController.isActivePlayer) {
            v.text = getString(R.string.x_mark)
            v.setTextColor(ContextCompat.getColor(this, R.color.goldenTainoi))
            gameController.gameState[gameStatePointer] = 0
        } else {
            v.text = getString(R.string.o_mark)
            v.setTextColor(ContextCompat.getColor(this, R.color.babyBlue))
            gameController.gameState[gameStatePointer] = 1
        }
        gameController.incrementRound()

        // Checks for conditions at the end of a game round.
        if (gameController.checkWinner()) {
            if (gameController.isActivePlayer) {
                playerOne.incrementScore()
                doEnd(getString(R.string.player_one_wins))
                updatePlayerScore()
            } else {
                playerTwo.incrementScore()
                doEnd(getString(R.string.player_two_wins))
                updatePlayerScore()
            }
        } else if (gameController.roundCount == 9) {
            doEnd(getString(R.string.players_tie))
        } else {
            gameController.toggleActivePlayer()
        }

        // Set the player status if one player has more wins than the other.
        statusModel.updateStatus(playerOne.score, playerTwo.score)
        playerStatus.text = statusModel.status

        // Reset game
        resetGame.setOnClickListener {
            playerOne.score = 0
            playerTwo.score = 0
            statusModel.clearStatus()
            playerStatus.text = statusModel.status
            updatePlayerScore()
            playAgain()
        }
    }

    // Updates text of the player score TextViews.
    fun updatePlayerScore() {
        playerOneScore.text = "${playerOne.score}"
        playerTwoScore.text = "${playerTwo.score}"
    }

    // Starts a new round with a clean game board.
    fun playAgain() {
        gameController.resetCount()
        gameController.resetActivePlayer()
        gameController.resetGameState()
        val boardFragment = BoardFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, boardFragment)
            .commit()

    }

    // Performs behavior under end condition.
    fun doEnd(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        playAgain()
    }
}