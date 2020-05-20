package fr.usubelli.webconsole.job.list.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class BuildDto {

    private final String id;
    private final String jobName;
    private final String javaId;
    private final String mavenId;
    private final String gitRepository;
    private final String branch;
    private final String repositoryId;
    private final String gitTag;
    private final LocalDateTime startTime;
    private final String commitMessage;
    public enum State {
        UNKONW,
        CREATED,
        STARTING,
        FAILURE,
        SUCCESS
    }
    private fr.usubelli.webconsole.job.build.dto.BuildDto.State state;

    @JsonCreator
    public BuildDto(
            @JsonProperty("id") String id,
            @JsonProperty("jobName") String jobName,
            @JsonProperty("javaId") String javaId,
            @JsonProperty("mavenId") String mavenId,
            @JsonProperty("gitRepository") String gitRepository,
            @JsonProperty("branch") String branch,
            @JsonProperty("repositoryId") String repositoryId,
            @JsonProperty("gitTag") String gitTag,
            @JsonProperty("startTime") LocalDateTime startTime,
            @JsonProperty("commitMessage") String commitMessage,
            @JsonProperty("state") fr.usubelli.webconsole.job.build.dto.BuildDto.State state) {
        this.id = id;
        this.jobName = jobName;
        this.javaId = javaId;
        this.mavenId = mavenId;
        this.gitRepository = gitRepository;
        this.branch = branch;
        this.repositoryId = repositoryId;
        this.gitTag = gitTag;
        this.startTime = startTime;
        this.commitMessage = commitMessage;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public fr.usubelli.webconsole.job.build.dto.BuildDto.State getState() {
        return state;
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

    public String getBranch() {
        return branch;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public String getGitTag() {
        return gitTag;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public String getJobName() {
        return jobName;
    }

}
