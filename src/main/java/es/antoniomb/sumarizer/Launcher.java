package es.antoniomb.sumarizer;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Launcher for FilmSumarizer
 *
 * Get from execution path or properties file (if defined) path to process film files
 *
 * Files must have the format "[YYYY] Title"
 *
 * args[0] = config.properties path
 */
public class Launcher
{
    final static Logger logger = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args)
	{
        logger.info("** **************************************** **");
        logger.info("** XML/XLS film processor for listing films **");
        logger.info("** Files must match syntax  \"[YYYY] Title\"  **");
        logger.info("** **************************************** **");

        String path = System.getProperty("user.dir"); //Default
        String outputFile = path;

        //Read from config.properties if exists
        try
        {
            String configPath = "src/main/resources/config.properties";
            if (args.length == 1) {
                configPath = args[0];
            }

            Properties prop = new Properties();
            InputStream input = new FileInputStream(configPath);
            prop.load(input);
            path = prop.getProperty("path");
            outputFile = prop.getProperty("outputPath");
        }
        catch (IOException ex)
        {
            logger.warn("Error loading config file, current path will be processed (" + path + ")");
        }

        File file = new File(path);

		XMLGenerator.run(file, outputFile);
        XLSGenerator.run(file, outputFile);
	}
	
}
