package fr.usubelli.webconsole.repository.create.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRepositoryDto {

    public enum Type {
        SNAPSHOT, RELEASE
    }
    private final String url;
    private final Type type;
    private final String name;
    private final String username;
    private final String password;

    @JsonCreator
    public CreateRepositoryDto(
            @JsonProperty("url") String url,
            @JsonProperty("type") Type type,
            @JsonProperty("name") String name,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password) {
        this.url = url;
        this.type = type;
        this.name = name;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

}
