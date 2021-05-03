package edu.neu.mad_sea.yaofuyang

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class StepsContentProvider : ContentProvider() {
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
        val recipeSteps = "(SELECT * FROM recipes R, recipe_step RS," +
                " steps S WHERE R._id == RS.recipeID" +
                " AND RS.stepId == S._id)"
        queryBuilder.tables = recipeSteps
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
        private const val AUTHORITY = "steps"
        private const val BASE_PATH = "step"
        val CONTENT_URI: Uri = Uri.parse(
            "content://" + AUTHORITY
                    + "/" + BASE_PATH
        )
        private lateinit var recipesDb: RecipesData
    }
}