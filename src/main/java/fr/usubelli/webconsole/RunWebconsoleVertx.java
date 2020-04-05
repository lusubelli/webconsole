package fr.usubelli.webconsole;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class RunWebconsoleVertx {

    private static final String DEFAULT_APPLICATION_CONF = "src/main/resources/application.conf";
    private static final Logger LOGGER = LoggerFactory.getLogger(RunWebconsoleVertx.class);

    public static void main(String[] args) {

        Configuration configuration;
        try {
            configuration = MicroServiceCommand.parse(args, DEFAULT_APPLICATION_CONF);
        } catch (ParseException e) {
            System.exit(1);
            return;
        }

        if (configuration == null) {
            System.exit(1);
            return;
        }

        ObjectMapper serverObjectMapper = new ObjectMapper();
        serverObjectMapper.registerModule(new JavaTimeModule());
        serverObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        PageBuilder pageBuilder = null;
        try {
            pageBuilder = new PageBuilder();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final VertxMicroService microService = new WebconsoleVertx(serverObjectMapper, pageBuilder);

        VertxServer.create(
                new MicroServiceConfiguration(configuration.getInt("http.port", 8080))
                        .basic(configuration.getConfiguration("http.basic", null))
                        .ssl(configuration.getConfiguration("http.ssl", null))
                        .jwt(configuration.getConfiguration("http.jwt", null)))
                .start(microService);

    }

}
