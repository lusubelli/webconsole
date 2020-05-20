package fr.usubelli.webconsole.repository.list.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryDto {

    public enum Type {
        SNAPSHOT, RELEASE
    }
    private final String url;
    private final Type type;
    private final String name;
    private final String username;

    @JsonCreator
    public RepositoryDto(
            @JsonProperty("url") String url,
            @JsonProperty("type") Type type,
            @JsonProperty("name") String name,
            @JsonProperty("username") String username) {
        this.url = url;
        this.type = type;
        this.name = name;
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

}
