package fr.usubelli.webconsole.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProjectDto {

    private final String name;
    private final List<JobDto> jobs;

    @JsonCreator
    public ProjectDto(
            @JsonProperty("name") String name,
            @JsonProperty("jobs") List<JobDto> jobs) {
        this.name = name;
        this.jobs = jobs;
    }

    public boolean addJob(JobDto job) {
        return this.jobs.add(job);
    }

    public String getName() {
        return name;
    }

    public List<JobDto> getJobs() {
        return jobs;
    }

}
