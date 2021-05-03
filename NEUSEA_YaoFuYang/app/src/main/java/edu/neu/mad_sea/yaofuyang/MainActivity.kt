package edu.neu.mad_sea.yaofuyang

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes.DisplayTicTacToeMenuActivity

private lateinit var aboutButton: Button
private lateinit var errorButton: Button
private lateinit var dictionaryButton: Button
private lateinit var ticTacToeButton: Button
private lateinit var recipeButton: Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.full_name)
        setContentView(R.layout.activity_main)

        // Accessing View objects
        aboutButton = findViewById(R.id.about_button)
        errorButton = findViewById(R.id.error_button)
        dictionaryButton = findViewById(R.id.dictionary_button)
        ticTacToeButton = findViewById(R.id.tic_tac_toe_button)
        recipeButton = findViewById(R.id.recipes_button)

        // Waits for clicks to About Button
        aboutButton.setOnClickListener{
            // Launch new activity to display About Me page.
            val intent = Intent(this, DisplayAboutActivity::class.java)
            startActivity(intent)
        }

        // Waits for clicks to Generate Error Button
        errorButton.setOnClickListener{
            // Intentionally generates an error at runtime.
            startActivity(null)
        }

        // Waits for clicks to Dictionary Button
        dictionaryButton.setOnClickListener{
            // Launches new activity to display Dictionary Page
            val intent = Intent(this, DisplayDictionaryActivity::class.java)
            startActivity(intent)
        }

        ticTacToeButton.setOnClickListener{
            // Launches new activity to display tic tac toe game
            val intent = Intent(this, DisplayTicTacToeMenuActivity::class.java)
            startActivity(intent)
        }

        recipeButton.setOnClickListener {
            val intent = Intent(this, DisplayRecipesMenuActivity::class.java)
            startActivity(intent)
        }

    }
}