package fr.usubelli.webconsole.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigValue;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final Config config;

    public Configuration(Config config) {
        this.config = config;
    }

    public String getString(String key) {
        String value;
        try {
            value = this.config.getString(key);
        } catch (ConfigException.Missing e) {
            value = null;
        }
        return value;
    }

    public String getString(final String key, final String defaultValue) {
        String value;
        try {
            value = this.config.getString(key);
        } catch (ConfigException.Missing e) {
            value = defaultValue;
        }
        return value;
    }

    public int getInt(final String key, final int defaultValue) {
        int value;
        try {
            value = this.config.getInt(key);
        } catch (ConfigException.Missing e) {
            value = defaultValue;
        }
        return value;
    }

    public Configuration getConfiguration(final String key) {
        Configuration value;
        try {
            value = new Configuration(this.config.getConfig(key));
        } catch (ConfigException.Missing e) {
            value = null;
        }
        return value;
    }

    public Configuration getConfiguration(final String key, final Configuration defaultValue) {
        Configuration value;
        try {
            value = new Configuration(this.config.getConfig(key));
        } catch (ConfigException.Missing e) {
            value = defaultValue;
        }
        return value;
    }

}
