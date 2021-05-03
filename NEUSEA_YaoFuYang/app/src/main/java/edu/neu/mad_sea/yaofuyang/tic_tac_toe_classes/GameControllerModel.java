package edu.neu.mad_sea.yaofuyang.tic_tac_toe_classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class GameControllerModel {
    /**
     * Possible winning positions, think of your game board as the following:
     * 0   1   2
     * 3   4   5
     * 6   7   8
     */
    private static final int[][] WINNING_POSITIONS = {
            {0,1,2}, {3,4,5}, {6,7,8}, // rows
            {0,3,6}, {1,4,7}, {2,5,8}, // columns
            {0,4,8}, {2,4,6}           // diagonal
    };

    /**
     * Game State: the state will be controlled by an array of integers
     * - 0: player one has selected this game piece (or button)
     * - 1: player two has selected this game piece (or button)
     * - 2: neither player has chosen this game piece and is available for use
     */
    private static final int[] DEFAULT_GAME_STATE = {2,2,2,2,2,2,2,2,2};
    private int[] gameState;
    private int roundCount;
    private boolean activePlayer;


    /**
     * Constructs an instance of the GameControllerModel class.
     */
    public GameControllerModel() {
        this.gameState = DEFAULT_GAME_STATE;
        this.roundCount = 0;
        this.activePlayer = true;
    }

    /**
     * Returns an int array representing the current game state.
     * @return an int array representing the current game state.
     */
    public int[] getGameState() {
        return this.gameState;
    }

    public void resetGameState() {
        for (int i = 0; i < DEFAULT_GAME_STATE.length; i++) {
            this.gameState[i] = 2;
        }
    }

    public int getRoundCount() {
        return this.roundCount;
    }

    public void resetCount() {
        this.roundCount = 0;
    }

    public void incrementRound() {
        this.roundCount++;
    }

    public boolean isActivePlayer() {
        return this.activePlayer;
    }

    public void toggleActivePlayer() {
        this.activePlayer = !this.activePlayer;
    }

    public void resetActivePlayer() {
        this.activePlayer = true;
    }

    /**
     * Returns true if there is a winner and false otherwise.
     * @return true if there is a winner and false otherwise.
     */
    public boolean checkWinnerTwo(List<Integer> currentState) {
        boolean winnerResult = false;

        for(int[] winningPosition: WINNING_POSITIONS) {
            if (currentState.get(winningPosition[0]).equals(currentState.get(winningPosition[1])) &&
                    currentState.get(winningPosition[1]).equals(currentState.get(winningPosition[2])) &&
                    currentState.get(winningPosition[0]) != 2) {
                winnerResult = true;
                break;  // Already found a winner.
            }
        }
        return winnerResult;
    }

    public boolean checkWinner() {
        boolean winnerResult = false;

        for(int[] winningPosition: WINNING_POSITIONS) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] != 2) {
                winnerResult = true;
                break;  // Already found a winner.
            }
        }
        return winnerResult;
    }
}
