package edu.neu.mad_sea.yaofuyang.dictionary_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.mad_sea.yaofuyang.R

class ResultFragment(private var allResults: ArrayList<Result>) : Fragment() {
    private lateinit var result : Result
    private lateinit var resultRecyclerView: RecyclerView
    private var adapter : ResultAdapter? = null
    private var searchResults = allResults

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        result = Result()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        resultRecyclerView =
            view.findViewById(R.id.result_recycler_view) as RecyclerView
        resultRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    fun removeItems() {
        allResults.clear()
        resultRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateUI() {
//        val results = result.results
        adapter = ResultAdapter(searchResults)
        resultRecyclerView.adapter = adapter
    }

    private inner class ResultHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        val resultTextView: TextView = itemView.findViewById(R.id.result)

        private lateinit var result : Result
        private val titleTextView: TextView = itemView.findViewById(R.id.result)
        fun bind(result: Result) {
            this.result = result
            titleTextView.text = this.result.result.toString()
        }
    }

    private inner class ResultAdapter(var results: List<Result>)
        : RecyclerView.Adapter<ResultHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ResultHolder {
            val view = layoutInflater.inflate(R.layout.list_item_result, parent, false)
            return ResultHolder(view)
        }

        override fun getItemCount() = results.size

        override fun onBindViewHolder(holder: ResultHolder, position: Int) {
            val result = results[position]
            holder.bind(result)
        }
    }
}