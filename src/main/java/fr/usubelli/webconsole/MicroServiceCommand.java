package fr.usubelli.webconsole;

import com.typesafe.config.ConfigFactory;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.cli.*;

import java.io.File;

public class MicroServiceCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MicroServiceCommand.class);

    private MicroServiceCommand() {
    }

    public static Configuration parse(String[] args, String defaultConfigurationPath) throws ParseException {

        Option input = new Option("c", "config", true, "configuration file path");
        input.setRequired(false);

        Options options = new Options();
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
            formatter.printHelp("Micro Service", options);
            throw e;
        }

        String configPath = cmd.getOptionValue("config", defaultConfigurationPath);

        LOGGER.info(String.format("Trying to load configuration %s", configPath));

        final File configFile = new File(configPath);
        if (!configFile.exists()) {
            LOGGER.warn("Empty config was loaded");
            return new Configuration(ConfigFactory.empty());
        }

        LOGGER.info(String.format("Config %s loaded", configPath));

        return new Configuration(ConfigFactory.parseFile(configFile).resolve());

    }
}
