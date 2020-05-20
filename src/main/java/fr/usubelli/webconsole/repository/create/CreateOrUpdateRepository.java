package fr.usubelli.webconsole.repository.create;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.usubelli.webconsole.repository.RepositoryConfiguration;
import fr.usubelli.webconsole.repository.create.dto.CreateRepositoryDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateOrUpdateRepository {

    private final ObjectMapper objectMapper;
    private final File repositoriesFile;

    public CreateOrUpdateRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.repositoriesFile = new File(
                System.getProperty("user.home") + "/webconsole/repositories",
                "repository.json");
    }

    public void createOrUpdate(CreateRepositoryDto createRepositoryDto) throws CreateRepositoryException {

        if (!repositoriesFile.exists()) {
            try {
                if(!repositoriesFile.createNewFile()) {
                    throw new CreateRepositoryException("");
                }
            } catch (IOException e) {
                throw new CreateRepositoryException(e);
            }
        }

        List<RepositoryConfiguration> repositories;
        try {
            repositories = objectMapper.readValue(repositoriesFile, new TypeReference<List<RepositoryConfiguration>>(){});
        } catch (IOException e) {
            repositories = new ArrayList<>();
        }

        final RepositoryConfiguration repositoryConfiguration = repositories.stream()
                .filter(repository -> repository.getType().equals(createRepositoryDto.getType().name())).findFirst()
                .orElse(null);
        if (repositoryConfiguration != null) {
            repositories.set(repositories.indexOf(repositoryConfiguration),
                    new RepositoryConfiguration(
                    createRepositoryDto.getUrl(),
                    createRepositoryDto.getType().name(),
                    createRepositoryDto.getName(),
                    createRepositoryDto.getUsername(),
                    createRepositoryDto.getPassword()));
        } else {
            repositories.add(new RepositoryConfiguration(
                    createRepositoryDto.getUrl(),
                    createRepositoryDto.getType().name(),
                    createRepositoryDto.getName(),
                    createRepositoryDto.getUsername(),
                    createRepositoryDto.getPassword()));
        }

        try {
            objectMapper.writeValue(repositoriesFile, repositories);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
