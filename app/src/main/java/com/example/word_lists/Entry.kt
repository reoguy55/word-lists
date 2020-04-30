package com.example.word_lists

import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

data class Entry (

    @JsonProperty("hwi")
    val hwi: HeadWordInformation,
    @JsonProperty("shortdef")
    val definitions: List<Any>?,
//    @JsonProperty("syns")
//    val synonyms: Synonyms?,
    @JsonProperty("prs")
    val prons: Pronunciations?,
    @JsonAnySetter
    var properties: Map<String, Any>?
) {
    override fun toString(): String {
//        val syn0 = synonyms?.pt?.getOrElse(0) {"None Found!"}
//        val syn1 = synonyms?.pt?.getOrElse(1) {""}
//        val syn2 = synonyms?.pt?.getOrElse(2) {""}
        return "Word: ${this.hwi.hw?.replace("*", "")}\nDefinition: ${this.definitions?.get(0)}"
    }

    fun getSoundFileURL(): String {
        val audioTag: String = prons?.sound?.audio ?: ""
        val subDir = when {
            audioTag.startsWith("bix", true) -> {
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