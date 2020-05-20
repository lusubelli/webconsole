package fr.usubelli.webconsole.repository.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.usubelli.webconsole.repository.list.dto.RepositoryDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListRepositories {

    private final ObjectMapper objectMapper;
    private final File repositoriesFile;

    public ListRepositories() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.repositoriesFile = new File(
                System.getProperty("user.home") + "/webconsole/repositories",
                "repository.json");
    }

    public List<RepositoryDto> list() throws ListRepositoriesException {
        if (!repositoriesFile.exists()) {
            try {
                if(!repositoriesFile.createNewFile()) {
                    throw new ListRepositoriesException("");
                }
            } catch (IOException e) {
                throw new ListRepositoriesException(e);
            }
        }

        List<RepositoryDto> repositories;
        try {
            repositories = objectMapper.readValue(repositoriesFile, new TypeReference<List<RepositoryDto>>(){});
        } catch (IOException e) {
            repositories = new ArrayList<>();
        }

        return repositories;
    }

}
