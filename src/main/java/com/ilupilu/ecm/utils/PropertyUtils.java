package com.ilupilu.ecm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Kishore on 11-08-2014.
 */
public class PropertyUtils {
    private static Properties properties = new Properties();
    private static final Logger log = LoggerFactory.getLogger(PropertyUtils.class);
    static{
        String path = "./VideoConverter.properties";
        try {
            log.info("Loading property files.");

            log.debug("Load inner property file.");
            InputStream stream=PropertyUtils.class.getResourceAsStream("/app.properties");
            if(stream!=null)
                properties.load(stream);

            log.debug("Load external property file and merge.");
            stream = new FileInputStream(path);
            if(stream!=null)
                properties.load(stream);

        } catch (IOException e) {
            log.warn("Exception: ",e);
        }
    }

    public static String get(String property)
    {
        return properties.getProperty(property);
    }
}
