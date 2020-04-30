package com.example.word_lists.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.word_lists.Entry
import com.example.word_lists.R
import com.example.word_lists.ThesaurusEntry
import com.example.word_lists.Vocab
import com.fasterxml.jackson.module.kotlin.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        textView.visibility = View.GONE
        val addButton: Button = root.findViewById(R.id.addButton)
        val wordEntry: EditText = root.findViewById(R.id.wordEntry)
        val wordList: LinearLayout = root.findViewById(R.id.wordList)
        val queue = Volley.newRequestQueue(context)
        val dictBase = "https://dictionaryapi.com/api/v3/references/collegiate/json/"
        val thesBase = "https://dictionaryapi.com/api/v3/references/thesaurus/json/"
        val dictKey = context?.getString(R.string.dict_api_key)
        val thesKey = context?.getString(R.string.thes_api_key)
        val resultText: TextView = root.findViewById(R.id.resultText)
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
                    val stringRequest = StringRequest(Request.Method.GET, dictURL,
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
                            val newWord = TextView(context)
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
        }
        return root
    }

}