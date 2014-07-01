package es.antoniomb.sumarizer;

import es.antoniomb.sumarizer.util.Film;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XLSGenerator {

    private final static Logger logger = Logger.getLogger(XLSGenerator.class.getName());

    public static String SHEET_NAME = "FilmList";

	static public void run(File file, String outputFile){

        logger.info("Process and generate xls file: "+outputFile+"/FilmList.xls");

        String path = file.getAbsolutePath();
        logger.info("Directory : " + path);

        HSSFWorkbook workbook = setupWorkbook(file);

        File baseDir = new File(path);
        List<Film> films = new ArrayList<Film>();

        Processor.processDirectory(films, baseDir.listFiles());

        for (Film film : films)
            addRow(film, workbook);

        writeWorkbook(outputFile, workbook);

        logger.info("Films processed: " + films.size());
	}

    private static HSSFWorkbook setupWorkbook(File file)
    {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)16);
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(font);

        CellStyle custom_style = workbook.createCellStyle();
        HSSFFont custom_font = workbook.createFont();
        custom_font.setFontHeightInPoints((short)14);
        custom_style.setFont(custom_font);

        CellStyle hlink_style = workbook.createCellStyle();
        HSSFFont hlink_font = workbook.createFont();
        hlink_font.setFontHeightInPoints((short)14);
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);

        HSSFSheet sheet = workbook.createSheet(SHEET_NAME);
        sheet.setDefaultRowHeightInPoints(20);
        sheet.setDefaultColumnStyle(0, custom_style);
        sheet.setDefaultColumnStyle(1, custom_style);
        sheet.setDefaultColumnStyle(2, hlink_style);
        sheet.setDefaultColumnStyle(3, hlink_style);
        sheet.setDefaultColumnStyle(4, hlink_style);

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Year");

        cell = row.createCell(1);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Title");

        cell = row.createCell(2);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("IMDB");

        cell = row.createCell(3);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Trailer");

        cell = row.createCell(4);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Oscars");

        return workbook;
    }

    private static void writeWorkbook(String outputFile, HSSFWorkbook workbook)
    {
        try
        {
            HSSFSheet sheet = workbook.getSheet(SHEET_NAME);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            FileOutputStream out =
                    new FileOutputStream(outputFile+"/FilmList.xls");

            workbook.write(out);

            out.close();
        }
        catch (FileNotFoundException e)
        {
            logger.error("Error while writing workbook",e);
        }
        catch (IOException e)
        {
            logger.error("Error while writing workbook", e);
        }
    }

    private static void addRow(Film film, HSSFWorkbook workbook)
    {
        HSSFSheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cell = row.createCell(0);
        cell.setCellValue(film.getYear());

        cell = row.createCell(1);
        cell.setCellValue(film.getTitle());

        cell = row.createCell(2);
        Hyperlink linkImdb = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        linkImdb.setAddress(film.getImdbLink());
        cell.setCellValue("IMDB");
        cell.setHyperlink(linkImdb);

        cell = row.createCell(3);
        Hyperlink linkYoutube = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        linkYoutube.setAddress(film.getYoutubeLink());
        cell.setCellValue("Trailer");
        cell.setHyperlink(linkYoutube);

        cell = row.createCell(4);
        HSSFHyperlink linkOscars = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        linkOscars.setAddress("http://www.filmaffinity.com/es/awards.php?award_id=academy_awards&year="+(film.getYear()+1)+"/");
        linkOscars.setTextMark("Oscars of year "+film.getYear());
        cell.setCellValue(String.valueOf(film.getYear()+1));
        cell.setHyperlink(linkOscars);
    }

}