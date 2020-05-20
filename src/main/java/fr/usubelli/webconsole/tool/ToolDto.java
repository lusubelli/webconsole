package fr.usubelli.webconsole.tool;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ToolDto {

    private final String type;
    private final String version;
    private final String os;
    private final String url;
    private final String archive;

    @JsonCreator
    public ToolDto(
            @JsonProperty("type") String type,
            @JsonProperty("version") String version,
            @JsonProperty("os") String os,
            @JsonProperty("url") String url,
            @JsonProperty("archive") String archive) {
        this.type = type;
        this.version = version;
        this.os = os;
        this.url = url;
        this.archive = archive;
    }

    public String getType() {
        return type;
    }

    public String getOs() {
        return os;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getArchive() {
        return archive;
    }

}
