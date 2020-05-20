package fr.usubelli.webconsole.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryConfiguration {

    private final String url;
    private final String type;
    private final String name;
    private final String username;
    private final String password;

    @JsonCreator
    public RepositoryConfiguration(
            @JsonProperty("url") String url,
            @JsonProperty("type") String type,
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

    public String getType() {
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
