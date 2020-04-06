package fr.usubelli.webconsole.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class JobDto {

    private final String name;
    private final List<BuildDto> builds;
    private final Map<String, String> properties;

    @JsonCreator
    public JobDto(
            @JsonProperty("name") String name,
            @JsonProperty("builds") List<BuildDto> builds,
            @JsonProperty("properties") Map<String, String> properties) {
        this.name = name;
        this.builds = builds;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public List<BuildDto> getBuilds() {
        return builds;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
