package com.example.word_lists

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class AddActivity : AppCompatActivity() {

    private var vocabWords = HashMap<String?, Vocab>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val intent = intent
        if (intent != null && intent.getBooleanExtra("reload", false)) {
            vocabWords = intent.getSerializableExtra("words") as HashMap<String?, Vocab>
        }
        refreshList()
        val addButton: Button = findViewById(R.id.addButton)
        val wordEntry: EditText = findViewById(R.id.wordEntry)
        val wordList: LinearLayout = findViewById(R.id.wordList)
        val queue = Volley.newRequestQueue(this)
        val dictBase = "https://dictionaryapi.com/api/v3/references/collegiate/json/"
        val thesBase = "https://dictionaryapi.com/api/v3/references/thesaurus/json/"
        val dictKey = getString(R.string.dict_api_key)
        val thesKey = getString(R.string.thes_api_key)
        val resultText: TextView = findViewById(R.id.resultText)
        addButton.setOnClickListener {
            if (wordEntry.text.toString() != "") {
                val thesURL = thesBase + wordEntry.text.toString() + "?key=" + thesKey
                val thesRequest = StringRequest(Request.Method.GET, thesURL,
                    Response.Listener { response ->
                        val trim1 = response.substring(1)
                        val trim2 = trim1.substring(0, trim1.length - 1)
                        val mapper = jacksonObjectMapper()
                        val thesEntry: ThesaurusEntry = mapper.readValue(trim2)
                        Log.d("thesResponse", response)
                        Log.d("syns", thesEntry.meta.toString())
                        val syn0 = thesEntry.meta?.syns?.get(0)?.getOrElse(0) {"N/A"} ?: "N/A"
                        val syn1 = thesEntry.meta?.syns?.get(0)?.getOrElse(1) {"N/A"} ?: "N/A"
                        val syn2 = thesEntry.meta?.syns?.get(0)?.getOrElse(2) {"N/A"} ?: "N/A"
                        val dictURL = dictBase + wordEntry.text.toString() + "?key=" + dictKey
                        //send dict request here
                        val stringRequest = StringRequest(
                            Request.Method.GET, dictURL,
                            Response.Listener { response ->
                                Log.d( "response", response)
                                val trim3 = response.substring(1)
                                val trim4 = trim3.substring(0, trim3.length - 1)
                                val mapper = jacksonObjectMapper()
                                val entry: Entry = mapper.readValue(trim4)
                                var displayText = "Request successful!\n${entry}\n"
                                displayText += "\nSynonyms: ${syn0}, ${syn1}, ${syn2}"
                                resultText.text = displayText
                                resultText.visibility = View.VISIBLE
                                val myLayoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT)
                                val newWord = TextView(this)
                                newWord.layoutParams = myLayoutParams
                                newWord.visibility = View.VISIBLE
                                newWord.setTextColor(Color.BLACK)
                                newWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                                newWord.text = entry.hwi.hw?.replace("*", "")
                                newWord.gravity = Gravity.CENTER
                                wordList.addView(newWord)
                                wordEntry.setText("")
//                            Log.d("childCount", wordList.childCount.toString())
//                            if (wordList.getChildAt(0) is TextView) {
//                                val child1 = wordList.getChildAt(0) as TextView
//                                Log.d("childText", child1.text.toString())
//                            }
                                val newVocab = Vocab(entry, thesEntry)
                                if (!vocabWords.containsKey(entry.hwi.hw?.replace("*", ""))) {
                                    vocabWords[entry.hwi.hw?.replace("*", "")] = newVocab
                                }
                            },
                            Response.ErrorListener {
                                resultText.text = "Request failed! Try another word!"
                                resultText.visibility = View.VISIBLE
                                wordEntry.setText("")
                            })
                        queue.add(stringRequest)
                    }, Response.ErrorListener {
                        resultText.text = "Request failed! Try another word!"
                        resultText.visibility = View.VISIBLE
                        wordEntry.setText("")
                    })
                queue.add(thesRequest)
            }
            refreshList()
        }
        val reviewButton: Button = findViewById(R.id.reviewButton)
        reviewButton.setOnClickListener{
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("words", vocabWords)
            startActivity(intent)
        }
    }

    fun refreshList() {
        val wordList: LinearLayout = findViewById(R.id.wordList)
        wordList.removeAllViews()
        for ((key, value) in vocabWords) {
            val wordText: TextView = TextView(this)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            wordText.layoutParams = lp
            wordText.gravity = Gravity.CENTER
            wordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            wordText.text = key
            wordList.addView(wordText)
        }
    }
}
