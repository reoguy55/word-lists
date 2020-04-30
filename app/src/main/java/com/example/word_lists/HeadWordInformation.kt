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
    "hwi",
    "hw"
)
data class HeadWordInformation(
    @JsonProperty("hwi")
    var hwi: List<Any>?,
    @JsonProperty("hw")
    var hw: String? = "",
    @JsonAnySetter
    var properties: Map<String, Any>?
) : Serializable
