package edu.neu.mad_sea.yaofuyang

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class RecipeContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        recipesDb = RecipesData(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        // Instantiate SQLiteQueryBuilder
        val queryBuilder = SQLiteQueryBuilder()

        // Set the table (need to join if using intermediate table)
        // Todo: Fix string value(s) to include RecipesData constants.
        val recipe_nutrition = "(SELECT * FROM recipes R, recipe_ingredients_jt RI," +
                " ingredients_lt I WHERE R._id == RI.recipeID" +
                " AND RI.ingredientId == I._id)"
        queryBuilder.tables = recipe_nutrition
        val db = recipesDb.readableDatabase
        val cursor = queryBuilder.query(
            db,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Todo: Implement this first.
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        private const val AUTHORITY = "recipe"
        private const val BASE_PATH = "full"
        val CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY
                    + "/" + BASE_PATH
        )
        private lateinit var recipesDb: RecipesData
    }
}