package com.sudoku.core.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Klasa zarządzająca plikami konfiguracyjnymi.
 */
public class ConfigUtil {
    public static String SUFFIX = ".conf";
    private static Logger LOGGER = Logger.getLogger(ConfigUtil.class);
    private static Config config = null;

    /**
     * Wczytuje plik z ustawieniami.
     *
     * @param appName nazwa aplikacji i jednocześnie przedrostek pliku konfiguracyjnego
     */
    public static void init(String appName) {
        String fullFileName = appName + ConfigUtil.SUFFIX;
        //load default
        config = ConfigFactory.defaultOverrides();
        //load file from the same directory
        File configFile = new File(fullFileName);
        if (configFile.exists()) {
            LOGGER.info("Loading user local config file.");
            config = config.withFallback(ConfigFactory.parseFile(configFile));
        }
        //load file from system property
        String systemPath = System.getProperty(fullFileName);
        if (systemPath != null) {
            configFile = new File(systemPath);
            if (configFile.exists()) {
                LOGGER.info("Loading user config from system env file.");
                config = config.withFallback(ConfigFactory.parseFile(configFile));
            }
        }
        //load default from resource
        LOGGER.info("Loading default config from resources.");
        config = config.withFallback(ConfigFactory.load(fullFileName));
    }

    public static Integer getInt(String propertyName) {
        return ConfigUtil.getInt(propertyName, null);
    }

    public static Integer getInt(String propertyName, Integer defaultValue) {
        Integer result = null;
        try {
            result = config.getInt(propertyName);
        } catch (ConfigException ex) {
            result = defaultValue;
        }
        return result;
    }

    public static String getString(String propertyName) {
        return getString(propertyName, "");
    }

    public static String getString(String propertyName, String defaultValue) {
        String result = null;
        try {
            result = config.getString(propertyName);
        } catch (ConfigException ex) {
            result = defaultValue;
        }
        return result;
    }
}
