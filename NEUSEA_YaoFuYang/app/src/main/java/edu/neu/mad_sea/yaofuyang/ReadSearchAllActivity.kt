package edu.neu.mad_sea.yaofuyang

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import edu.neu.mad_sea.yaofuyang.ReadRecipeActivity
import edu.neu.mad_sea.yaofuyang.ReadSearchAllActivity

class ReadSearchAllActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    // Note: The first column in all tables must be _id, or load/reset cursor methods will not work.
    private lateinit var adapter: SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_search_all)
        val recipesList = findViewById<ListView>(R.id.recipes_search_result_list_view)
        LoaderManager.getInstance(this).initLoader(0, null, this)
        adapter = SimpleCursorAdapter(
            this,
            R.layout.recipes_list_item,
            null,
            FROM,
            TO,
            0
        )
        recipesList.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val recipeId = view.findViewById<TextView>(R.id.recipe_list_item_id)
            val recipeName = view.findViewById<TextView>(R.id.recipe_list_item_name)
            val intent = Intent(this@ReadSearchAllActivity, ReadRecipeActivity::class.java)
            intent.putExtra("recipe_id", recipeId.text.toString())
            intent.putExtra("recipe_name", recipeName.text.toString())
            this@ReadSearchAllActivity.startActivity(intent)
        }
        recipesList.adapter = adapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            SearchAllContentProvider.CONTENT_URI, FROM, null, null,
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
        // Have to include _id column in projection
        private val FROM = arrayOf(
            "_id",
            RecipesData.RECIPE_NAME
        )
        private val TO = intArrayOf(
            R.id.recipe_list_item_id,
            R.id.recipe_list_item_name
        )
    }
}