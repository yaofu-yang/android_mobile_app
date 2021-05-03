package edu.neu.mad_sea.yaofuyang

import android.content.Intent
import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

private lateinit var browseButton : Button
private lateinit var allIngredientsButton : Button
class SearchRecipesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipes)

        browseButton = findViewById(R.id.browse_ingredient_button)
        allIngredientsButton = findViewById(R.id.all_ingredients_search_button)

        browseButton.setOnClickListener {
            val intent = Intent(this, ReadRecipesListActivity::class.java)
            startActivity(intent)
        }

        allIngredientsButton.setOnClickListener {
            val intent = Intent(this, ReadSearchAllActivity::class.java)
            startActivity(intent)
            // Use all ingredients in list to generate.
        }
    }
}