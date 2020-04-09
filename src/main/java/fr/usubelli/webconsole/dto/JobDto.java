package fr.usubelli.webconsole.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class JobDto {

    private final String name;
    private final String type;
    @JsonIgnore
    private final List<BuildDto> builds;
    private final Map<String, String> properties;

    @JsonCreator
    public JobDto(
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("builds") List<BuildDto> builds,
            @JsonProperty("properties") Map<String, String> properties) {
        this.name = name;
        this.type = type;
        this.builds = builds;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<BuildDto> getBuilds() {
        return builds;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
