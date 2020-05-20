package fr.usubelli.webconsole.job.create.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateJobDto {

    private final String project;
    private final String job;
    private final String javaId;
    private final String mavenId;
    private final String gitRepository;
    private final RepositoryDto snapshot;
    private final RepositoryDto release;

    @JsonCreator
    public CreateJobDto(
            @JsonProperty("project") String project,
            @JsonProperty("job") String job,
            @JsonProperty("javaId") String javaId,
            @JsonProperty("mavenId") String mavenId,
            @JsonProperty("gitRepository") String gitRepository,
            @JsonProperty("snapshot") RepositoryDto snapshot,
            @JsonProperty("release") RepositoryDto release) {
        this.project = project;
        this.job = job;
        this.javaId = javaId;
        this.mavenId = mavenId;
        this.gitRepository = gitRepository;
        this.snapshot = snapshot;
        this.release = release;
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

    public RepositoryDto getSnapshot() {
        return snapshot;
    }

    public RepositoryDto getRelease() {
        return release;
    }

}
