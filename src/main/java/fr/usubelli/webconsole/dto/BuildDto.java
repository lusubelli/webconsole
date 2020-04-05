package fr.usubelli.webconsole.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BuildDto {

    public enum State {
        RUNNING, SUCCESS, ERROR
    }

    private final int number;
    private final State state;
    private final Map<String, String> properties;

    @JsonCreator
    public BuildDto(
            @JsonProperty("number") int number,
            @JsonProperty("state") State state,
            @JsonProperty("properties") Map<String, String> properties) {
        this.number = number;
        this.state = state;
        this.properties = properties;
    }

    public int getNumber() {
        return number;
    }

    public State getState() {
        return state;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
