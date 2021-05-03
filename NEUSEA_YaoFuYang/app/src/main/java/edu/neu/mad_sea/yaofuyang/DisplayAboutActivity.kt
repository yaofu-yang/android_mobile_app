package edu.neu.mad_sea.yaofuyang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DisplayAboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Changes title to appropriate activity.
        setTitle(R.string.about_title)
        setContentView(R.layout.activity_display_about)
    }
}