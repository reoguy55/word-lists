package com.example.word_lists

import java.io.Serializable


data class Vocab(
    val dictEntry: Entry,
    val thesEntry: ThesaurusEntry
) : Serializable