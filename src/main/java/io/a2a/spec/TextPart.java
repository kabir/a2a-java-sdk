package io.a2a.spec;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.a2a.util.Assert;

/**
 * A fundamental text unit of an Artifact or Message.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextPart extends Part<String> {
    private String text;
    private Map<String, Object> metadata;
    private Type type;

    public TextPart(String text) {
        this(text, null);
    }

    @JsonCreator
    public TextPart(@JsonProperty("text") String text, @JsonProperty("metadata") Map<String, Object> metadata) {
        Assert.checkNotNullParam("text", text);
        this.text = text;
        this.metadata = metadata;
        this.type = Type.TEXT;
    }

    @Override
    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}