package edu.neu.mad_sea.yaofuyang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import edu.neu.mad_sea.yaofuyang.dictionary_classes.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

private lateinit var searchButton: Button
private lateinit var clearButton: Button
private lateinit var ackButton: Button

private lateinit var availableLetters: EditText
private lateinit var pattern: EditText
private lateinit var wordLength: EditText
private lateinit var timer: TextView

private lateinit var fragment : ResultFragment
private lateinit var resultsList : ArrayList<Result>

private lateinit var bufferedReader: BufferedReader

class DisplayDictionaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.dictionary_title)
        setContentView(R.layout.activity_display_dictionary)

        searchButton = findViewById(R.id.search_button)
        clearButton = findViewById(R.id.clear_button)
        ackButton = findViewById(R.id.ack_button)
        availableLetters = findViewById(R.id.available_letters_box)
        pattern = findViewById(R.id.pattern_box)
        wordLength = findViewById(R.id.number_of_letters_box)
        timer = findViewById(R.id.timer)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment != null) {
            fragment = ResultFragment(resultsList)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        // Waits for clicks to Ack button to display Acknowledgements.
        ackButton.setOnClickListener{
            // Display all of the acknowledgements
            val intent = Intent(this, DisplayDictAckActivity::class.java)
            startActivity(intent)
        }

        // Waits for clicks to Clear button to refresh text fields.
        clearButton.setOnClickListener{
            availableLetters.setText(R.string.empty_string)
            pattern.setText(R.string.empty_string)
            wordLength.setText(R.string.empty_string)
            fragment.removeItems()
        }

        // Waits for clicks to the Search button.
        searchButton.setOnClickListener{
            // Getting parameters from edit text boxes.
            val letters = availableLetters.text.toString()
            val pattern = pattern.text.toString()
            val length = wordLength.text.toString()

            val inputMessage = ParameterChecker.checkInput(letters, pattern, length)
            if (inputMessage != null) {
                println(inputMessage)
                Toast.makeText(this, inputMessage, Toast.LENGTH_LONG).show()
            } else {  // Inputs were properly formed.
                val trie = DictionaryTrie()
                var word: String? = ""
                try {
                    bufferedReader = BufferedReader(
                        InputStreamReader(assets.open("wordlist.txt"))
                    )
                    while (word != null) {
                        word = bufferedReader.readLine()
                        if (word != null) {
                            trie.insert(word)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val resultsList = trie.customSearch(letters, pattern)

                // Fragment portion
                if (currentFragment == null) {
                    fragment = ResultFragment(resultsList)
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit()
                }
                // Set timer
                timer.text = trie.searchTime
            }
        }
    }
}