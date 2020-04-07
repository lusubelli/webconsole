package fr.usubelli.webconsole;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

public class RunWebconsoleVertx {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunWebconsoleVertx.class);

    public static void main(String[] args) {

        Configuration configuration;
        try {
            configuration = MicroServiceCommand.parse(args,
                    "src/main/resources/config/application.conf");
        } catch (ParseException e) {
            System.exit(1);
            return;
        }

        ObjectMapper serverObjectMapper = new ObjectMapper();
        serverObjectMapper.registerModule(new JavaTimeModule());
        serverObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        final String templateFolderPath = configuration.getString("template", "src/main/resources/ftlh");

        PageBuilder pageBuilder;
        try {
            pageBuilder = new PageBuilder(new File(templateFolderPath));
            LOGGER.info(String.format("Template folder %s was loaded", templateFolderPath));
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot find template folder %s", templateFolderPath));
            System.exit(1);
            return;
        }

        final String buildFolderPath = System.getProperty("user.home") + "/webconsole/builds";

        final File buildFolder = new File(buildFolderPath);
        if (!buildFolder.exists()) {
            buildFolder.mkdirs();
        }

        final String binFolderPath = configuration.getString("bin", "src/main/resources/bin");

        LOGGER.info(String.format("Build folder was initialized in %s", buildFolderPath));

        final VertxMicroService microService = new WebconsoleVertx(serverObjectMapper, pageBuilder,
                new File(binFolderPath),
                buildFolder);

        VertxServer.create(
                new MicroServiceConfiguration(configuration.getInt("http.port", 8080))
                        .basic(configuration.getConfiguration("http.basic", null))
                        .ssl(configuration.getConfiguration("http.ssl", null))
                        .jwt(configuration.getConfiguration("http.jwt", null)))
                .start(microService);

    }

}
