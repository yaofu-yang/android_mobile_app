package edu.neu.mad_sea.yaofuyang

import android.database.Cursor
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class ReadIngredientsListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    // Note: The first column in all tables must be _id, or load/reset cursor methods will not work.
    private lateinit var adapter: SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_ingredients_list)
        val recipesList = findViewById<ListView>(R.id.ingredients_listView)
        LoaderManager.getInstance(this).initLoader(0, null, this)
        adapter = SimpleCursorAdapter(
            this,
            R.layout.ingredients_list_item,
            null,
            FROM,
            TO,
            0
        )
        recipesList.adapter = adapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            IngredientListContentProvider.CONTENT_URI, FROM, null, null,
            null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // delete reference as data is no longer available.
        adapter.swapCursor(null)
    }

    companion object {
        private val TO = intArrayOf(
            R.id.ingredient_list_item_name,
            R.id.ingredient_list_item_name
        )

        // Have to include _id column in projection
        private val FROM = arrayOf(
            "_id",
            RecipesData.INGREDIENT_NAME
        )
    }
}