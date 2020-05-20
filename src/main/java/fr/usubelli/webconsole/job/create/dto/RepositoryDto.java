package fr.usubelli.webconsole.job.create.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryDto {

    private final String branch;
    private final String repositoryId;

    @JsonCreator
    public RepositoryDto(
            @JsonProperty("branch") String branch,
            @JsonProperty("repositoryId") String repositoryId) {
        this.branch = branch;
        this.repositoryId = repositoryId;
    }

    public String getBranch() {
        return branch;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

}
