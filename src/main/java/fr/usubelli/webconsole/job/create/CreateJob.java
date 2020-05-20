package fr.usubelli.webconsole.job.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usubelli.webconsole.job.create.dto.CreateJobDto;

import java.io.File;
import java.io.IOException;

public class CreateJob {
    private final File buildFolder;
    private ObjectMapper objectMapper;

    public CreateJob(File buildFolder) {
        this.buildFolder = buildFolder;
        this.objectMapper = new ObjectMapper();
    }

    public void create(CreateJobDto createJobDto) throws CreateJobException {
        final File jobFolder = new File(buildFolder, createJobDto.getJob());
        if (!jobFolder.exists()) {
            final boolean directoryCreationResult = jobFolder.mkdirs();
            if (!directoryCreationResult) {
                throw new CreateJobException(String.format("Impossible to createOrUpdate folder %s", jobFolder.getAbsolutePath()));
            }
        }
        try {
            objectMapper.writeValue(new File(jobFolder, "definition.json"), createJobDto);
        } catch (IOException e) {
            throw new CreateJobException(e);
        }
    }
}
