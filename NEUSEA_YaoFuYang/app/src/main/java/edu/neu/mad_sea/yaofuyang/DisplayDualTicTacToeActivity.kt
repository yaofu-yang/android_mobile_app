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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes.DisplayTicDualAckActivity
import edu.neu.mad_sea.yaofuyang.tic_tac_toe_classes.*
import kotlinx.android.synthetic.main.activity_display_dual_tic_tac_toe.*

private const val TAG = "DualTicTacToeActivity"
class DisplayDualTicTacToeActivity : AppCompatActivity(), View.OnClickListener {
    /**
     * UI Components:
     * - playerOneScore: number of games won by player one
     * - playerTwoScore: number of games won by player two
     * - playerStatus: displays the current overall game leader
     * - resetGame: button used to reset the entire game, player scores, and playerStatus
     */
    private var playerOneName = "Player 1"
    private var playerTwoName = "Player 2"

    private lateinit var playerOneTitle : TextView
    private lateinit var playerTwoTitle : TextView
    private lateinit var playerOneScore: TextView
    private lateinit var playerTwoScore: TextView
    private lateinit var playerStatus: TextView

    private lateinit var joinButton : Button
    private lateinit var resetGame : Button
    private lateinit var ackButton: Button
    private var buttons = arrayOfNulls<Button>(9)

    private lateinit var gameController: GameControllerModel
    private val database = FirebaseDatabase.getInstance()
    private var activePlayer = ""
    private var gameState = arrayListOf<Int>(9)

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
        setContentView(R.layout.activity_display_dual_tic_tac_toe)

        // Initialize the buttons and set the on click listeners.
        for (i in buttons.indices) {
            val buttonID = "${getString(R.string.button_id_prefix)}$i"
            val resourceID = resources.getIdentifier(buttonID, "id", packageName)
            buttons[i] = findViewById(resourceID)
            buttons[i]!!.setOnClickListener(this)  // Passes View.OnClickListener
        }
        // Creates the event listeners for the following:
        // Game board, player scores, winning status, name
        setEventListeners()

        // Set views for text views
        playerOneTitle = findViewById(R.id.playerOne)
        playerTwoTitle = findViewById(R.id.playerTwo)
        playerOneScore = findViewById(R.id.playerOneScore)
        playerTwoScore = findViewById(R.id.playerTwoScore)
        playerStatus = findViewById(R.id.playerStatus)

        // Set views for buttons
        joinButton = findViewById(R.id.join_game)
        resetGame = findViewById(R.id.resetGame)
        ackButton = findViewById(R.id.ack_button)

        gameController = GameControllerModel()

        // Listener to clicks to the join game button
        joinButton.setOnClickListener {
            val occupancyRef = database.getReference("occupancy")
            val activePlayerRef = database.getReference("activePlayer")
            val playerOneRef = database.getReference("players/player1")
            val playerTwoRef = database.getReference("players/player2")
            val occupancyListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val occupancy = snapshot.value as Long
                    val name = FirebaseAuth.getInstance().currentUser?.displayName
                    if (name != null) {
                        when (occupancy) {
                            0L -> {
                                playerOneRef.setValue(name)
                                activePlayerRef.setValue(name)
                                occupancyRef.setValue(1L)
                            }
                            1L -> {
                                playerTwoRef.setValue(name)
                                occupancyRef.setValue(2L)
                            }
                            else -> Toast.makeText(baseContext, "Room is full", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(baseContext, "Joined as : $name", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, "Failed to read value", Toast.LENGTH_SHORT).show()
                }
            }
            occupancyRef.addListenerForSingleValueEvent(occupancyListener)
        }

        // Listener to reset the game to original state, with same players.
        resetGame.setOnClickListener {
            playerOne.score = 0
            playerTwo.score = 0
            statusModel.clearStatus()
            database.getReference("status").setValue(statusModel.status)
            resetPlayerScores()
            playAgain()
            activePlayer = playerOneName
        }

        ackButton.setOnClickListener{
            // Display all of the acknowledgements
            val intent = Intent(this, DisplayTicDualAckActivity::class.java)
            startActivity(intent)
        }
    }

    // When a box on the game board is clicked.
    override fun onClick(v: View?) {
        Log.d(TAG, "TicTacToeBox: $String")
        if ((v as Button).text.toString() != getString(R.string.empty_string)) {
            return
        }

        // Below finds the button ID that was clicked and updates the game state.
        val buttonID = v.getResources().getResourceEntryName(v.getId())
        val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()

        // Player markings depending on turn.
        val name = FirebaseAuth.getInstance().currentUser?.displayName
        if (activePlayer == name) {
            if (name == playerOneName) {
                updateGameState(gameStatePointer, 0)
            } else if (name == playerTwoName) {
                updateGameState(gameStatePointer, 1)
            }
            gameController.incrementRound()
            endConditions()
        }
    }

    // Starts a new round with a clean game board.
    private fun playAgain() {
        gameController.resetCount()
        resetActivePlayer()
        resetGameState()
    }

