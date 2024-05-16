package com.tpsolution.animestore.utils;

import com.tpsolution.animestore.payload.DataOrderDetailResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;


public class ExcelGenerator {
    private List<DataOrderDetailResponse> dataOrderDetailResponses;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator(List<DataOrderDetailResponse> paramListData) {
        this.dataOrderDetailResponses = paramListData;
        workbook = new XSSFWorkbook();
    }
    private void writeHeader() {
        sheet = workbook.createSheet("SHEET1");

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(13);
        font.setFontName("Times New Roman");
        style.setFont(font);

        Row row = sheet.createRow(0);
        createCell(row, 2, "HÓA ĐƠN BÁN LẺ", style);

        Row row2 = sheet.createRow(1);
        createCell(row2, 0, "STT", style);
        createCell(row2, 1, "TÊN SẢN PHẨM", style);
        createCell(row2, 2, "SỐ LƯỢNG", style);
        createCell(row2, 3, "ĐƠN GIÁ", style);
        createCell(row2, 4, "THÀNH TIỀN", style);

    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        }else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
    private void write() {

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        font.setFontName("Times New Roman");
        font.setBold(false);
        style.setFont(font);

        int rowCount = 2;
        int stt = 1;
        Double subTotal = 0.0;
        for (DataOrderDetailResponse record: dataOrderDetailResponses) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, stt , style);
            createCell(row, columnCount++, record.getProductName(), style);
            createCell(row, columnCount++, record.getAmount(), style);
            createCell(row, columnCount++, record.getUnitPrice(), style);
            createCell(row, columnCount++, record.getSubTotal(), style);
            subTotal += record.getSubTotal();
            stt++;
        }

        CellStyle style2 = workbook.createCellStyle();
        XSSFFont font2 = workbook.createFont();
        font2.setBold(true);
        font2.setFontHeight(13);
        font2.setFontName("Times New Roman");
        style2.setFont(font2);

        Row row2 = sheet.createRow(rowCount);
        createCell(row2, 0, "CỘNG", style2);
        createCell(row2, 4, subTotal, style2);
    }
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}

