package edu.neu.mad_sea.yaofuyang

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditIngredientsActivity : AppCompatActivity() {
    private lateinit var recipesDb : RecipesData
    private lateinit var addIngredientBox : EditText
    private lateinit var deleteIngredientBox : EditText
    private lateinit var addIngredientButton : Button
    private lateinit var deleteIngredientButton : Button
    private lateinit var showIngredientsButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ingredients)
        recipesDb = RecipesData(this)

        addIngredientBox = findViewById(R.id.add_ingredient_box)
        addIngredientButton = findViewById(R.id.add_ingredient_button)

        deleteIngredientBox = findViewById(R.id.delete_ingredient_box)
        deleteIngredientButton = findViewById(R.id.delete_ingredient_button)
        showIngredientsButton = findViewById(R.id.show_ingredients_button)

        addIngredientButton.setOnClickListener {
            val ingredient = addIngredientBox.text.toString()
            addNewIngredient(ingredient)
        }

        deleteIngredientButton.setOnClickListener {
            val ingredient = deleteIngredientBox.text.toString()
            deleteIngredient(ingredient)
        }

        showIngredientsButton.setOnClickListener {
            val intent = Intent(this, ReadIngredientsListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addNewIngredient(ingredientName : String) {
        val db: SQLiteDatabase = recipesDb.writableDatabase
        val values = ContentValues()
        values.put(RecipesData.INGREDIENT_NAME, ingredientName)
        db.insert(RecipesData.INGREDIENTS_LIST, null, values)
    }

    private fun deleteIngredient(ingredientName: String) {
        val db: SQLiteDatabase = recipesDb.writableDatabase
        val deleteQuery = "DELETE FROM " + RecipesData.INGREDIENTS_LIST +
                " WHERE " + RecipesData.INGREDIENT_NAME + "=" + "'" + ingredientName + "';";
        db.execSQL(deleteQuery);
    }
}