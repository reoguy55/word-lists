package com.example.word_lists.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.word_lists.R

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
        val addButton: Button = root.findViewById(R.id.addButton)
        val wordEntry: TextView = root.findViewById(R.id.wordEntry)
        val wordList: LinearLayout = root.findViewById(R.id.wordList)
        addButton.setOnClickListener { unused ->
            if (wordEntry.text.toString() != "") {
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                val newWord = TextView(context)
                newWord.layoutParams = layoutParams
//                newWord.setLayoutParams(layoutParams)
                newWord.text = wordEntry.text
//                newWord.setText(wordEntry.text)
                wordList.addView(newWord)
                wordEntry.text = ""
            }
        }
        return root
    }

}