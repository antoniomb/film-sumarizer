package es.antoniomb.sumarizer;

import es.antoniomb.sumarizer.util.Film;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLGenerator {

    private final static Logger logger = Logger.getLogger(XMLGenerator.class.getName());
	
	static public void run(File file, String outputFile){

        logger.info("Process and generate xml file: "+outputFile+"/FilmList.xml");

        String path = file.getAbsolutePath();
        logger.info("Directory : "+ path);

        FileWriter f = setupXML(outputFile);

        File baseDir = new File(path);
        List<Film> films = new ArrayList<Film>();

        Processor.processDirectory(films, baseDir.listFiles());

        for (Film film : films)
            addRow(film, f);

        closeXML(f);

        logger.info("Films processed: " + films.size());
	}

    private static void addRow(Film film, FileWriter f)
    {
        StringBuilder str = new StringBuilder();
        str.append("\t<Film>\n");
        str.append("\t\t<Year>" + film.getYear() + "</Year>\n");
        str.append("\t\t<Title>" + film.getTitle() + "</Title>\n");
        str.append("\t\t<ImdbLink>" + film.getImdbLink() + "</ImdbLink>\n");
        str.append("\t\t<Trailer>" + film.getYoutubeLink() + "</Trailer>\n");
        str.append("\t</Film>\n");

        try
        {
            f.write(str.toString());
        }
        catch (IOException e)
        {
            logger.error("Error while writing xml", e);
        }
    }

    private static void closeXML(FileWriter f)
    {
        try
        {
            f.write("</FilmList>\n");
        }
        catch (IOException e)
        {
            logger.error("Error while creating xml", e);
        }
    }

    private static FileWriter setupXML(String outputFile)
    {
        FileWriter f = null;
        try
        {
            f = new FileWriter(outputFile+"/FilmList.xml");

            f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
            f.write("<FilmList>\n");
        }
        catch (IOException e) {
            logger.error("Error while writing xml",e);
        }
        return f;
    }

}