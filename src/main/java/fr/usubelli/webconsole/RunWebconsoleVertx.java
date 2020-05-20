package fr.usubelli.webconsole;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.usubelli.webconsole.common.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.cli.ParseException;

import java.io.File;

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

        if (System.getenv("WEBCONSOLE") == null) {
            LOGGER.error("Missing environment variable WEBCONSOLE");
            System.exit(1);
            return;
        }

        final String buildFolderPath = System.getenv("WEBCONSOLE") + "/builds";

        final File buildFolder = new File(buildFolderPath);
        if (!buildFolder.exists()) {
            buildFolder.mkdirs();
        }

        LOGGER.info(String.format("Job folder was initialized in %s", buildFolderPath));

        final VertxMicroService microService = new WebconsoleVertx(serverObjectMapper, buildFolder);

        VertxServer.create(
                new MicroServiceConfiguration(configuration.getInt("http.port", 8080))
                        .basic(configuration.getConfiguration("http.basic", null))
                        .ssl(configuration.getConfiguration("http.ssl", null))
                        .jwt(configuration.getConfiguration("http.jwt", null)))
                .start(microService);

    }

}
