package fr.usubelli.webconsole.job.build.git;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitCloneParameters {

    private final String repository;
    private final String branch;

    @JsonCreator
    public GitCloneParameters(
            @JsonProperty("repository") String repository,
            @JsonProperty("branch") String branch) {
        this.repository = repository;
        this.branch = branch;
    }

    public String getRepository() {
        return repository;
    }

    public String getBranch() {
        return branch;
    }

}
