package edu.neu.mad_sea.yaofuyang

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class NutritionContentProvider : ContentProvider() {
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
        val recipeNutrition = "(SELECT * FROM recipes R, nutrition_lt N" +
                " WHERE R.nutritionId == N._id)"
        queryBuilder.tables = recipeNutrition
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
        private const val AUTHORITY = "nutrition"
        private const val BASE_PATH = "macros"
        val CONTENT_URI : Uri = Uri.parse(
            "content://" + AUTHORITY
                    + "/" + BASE_PATH
        )
        private lateinit var recipesDb: RecipesData
    }
}