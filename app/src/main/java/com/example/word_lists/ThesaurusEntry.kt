package com.example.word_lists

import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

data class ThesaurusEntry (
    @JsonProperty("meta")
    var meta: Synonyms?,
    @JsonAnySetter
    var properties: Map<String, Any>?
)