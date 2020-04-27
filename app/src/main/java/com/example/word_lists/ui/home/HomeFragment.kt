package com.example.word_lists.ui.home

import android.os.Bundle
import android.util.Log
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
import com.example.word_lists.MainActivity
import com.example.word_lists.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.sql.SQLOutput

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
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        textView.visibility = View.GONE
        val addButton: Button = root.findViewById(R.id.addButton)
        val wordEntry: EditText = root.findViewById(R.id.wordEntry)
        val wordList: LinearLayout = root.findViewById(R.id.wordList)
        val queue = Volley.newRequestQueue(context)
        val url = "https://dictionaryapi.com/api/v3/references/collegiate/json/"
        val dictKey = context?.getString(R.string.dict_api_key)
//        val thesKey = context?.getString(R.string.thes_api_key)
        val resultText: TextView = root.findViewById(R.id.resultText)
        addButton.setOnClickListener { unused ->
            if (wordEntry.text.toString() != "") {
                val dictURL = url + wordEntry.text.toString() + "?key=" + dictKey
                //send request here
                val stringRequest = StringRequest(Request.Method.GET, dictURL,
                    Response.Listener { response ->
                        // Display the first 500 characters of the response string.
//                        resultText.text = "Response is: ${response.substring(0, 500)}"
                        val gson = Gson()
//                        System.out.println("first character of response: " + response[0])
                        Log.d("response", response)
                        val trim1 = response.substring(1)
//                        System.out.println("first character of trim1: " + trim1[0])
//                        System.out.println("last character of trim1: " + trim1[trim1.length - 1])
                        val trim2 = trim1.substring(0, trim1.length - 1)
                        System.out.println(trim2)
//                        System.out.println("first character of trim2: " + trim2[0])
//                        System.out.println("last character of trim2: " + trim2[trim2.length - 1])
//                        System.out.println(trim2)
//                        val result: String = gson.toJson(trim2)
//                        val newEntry = gson.fromJson(result, Entry::class.java)
//                        resultText.text = "Request successful!\n${newEntry}"
//                        resultText.visibility = View.VISIBLE
//                        val layoutParams = LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT)
//                        val newWord = TextView(context)
//                        newWord.layoutParams = layoutParams
////                newWord.setLayoutParams(layoutParams)
////                newWord.text = wordEntry.text
//                        newWord.text = newEntry.info.hw
//                        wordList.addView(newWord)
//                        wordEntry.setText("")
                    },
                    Response.ErrorListener {
                        resultText.text = "Request failed! Try another word!"
                        resultText.visibility = View.VISIBLE
                        wordEntry.setText("")
                    })
                queue.add(stringRequest)
            }
        }
        return root
    }

}