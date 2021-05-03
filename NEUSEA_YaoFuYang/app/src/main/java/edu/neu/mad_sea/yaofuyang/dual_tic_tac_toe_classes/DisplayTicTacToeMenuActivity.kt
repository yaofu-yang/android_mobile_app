package edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import edu.neu.mad_sea.yaofuyang.DisplayDualTicTacToeActivity
import edu.neu.mad_sea.yaofuyang.DisplayTicTacToeActivity
import edu.neu.mad_sea.yaofuyang.R

private lateinit var one_player_button : Button
private lateinit var two_player_button : Button
private lateinit var create_username_button : Button
private lateinit var fcm_challenge_button : Button

class DisplayTicTacToeMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_tic_tac_toe_menu)
        setTitle(R.string.tic_menu)
        createNotificationChannel()

        one_player_button = findViewById(R.id.one_player_button)
        two_player_button = findViewById(R.id.two_player_button)
        create_username_button = findViewById(R.id.create_user_button)
        fcm_challenge_button = findViewById(R.id.fcm_button)

        // Listener to initiate single player mode
        one_player_button.setOnClickListener {
            val intent = Intent(this, DisplayTicTacToeActivity::class.java)
            startActivity(intent)
        }

        // Listener to initiate 2-player mode.
        two_player_button.setOnClickListener {
            // Initialize initial game state.
            setInitialIndexes()
            val intent = Intent(this, DisplayDualTicTacToeActivity::class.java)
            startActivity(intent)
        }

        // Listener to initiate user account creation/deletion.
        create_username_button.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        fcm_challenge_button.setOnClickListener {
            val intent = Intent(this, TicFCMActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "2playerTicChannel"
            val description = "The messaging channel for finding another player."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_NAME, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "TicMenu"
        const val CHANNEL_NAME = "2playerTicChannel"
    }

    private fun setInitialIndexes() {
        val database = FirebaseDatabase.getInstance()

        // Initialize and sets game box values to default value (2)
        val gameStateRef = database.getReference("gameState")
        gameStateRef.setValue(listOf(2, 2, 2, 2, 2, 2, 2, 2, 2))

        // Initializes and sets player 1 & 2 names to default values.
        val playerRef = database.getReference("players")
        val playerOneRef = playerRef.child("player1")
        val playerTwoRef = playerRef.child("player2")
        playerOneRef.setValue("Player1")
        playerTwoRef.setValue("Player2")

        // Initializes and sets player 1 & 2 scores to default values
        val playerScoreRef = database.getReference("scores")
        val oneScoreRef = playerScoreRef.child("player1")
        val twoScoreRef = playerScoreRef.child("player2")
        oneScoreRef.setValue(0)
        twoScoreRef.setValue(0)

        // Add a value for storing occupancy to indicate player order
        // Whoever sends the invite will default to player 1
        val occupancyRef = database.getReference("occupancy")
        occupancyRef.setValue(0L)

        // Initializes active player which tracks player turn by username
        val activeRef = database.getReference("activePlayer")
        activeRef.setValue("player")

        // Initialize the status representing current leading player.
        val statusRef = database.getReference("status")
        statusRef.setValue("")

        // Initialize value indicating that a winner for a round has been identified.
        val winnerRef = database.getReference("hasWinner")
        winnerRef.setValue(false)
    }

}