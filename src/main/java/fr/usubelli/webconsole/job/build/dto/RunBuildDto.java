package fr.usubelli.webconsole.job.build.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunBuildDto {

    private final String project;
    private final String job;
    private final String javaId;
    private final String mavenId;
    private final String gitRepository;
    private final RepositoryDto repository;
    private final String gitTag;

    @JsonCreator
    public RunBuildDto(
            @JsonProperty("project") String project,
            @JsonProperty("job") String job,
            @JsonProperty("javaId") String javaId,
            @JsonProperty("mavenId") String mavenId,
            @JsonProperty("gitRepository") String gitRepository,
            @JsonProperty("repository") RepositoryDto repository,
            @JsonProperty("gitTag") String gitTag) {
        this.project = project;
        this.job = job;
        this.javaId = javaId;
        this.mavenId = mavenId;
        this.gitRepository = gitRepository;
        this.repository = repository;
        this.gitTag = gitTag;
    }

    public String getProject() {
        return project;
    }

    public String getJob() {
        return job;
    }

    public String getJavaId() {
        return javaId;
    }

    public String getMavenId() {
        return mavenId;
    }

    public String getGitRepository() {
        return gitRepository;
    }

    public RepositoryDto getRepository() {
        return repository;
    }

    public String getGitTag() {
        return gitTag;
    }

}