    private fun resetPlayerScores() {
        val scoreRef = database.getReference("scores")
        scoreRef.child("player1").setValue(0)
        scoreRef.child("player2").setValue(0)
    }

    private fun resetActivePlayer() {
        // val gameStateRef = database.getReference("gameState")
       database.getReference("activePlayer").setValue(playerOneName)
    }

    private fun resetGameState() {
        val gameStateRef = database.getReference("gameState")
        gameStateRef.setValue(listOf(2, 2, 2, 2, 2, 2, 2, 2, 2))
    }

    private fun updateGameState(index: Int, newValue: Int) {
        val gameStateRef = database.getReference("gameState")
        gameStateRef.child("$index").setValue(newValue)
    }


    // TODO: Review
    // Sets all of the listeners to maintain synchronization of game elements.
    private fun setEventListeners() {
        setGameStateListener()
        setPlayerNameListener()
        setPlayerScoreListener()
        setWinningStatusListener()
        setActivePlayerListener()
    }


    // Listener to update local active player variable based on change to shared variable.
    private fun setActivePlayerListener() {
        val activeRef = database.getReference("activePlayer")
        val activeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val player = snapshot.value as String
                activePlayer = player
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to read value", Toast.LENGTH_SHORT).show()
            }
        }
        activeRef.addValueEventListener(activeListener)
    }

    // Listener to update winning status shared by both players.
    private fun setWinningStatusListener() {
        val statusRef = database.getReference("status")
        val statusListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.value
                playerStatus.text = "$status"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to read value", Toast.LENGTH_SHORT).show()
            }
        }
        statusRef.addValueEventListener(statusListener)
    }

    // Listener for updates to the player scores.
    private fun setPlayerScoreListener() {
        val playerScoreRef = database.getReference("scores")
        val scoreListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val p1Score = snapshot.child("player1").value
                val p2Score = snapshot.child("player2").value
                playerOneScore.text = "$p1Score"
                playerTwoScore.text = "$p2Score"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to read value", Toast.LENGTH_SHORT).show()
            }
        }
        playerScoreRef.addValueEventListener(scoreListener)
    }

    // Listener for updates to the player names
    private fun setPlayerNameListener() {
        val playerNamesRef = database.getReference("players")
        val namesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val p1Name = snapshot.child("player1").value as String
                val p2Name = snapshot.child("player2").value as String
                playerOneName = p1Name
                playerTwoName = p2Name
                playerOneTitle.text = p1Name
                playerTwoTitle.text = p2Name
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to read value", Toast.LENGTH_SHORT).show()
            }
        }
        playerNamesRef.addValueEventListener(namesListener)
    }

    // Listener for updates to the game board state
    private fun setGameStateListener() {
        val gameStateRef = database.getReference("gameState")

        // Synchronizes updates to the board.
        val boxListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This will be ordered, so can access in order and modify view of buttons.
                gameState = arrayListOf()
                for ((buttons_index, postSnapshot) in snapshot.children.withIndex()) {
                    val button = buttons[buttons_index]
                    val res = postSnapshot.getValue<Int>()
                    when (res) {
                        0 -> {button!!.text = getString(R.string.x_mark)
                            button.setTextColor(
                                ContextCompat.getColor(this@DisplayDualTicTacToeActivity, R.color.goldenTainoi))}
                        1 -> {button!!.text = getString(R.string.o_mark)
                            button.setTextColor(
                                ContextCompat.getColor(this@DisplayDualTicTacToeActivity, R.color.babyBlue))}
                        else -> button!!.text = getString(R.string.empty_string)
                    }
                    gameState.add(res!!)
                }
                endConditions()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed to read value", Toast.LENGTH_SHORT).show()
            }
        }
        gameStateRef.addValueEventListener(boxListener)
    }

    fun endConditions() {
        if (checkWinner()) {
            if (activePlayer == playerOneName) {
                playerOne.incrementScore()
                database.getReference("scores/player1").setValue(playerOne.score)
            } else if (activePlayer == playerTwoName) {
                playerTwo.incrementScore()
                database.getReference("scores/player2").setValue(playerTwo.score)
            }
            playAgain()
        } else if (gameController.roundCount == 9) {
            playAgain()
        } else {
            if (activePlayer == playerOneName) {
                database.getReference("activePlayer").setValue(playerTwoName)
            } else if (activePlayer == playerTwoName) {
                database.getReference("activePlayer").setValue(playerOneName)
            }
        }
        // Update winning status after determining winner
        statusModel.updateStatus(playerOne.score, playerTwo.score)
        database.getReference("status").setValue(statusModel.status)
    }


    fun checkWinner(): Boolean {
        val WINNING_POSITIONS = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        var winnerResult = false
        for (winningPosition in WINNING_POSITIONS) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState.get(
                    winningPosition[1]
                ) == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2
            ) {
                winnerResult = true
                break // Already found a winner.
            }
        }
        return winnerResult
    }
}