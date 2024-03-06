package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final List<String> detail;

    @JsonCreator
    public ErrorInfo(
            @JsonProperty("url") CharSequence url,
            @JsonProperty("type") ErrorType type,
            @JsonProperty("detail") List<String> detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }

    public String toString() {
        return type + " at request " + url + ": " + detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return type == errorInfo.type &&
                Objects.equals(url, errorInfo.url) &&
                Objects.equals(detail, errorInfo.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url, detail);
    }
}