package com.software.tareasApp.utils;

import com.software.tareasApp.domain.service.DTO.PolizaDTO;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.Tarea;
import javafx.scene.control.TableView;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ExportarExcel {

    public static void crearExcelCliente(TableView<Cliente> table, String nombreArchivo, LocalDate desde, LocalDate hasta, String nombreReporte) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet(crearEtiqueta(desde, hasta));

        crearTitulo(spreadsheet, workbook, nombreReporte);
        crearCellsCliente(workbook, spreadsheet, table);
        crearArchivo(nombreArchivo, workbook);
    }

    public static void crearExcelTarea(TableView<Tarea> table, String nombreArchivo, String mes, String nombreReporte,String total) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet(mes);

        crearTitulo(spreadsheet, workbook, nombreReporte);
        crearCellsTarea(workbook, spreadsheet, table, total);
        crearArchivo(nombreArchivo, workbook);
    }

    public static void crearExcelPoliza(TableView<Poliza> table, String nombreArchivo, LocalDate desde, LocalDate hasta, String nombreReporte) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet(crearEtiqueta(desde,hasta));

        crearTitulo(spreadsheet, workbook, nombreReporte);
        crearCellsPoliza(workbook, spreadsheet, table);
        crearArchivo(nombreArchivo, workbook);
    }

    public static void crearExcelPolizaDTO(TableView<PolizaDTO> table, String nombreArchivo, LocalDate desde, LocalDate hasta, String nombreReporte) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet( crearEtiqueta(desde,hasta));

        crearTitulo(spreadsheet, workbook, nombreReporte);
        crearCellsPolizaDTO(workbook, spreadsheet, table);
        crearArchivo(nombreArchivo, workbook);
    }

    private static String crearEtiqueta(LocalDate desde, LocalDate hasta){
        String desdeString = UtilsGeneral.getFechaFormato(UtilsGeneral.getDateFromLocalDate(desde));
        String hastaString = UtilsGeneral.getFechaFormato(UtilsGeneral.getDateFromLocalDate(hasta));
        return desdeString + " " + hastaString;
    }

    private static void crearTitulo(Sheet spreadsheet, HSSFWorkbook workbook, String nombreReporte){
        Row titleRow = spreadsheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Cell cellTitle = titleRow.createCell(0);
        cellTitle.setCellValue("Reporte: " + nombreReporte);
        cellTitle.setCellStyle(style);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cellTitle.setCellStyle(style);
    }

    private static void crearCellsPoliza(HSSFWorkbook workbook, Sheet spreadsheet, TableView<Poliza> table){
        Row row = spreadsheet.createRow(1);
        CellStyle cellStyle = createCellStyle(workbook);

        for (int j = 0; j < table.getColumns().size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(table.getColumns().get(j).getText());
            cell.setCellStyle(cellStyle);
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 2);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if(table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                    spreadsheet.autoSizeColumn(j);
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
    }

    private static void crearCellsTarea(HSSFWorkbook workbook, Sheet spreadsheet, TableView<Tarea> table, String total){
        Row row = spreadsheet.createRow(1);
        CellStyle cellStyle = createCellStyle(workbook);

        for (int j = 0; j < table.getColumns().size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(table.getColumns().get(j).getText());
            cell.setCellStyle(cellStyle);
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 2);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if(table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                    spreadsheet.autoSizeColumn(j);
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
        row = spreadsheet.createRow(table.getItems().size()+1);
        row.createCell(table.getColumns().size()-1).
                setCellValue(total);
    }

    private static void crearCellsPolizaDTO(HSSFWorkbook workbook, Sheet spreadsheet, TableView<PolizaDTO> table){
        Row row = spreadsheet.createRow(1);
        CellStyle cellStyle = createCellStyle(workbook);

        for (int j = 0; j < table.getColumns().size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(table.getColumns().get(j).getText());
            cell.setCellStyle(cellStyle);
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 2);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if(table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                    spreadsheet.autoSizeColumn(j);
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
    }

    private static void crearCellsCliente(HSSFWorkbook workbook, Sheet spreadsheet, TableView<Cliente> table){
        Row row = spreadsheet.createRow(1);
        CellStyle cellStyle = createCellStyle(workbook);

        for (int j = 0; j < table.getColumns().size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(table.getColumns().get(j).getText());
            cell.setCellStyle(cellStyle);
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 2);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if(table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                    spreadsheet.autoSizeColumn(j);
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
    }

    private static void crearArchivo(String nombreArchivo, HSSFWorkbook workbook) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
        workbook.write(fileOut);
        fileOut.close();
    }

    private static CellStyle createCellStyle(HSSFWorkbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor myColor = palette.findSimilarColor(44, 98, 181);
        cellStyle.setFillForegroundColor(myColor.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFFont font= workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);

        return cellStyle;
    }
}
