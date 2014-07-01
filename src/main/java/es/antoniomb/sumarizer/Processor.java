package es.antoniomb.sumarizer;

import es.antoniomb.sumarizer.util.Film;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Processor {

    private final static Logger logger = Logger.getLogger(Processor.class.getName());

    public static void processDirectory(List<Film> films, File[] files)
    {
        logger.info("Files to process: " + files.length);

        for (File file : files)
        {
            String filename = file.getName();
            logger.info("Processing file " + filename);

            if (file.isDirectory())
                processDirectory(films, file.listFiles());

            if (filename.contains("]"))
            {
                Film film = new Film();

                String title = filename.substring(filename.lastIndexOf("]") + 2, filename.lastIndexOf("."));
                String year = filename.substring(1, filename.lastIndexOf("]"));

                if (title.contains(" CD1"))
                    title = title.substring(0, title.indexOf(" CD1"));
                else if (title.contains(" CD2"))
                    continue;

                String extension = filename.substring(filename.lastIndexOf("."));
                if (extension.equals(".srt") || extension.equals(".sub"))
                    continue;

                film.setTitle(title);
                film.setYear(Integer.valueOf(year));
                film.setImdbLink("http://www.imdb.com/find?s=tt&amp;q=" + title);
                film.setYoutubeLink("http://www.youtube.com/results?search_query=" + title.replace(' ', '+') + "+trailer");

                films.add(film);
            }
        }

        sortFilms(films);
    }

    private static void sortFilms(List<Film> films)
    {
        Comparator<Film> comparator = new Comparator<Film>() {
            public int compare(Film f1, Film f2) {
                return f1.getYear() - f2.getYear();
            }
        };

        Collections.sort(films,comparator);
    }

}
