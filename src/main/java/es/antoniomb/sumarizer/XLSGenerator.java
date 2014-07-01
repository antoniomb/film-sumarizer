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

        HSSFSheet sheet = workbook.createSheet(SHEET_NAME);

        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(font);

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("Year");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(1);
        cell.setCellValue("Title");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(2);
        cell.setCellValue("ImdbLink");
        cell.setCellStyle(boldStyle);

        cell = row.createCell(3);
        cell.setCellValue("Trailer");
        cell.setCellStyle(boldStyle);

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
        CreationHelper creationHelper = workbook.getCreationHelper();

        CellStyle hlink_style = workbook.createCellStyle();
        Font hlink_font = workbook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);

        HSSFSheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cell = row.createCell(0);
        cell.setCellValue(film.getYear());

        cell = row.createCell(1);
        cell.setCellValue(film.getTitle());

        cell = row.createCell(2);
        Hyperlink link = creationHelper.createHyperlink(HSSFHyperlink.LINK_URL);
        link.setAddress(film.getImdbLink());
        cell.setHyperlink(link);
        cell.setCellValue("IMDB");
        cell.setCellStyle(hlink_style);

        cell = row.createCell(3);
        link = creationHelper.createHyperlink(HSSFHyperlink.LINK_URL);
        link.setAddress(film.getYoutubeLink());
        cell.setHyperlink(link);
        cell.setCellValue("Youtube - trailer");
        cell.setCellStyle(hlink_style);
    }

}