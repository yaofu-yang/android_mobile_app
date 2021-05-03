package edu.neu.mad_sea.yaofuyang.dictionary_classes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.neu.mad_sea.yaofuyang.R

class DisplayDictAckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.ack_title)
        setContentView(R.layout.activity_display_dict_ack)
    }
}