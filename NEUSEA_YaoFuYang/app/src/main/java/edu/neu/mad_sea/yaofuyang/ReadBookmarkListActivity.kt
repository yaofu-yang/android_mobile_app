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
import edu.neu.mad_sea.yaofuyang.ReadBookmarkListActivity
import edu.neu.mad_sea.yaofuyang.ReadRecipeActivity

class ReadBookmarkListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    // Note: The first column in all tables must be _id, or load/reset cursor methods will not work.
    private lateinit var adapter: SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_bookmark_list)
        val bookmarkList = findViewById<ListView>(R.id.bookmarks_list_view)
        LoaderManager.getInstance(this).initLoader(0, null, this)
        adapter = SimpleCursorAdapter(
            this,
            R.layout.bookmark_list_item,
            null,
            FROM,
            TO,
            0
        )
        bookmarkList.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val recipeId = view.findViewById<TextView>(R.id.bookmark_list_item_id)
            val recipeName = view.findViewById<TextView>(R.id.bookmark_list_item_name)
            val intent = Intent(this@ReadBookmarkListActivity, ReadRecipeActivity::class.java)
            intent.putExtra("recipe_id", recipeId.text.toString())
            intent.putExtra("recipe_name", recipeName.text.toString())
            this@ReadBookmarkListActivity.startActivity(intent)
        }
        bookmarkList.adapter = adapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            BookmarkListContentProvider.CONTENT_URI, FROM, null, null,
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
            RecipesData.RECIPE_ID,
            RecipesData.RECIPE_NAME
        )
        private val TO = intArrayOf(
            R.id.bookmark_list_item_id,
            R.id.bookmark_list_item_id,
            R.id.bookmark_list_item_name
        )
    }
}