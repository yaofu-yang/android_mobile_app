package edu.neu.mad_sea.yaofuyang.tic_tac_toe_classes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import edu.neu.mad_sea.yaofuyang.App
import edu.neu.mad_sea.yaofuyang.R
import java.util.*

class BoardFragment : Fragment(), View.OnClickListener {
    /**
     * Define an interface for hosting activities
     */
    interface Callbacks {
        fun onBoxSelected(v : View?)
    }

    private var callbacks: Callbacks? = null
    var buttons = arrayOfNulls<Button>(9)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    companion object {
        fun newInstance(): BoardFragment {
            return BoardFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)

        for (i in buttons.indices) {
            val buttonID = "${getString(R.string.button_id_prefix)}$i"
            val resourceID = resources.getIdentifier(buttonID, "id", App.getInstance().packageName)
            buttons[i] = view.findViewById(resourceID)
//            if (buttons[i] == null) {
//                println("The Button is null")
//            }
            buttons[i]!!.setOnClickListener(this)  // Passes View.OnClickListener
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onClick(v: View?) {
        println("You have clicked the button")
        callbacks?.onBoxSelected(v)
    }
}
