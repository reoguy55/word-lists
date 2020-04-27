package com.example.word_lists

import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern

data class Entry (

    @SerializedName("hwi")
    val info: HeadWordInformation,
//    val word: this.info.hw
    @SerializedName("shortdef")
    val definitions: Array<String>,
    @SerializedName("syns")
    val synonyms: Synonyms,
    @SerializedName("prs")
    val prons: Pronunciations
//    val audioTag: String
) {
    override fun toString(): String {
        val syn0 = synonyms.pt?.getOrElse(0) {"None Found!"}
        val syn1 = synonyms.pt?.getOrElse(1) {""}
        val syn2 = synonyms.pt?.getOrElse(2) {""}
        return "Word: ${this.info.hw}\nDefinition: ${this.definitions[0]}\nSynonyms: ${syn0}, ${syn1}, ${syn2}"
    }

    fun getSoundFileURL(): String {
        var subDir = ""
        val audioTag = prons.sound?.audio
        if (audioTag == null) {
            return ""
        }
        subDir = when {
            audioTag?.startsWith("bix", true) -> {
                "bix"
            }
            audioTag.startsWith("gg", true) -> {
                "gg"
            }
            Pattern.matches("\\p{Punct}", audioTag.substring(0, 1)) -> {
                "number"
            }
            else -> {
                audioTag.substring(0, 1)
            }
        }
        return "https://media.merriam-webster.com/soundc11/${subDir}/${audioTag}.wav"
    }
}