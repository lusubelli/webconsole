package fr.usubelli.webconsole.job.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.usubelli.webconsole.job.list.dto.JobDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListJobs {
    private final File buildFolder;
    private ObjectMapper objectMapper;

    public ListJobs(File buildFolder) {
        this.buildFolder = buildFolder;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public List<JobDto> list() throws ListJobsException {
        final File[] jobDirectories = this.buildFolder.listFiles();
        if (jobDirectories == null) {
            return new ArrayList<>();
        }
        return Stream.of(jobDirectories)
                .filter(File::isDirectory)
                .filter(jobDirectory -> new File(jobDirectory, "definition.json").exists())
                .map(jobDirectory -> {
                    JobDto jobDto = null;
                    try {
                        jobDto = objectMapper.readValue(new File(jobDirectory, "definition.json"), JobDto.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return jobDto;
                })
                .collect(Collectors.toList());
    }
}
