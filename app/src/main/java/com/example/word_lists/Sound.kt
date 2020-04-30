package com.example.word_lists
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "audio",
    "ref",
    "stat"
)
data class Sound(
    @JsonProperty("audio")
    val audio: String,
    @JsonProperty("ref")
    val ref: String,
    @JsonProperty("stat")
    val stat: String,
    @JsonAnySetter
    var properties: Map<String, String>
) : Serializable