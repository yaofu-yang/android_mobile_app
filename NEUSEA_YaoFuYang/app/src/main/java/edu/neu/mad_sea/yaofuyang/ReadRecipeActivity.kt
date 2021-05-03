package edu.neu.mad_sea.yaofuyang

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class ReadRecipeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    // Note: The first column in all tables must be _id, or load/reset cursor methods will not work.
    private lateinit var ingredientsAdapter: SimpleCursorAdapter
    private lateinit var stepsAdapter: SimpleCursorAdapter
    private lateinit var nutritionAdapter: SimpleCursorAdapter

    // Add/Delete buttons for bookmark list
    private lateinit var addBookmarkButton: Button
    private lateinit var removeBookmarkButton: Button
//    private lateinit var addReviewButton : Button
//    private lateinit var seeReviewButton : Button

    private lateinit var recipesDb : RecipesData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_recipe)
        recipesDb = RecipesData(this)

        val intent = intent
        recipeId = intent.getStringExtra("recipe_id").toString()
        val recipeName = intent.getStringExtra("recipe_name").toString()

        // Initialize all textviews
        val recipeTitle = findViewById<TextView>(R.id.recipe_title)
        recipeTitle.text = "Recipe Title: $recipeName"

        // Handle the ingredients List View
        val ingredientList = findViewById<ListView>(R.id.recipe_item_ingredients)
        LoaderManager.getInstance(this).initLoader(1, null, this)
        ingredientsAdapter = SimpleCursorAdapter(
            this,
            R.layout.ingredient_item,
            null,
            FROM_INGREDIENT,
            TO_INGREDIENT,
            0
        )
        ingredientList.adapter = ingredientsAdapter

        // Handle the steps List View.
        val stepsList = findViewById<ListView>(R.id.recipe_item_steps)
        LoaderManager.getInstance(this).initLoader(2, null, this)
        stepsAdapter = SimpleCursorAdapter(
            this,
            R.layout.step_item,
            null,
            FROM_STEP,
            TO_STEP,
            0
        )
        stepsList.adapter = stepsAdapter

        // Handle the nutrition List View.
        val nutritionList = findViewById<ListView>(R.id.recipe_nutrition)
        LoaderManager.getInstance(this).initLoader(3, null, this)
        nutritionAdapter = SimpleCursorAdapter(
            this,
            R.layout.nutrition_item,
            null,
            FROM_NUTRITION,
            TO_NUTRITION,
            0
        )
        nutritionList.adapter = nutritionAdapter

        addBookmarkButton = findViewById(R.id.add_bookmark_button)
        removeBookmarkButton = findViewById(R.id.remove_bookmark_button)
//        addReviewButton = findViewById(R.id.add_review_button)
//        seeReviewButton = findViewById(R.id.see_reviews_button)

        addBookmarkButton.setOnClickListener {
            addNewBookmark(recipeId, recipeName)
        }
        removeBookmarkButton.setOnClickListener {
            removeBookmark(recipeId, recipeName)
        }
//        addReviewButton.setOnClickListener {
//            // TODO: Launch activity.
//        }
//        seeReviewButton.setOnClickListener {
//            // TODO: Launch activity.
//        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val selection = "_id=?"
        val selectionArgs = arrayOfNulls<String>(1)
        selectionArgs[0] = recipeId
        if (id == 1) {
            return CursorLoader(
                this,
                RecipeContentProvider.CONTENT_URI, FROM_INGREDIENT, selection, selectionArgs,
                null
            )
        }
        return if (id == 2) {
            CursorLoader(
                this,
                StepsContentProvider.CONTENT_URI,
                FROM_STEP,
                selection,
                selectionArgs,
                null
            )
        } else CursorLoader(
            this,
            NutritionContentProvider.CONTENT_URI,
            FROM_NUTRITION,
            selection,
            selectionArgs,
            null
        )
        // Neither of the above indicates case 3.
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        if (loader.id == 1) {
            ingredientsAdapter.swapCursor(data)
        }
        if (loader.id == 2) {
            stepsAdapter.swapCursor(data)
        }
        if (loader.id == 3) {
            nutritionAdapter.swapCursor(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // delete reference as data is no longer available.
        if (loader.id == 1) {
            ingredientsAdapter.swapCursor(null)
        }
        if (loader.id == 2) {
            stepsAdapter.swapCursor(null)
        }
        if (loader.id == 3) {
            nutritionAdapter.swapCursor(null)
        }
    }

    private fun addNewBookmark(recipeId: String, recipeName: String) {
        val db: SQLiteDatabase = recipesDb.writableDatabase
//        db.execSQL("DROP TABLE IF EXISTS favorite_recipes")
//        val createBookmarkedRecipes =
//            "CREATE TABLE IF NOT EXISTS " + RecipesData.BOOKMARKED_RECIPES + "(" +
//                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + RecipesData.RECIPE_ID + " INTEGER, " +
//                    RecipesData.RECIPE_NAME + " TEXT);"
//        db.execSQL(createBookmarkedRecipes)
        val values = ContentValues()
        values.put(RecipesData.RECIPE_ID, recipeId)
        values.put(RecipesData.RECIPE_NAME, recipeName)
        db.insert(RecipesData.BOOKMARKED_RECIPES, null, values)
    }

    // TODO: Fix ingredients portion to recipe
    private fun removeBookmark(recipeId: String, recipeName: String) {
        val db: SQLiteDatabase = recipesDb.writableDatabase
        val deleteQuery = "DELETE FROM " + RecipesData.BOOKMARKED_RECIPES +
                " WHERE " + RecipesData.RECIPE_ID + "=" + "'" + recipeId + "';";
        db.execSQL(deleteQuery);
    }


    companion object {
        // Have to include _id column in projection
        // FROM/TO arrays for ingredients.
        private val FROM_INGREDIENT = arrayOf("_id", RecipesData.INGREDIENT_NAME)
        private val TO_INGREDIENT = intArrayOf(
            R.id.recipe_item_ingredient,
            R.id.recipe_item_ingredient
        )

        // FROM/TO arrays for steps
        private val FROM_STEP = arrayOf("_id", RecipesData.STEP_NAME)
        private val TO_STEP = intArrayOf(R.id.recipe_item_step, R.id.recipe_item_step)
        private lateinit var recipeId: String

        // FROM/TO arrays for nutrition
        private val FROM_NUTRITION = arrayOf(
            "_id", RecipesData.NUTRITION_CALORIES,
            RecipesData.NUTRITION_TOTAL_FAT_PVD, RecipesData.NUTRITION_SUGAR_PVD,
            RecipesData.NUTRITION_SODIUM_PVD, RecipesData.NUTRITION_PROTEIN_PVD,
            RecipesData.NUTRITION_SATURATED_FAT_PVD, RecipesData.NUTRITION_CARBOHYDRATES_PVD
        )
        private val TO_NUTRITION = intArrayOf(
            R.id.recipe_calories,
            R.id.recipe_calories, R.id.recipe_total_fat_pvd, R.id.recipe_sugar_pvd,
            R.id.recipe_sodium_pvd, R.id.recipe_protein_pvd, R.id.recipe_sat_fat_pvd,
            R.id.recipe_carbohydrates_pvd
        )
    }
}