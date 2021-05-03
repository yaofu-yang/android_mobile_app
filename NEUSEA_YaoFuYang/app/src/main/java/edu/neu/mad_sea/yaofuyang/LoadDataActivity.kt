package edu.neu.mad_sea.yaofuyang

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LoadDataActivity : AppCompatActivity() {
    private lateinit var recipesDb: RecipesData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)
        recipesDb = RecipesData(this)
    }

    fun deleteExistingRecipes(view: View?) {
        val db = recipesDb.writableDatabase
        //
//        db.execSQL("DELETE FROM " + "recipes");
//        db.execSQL("DELETE FROM " + "nutrition_lt");
//        db.execSQL("DELETE FROM " + "ingredients_lt");
//        db.execSQL("DELETE FROM " + "recipe_ingredients_jt");
//        db.execSQL("DELETE FROM " + "steps");
//        db.execSQL("DELETE FROM " + "recipe_step");
        db.execSQL("VACUUM")
    }

    // Should handle adding a new recipe entry, which includes rows in different tables.
    fun addNewEntry(view: View?) {
        // Test recipe
        val db = recipesDb.writableDatabase
        var row = ""

        // Parses data from recipes.csv
        try {
            val recipeReader = BufferedReader(InputStreamReader(assets.open("recipes.csv")))
            db.beginTransaction()
            while (recipeReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                addNewRecipe(data[0], data[1], data[2], data[3], data[4], data[5], data[6])
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            recipeReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with recipes.")


        // Parses data from nutritions.csv
        try {
            val nutritionReader = BufferedReader(InputStreamReader(assets.open("nutritions.csv")))
            db.beginTransaction()
            while (nutritionReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                addNewNutrition(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7])
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            nutritionReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with nutrition.")

        // Parses data from ingredients.csv
        try {
            val ingredientReader = BufferedReader(InputStreamReader(assets.open("ingredients.csv")))
            db.beginTransaction()
            while (ingredientReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                addNewIngredient(data[0], data[1])
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            ingredientReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with ingredients.")

        // Parses data from recipe.ingredient.csv
        try {
            val recipeIngredientReader = BufferedReader(
                InputStreamReader(assets.open("recipe.ingredient.csv"))
            )
            db.beginTransaction()
            while (recipeIngredientReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                addNewRecipeIngredient(data[0], data[1], data[2])
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            recipeIngredientReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with recipeingredients.")


        // Parses data from steps1.csv
        try {
            val stepReader = BufferedReader(InputStreamReader(assets.open("steps1.csv")))
            db.beginTransaction()
            while (stepReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                if (data.size >= 2) {
                    addNewStep(data[0], data[1])
                }
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            stepReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with steps1.")

        // Parses data from steps2.csv
        try {
            val stepReader = BufferedReader(InputStreamReader(assets.open("steps2.csv")))
            db.beginTransaction()
            while (stepReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                if (data.size >= 2) {
                    addNewStep(data[0], data[1])
                }
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            stepReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with steps2.")


        // Parses data from recipe.step.csv
        println("About to start loading RecipeStep")
        try {
            val recipeStepReader = BufferedReader(InputStreamReader(assets.open("recipe.step.csv")))
            db.beginTransaction()
            while (recipeStepReader.readLine().also { row = it } != null) {
                val data = row.split(",".toRegex()).toTypedArray()
                addNewRecipeStep(data[0], data[1], data[2])
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            recipeStepReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("Done with recipeStep.")
    }

    // Adds new recipe with all attributes defined.
    private fun addNewRecipe(
        id: String, name: String, minutes: String, date: String,
        nutritionId: String, nSteps: String, nIngredients: String
    ) {
        val db = recipesDb.writableDatabase
        val values = ContentValues()
        values.put("_id", id)
        values.put(RecipesData.RECIPE_NAME, name)
        values.put(RecipesData.RECIPE_MINUTES, minutes)
        values.put(RecipesData.RECIPE_DATE, date)
        values.put(RecipesData.RECIPE_NUTRITION_ID, nutritionId)
        values.put(RecipesData.RECIPE_N_STEPS, nSteps)
        values.put(RecipesData.RECIPE_N_INGREDIENTS, nIngredients)
        db.insert(RecipesData.RECIPES_TABLE, null, values)
    }

    // Adds new nutrition with all attributes defined.
    private fun addNewNutrition(
        id: String, calories: String, totalFat: String, sugar: String,
        sodium: String, protein: String, satFat: String, carbohydrates: String
    ) {
        val db = recipesDb.writableDatabase
        val values = ContentValues()
        values.put("_id", id)
        values.put(RecipesData.NUTRITION_CALORIES, calories)
        values.put(RecipesData.NUTRITION_TOTAL_FAT_PVD, totalFat)
        values.put(RecipesData.NUTRITION_SUGAR_PVD, sugar)
        values.put(RecipesData.NUTRITION_SODIUM_PVD, sodium)
        values.put(RecipesData.NUTRITION_PROTEIN_PVD, protein)
        values.put(RecipesData.NUTRITION_SATURATED_FAT_PVD, satFat)
        values.put(RecipesData.NUTRITION_CARBOHYDRATES_PVD, carbohydrates)
        db.insert(RecipesData.NUTRITION_LT, null, values)
    }

    // Handles adding a new row to the ingredients table.
    private fun addNewIngredient(ingredientId: String, ingredientName: String) {
        val db = recipesDb.writableDatabase
        val values = ContentValues()
        values.put("_id", ingredientId)
        values.put(RecipesData.INGREDIENT_NAME, ingredientName)
        db.insert(RecipesData.INGREDIENTS_LT, null, values)
    }

    private fun addNewRecipeIngredient(id: String, recipeId: String, ingredientId: String) {
        val db = recipesDb.writableDatabase
        val values = ContentValues()
        values.put("_id", id)
        values.put(RecipesData.RECIPE_ID, recipeId)
        values.put(RecipesData.INGREDIENT_ID, ingredientId)
        db.insert(RecipesData.RECIPE_INGREDIENTS_TABLE, null, values)
    }

    // Handles adding a new row to the ingredients table.
    private fun addNewStep(id: String, desc: String) {
        val db = recipesDb.writableDatabase
        val values = ContentValues()
        values.put("_id", id)
        values.put(RecipesData.STEP_NAME, desc)
        db.insert(RecipesData.STEPS_TABLE, null, values)
    }

    // Handles adding a new row to the ingredients table.
    private fun addNewRecipeStep(id: String, recipeId: String, stepId: String) {
        val db = recipesDb.writableDatabase
        val values = ContentValues()
        values.put("_id", id)
        values.put(RecipesData.RECIPE_ID, recipeId)
        values.put(RecipesData.STEP_ID, stepId)
        db.insert(RecipesData.RECIPE_STEP_TABLE, null, values)
    }
}