package edu.neu.mad_sea.yaofuyang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

private lateinit var ingredients_button : Button
//private lateinit var add_recipe_button : Button
//private lateinit var add_review_button: Button
private lateinit var search_recipes_button : Button
private lateinit var data_loader_button : Button
private lateinit var check_bookmarks_button : Button
private lateinit var recipes_ack_button : Button

class DisplayRecipesMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_recipes_menu)

        ingredients_button = findViewById(R.id.ingredients_button)
//        add_recipe_button = findViewById(R.id.add_recipe_button)
//        add_review_button = findViewById(R.id.add_review_button)
        search_recipes_button = findViewById(R.id.search_recipes_button)
        data_loader_button = findViewById(R.id.recipes_data_manager)
        check_bookmarks_button = findViewById(R.id.recipes_bookmarked_list)
        recipes_ack_button = findViewById(R.id.ack_button)


        ingredients_button.setOnClickListener {
            val intent = Intent(this, EditIngredientsActivity::class.java)
            startActivity(intent)
        }

//        add_recipe_button.setOnClickListener {
//            val intent = Intent(this, AddRecipeActivity::class.java)
//            startActivity(intent)
//        }
//
//        add_review_button.setOnClickListener {
//            val intent = Intent(this, AddReviewActivity::class.java)
//            startActivity(intent)
//        }

        search_recipes_button.setOnClickListener {
            val intent = Intent(this, SearchRecipesActivity::class.java)
            startActivity(intent)
        }

        data_loader_button.setOnClickListener {
            val intent = Intent(this, LoadDataActivity::class.java)
            startActivity(intent)
        }

        check_bookmarks_button.setOnClickListener {
            val intent = Intent(this, ReadBookmarkListActivity::class.java)
            startActivity(intent)
        }

        recipes_ack_button.setOnClickListener {
            val intent = Intent(this, DisplayRecipesAck::class.java)
            startActivity(intent)
        }
    }
}