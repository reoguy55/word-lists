package com.example.word_lists

import android.app.ActionBar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ReviewActivity : AppCompatActivity() {

    private var wordList = HashMap<String?, Vocab>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        val intent = intent
        wordList = intent.getSerializableExtra("words") as HashMap<String?, Vocab>
        refreshList()

        val homeButton: Button = findViewById(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("words", wordList)
            intent.putExtra("reload", true)
            startActivity(intent)
        }
    }

    fun refreshList() {
        val chunkList: LinearLayout = findViewById(R.id.chunkList)
        chunkList.removeAllViews()
        for ((key, value) in wordList) {
            val word = key
            val def = value.dictEntry.definitions?.getOrElse(0) {"N/A"}
            val syn0 = value.thesEntry.meta?.syns?.get(0)?.get(0)
            val syn1 = value.thesEntry.meta?.syns?.get(0)?.get(1)
            val syn2 = value.thesEntry.meta?.syns?.get(0)?.get(2)
            val chunk = LayoutInflater.from(this).inflate(R.layout.chunk_vocab_word, null, false)
            val wordBox: TextView = chunk.findViewById(R.id.wordView)
            wordBox.text = word
            wordBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            val displayButton: Button = chunk.findViewById(R.id.displayButton)
            val removeButton: Button = chunk.findViewById(R.id.removeButton)
            displayButton.setOnClickListener {
                val display: LinearLayout = findViewById(R.id.infoDisplay)
                val displayPlaceholder: TextView = findViewById(R.id.displayText)
                if (displayPlaceholder.visibility == View.GONE) {
                    if (displayButton.text == "Display") {
                        Log.d("toggleFail", "Tried pressing a separate button while one was already displayed!")
                    } else {
                        display.removeAllViews()
                        displayPlaceholder.visibility = View.VISIBLE
                        display.addView(displayPlaceholder)
                        displayButton.text = "Display"
                    }
                } else {
                    displayPlaceholder.visibility = View.GONE
                    val textLayout = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    val wordText = TextView(this)
                    wordText.layoutParams = textLayout
                    wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    wordText.visibility = View.VISIBLE
                    wordText.text = "Word: $word"
                    display.addView(wordText)

                    val defText = TextView(this)
                    defText.layoutParams = textLayout
                    defText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    defText.visibility = View.VISIBLE
                    defText.text = "Definition: $def"
                    display.addView(defText)

                    val synText = TextView(this)
                    synText.layoutParams = textLayout
                    synText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    synText.visibility = View.VISIBLE
                    synText.text = "Synonyms: ${syn0}, ${syn1}, $syn2"
                    display.addView(synText)

                    displayButton.text = "Hide"
                }
            }
            removeButton.setOnClickListener {
                wordList.remove(word)
                val display: LinearLayout = findViewById(R.id.infoDisplay)
                val displayPlaceholder: TextView = findViewById(R.id.displayText)
                display.removeAllViews()
                displayPlaceholder.visibility = View.VISIBLE
                display.addView(displayPlaceholder)
                refreshList()
            }
            chunkList.addView(chunk)
        }
    }
}
