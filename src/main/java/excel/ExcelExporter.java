package excel;

import model.Event;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporter {

    public void export(List<Event> events, List<String> logs, String fileName) {

        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle liveStyle = createStyle(workbook, IndexedColors.LIGHT_GREEN);
            CellStyle finishedStyle = createStyle(workbook, IndexedColors.GREY_25_PERCENT);
            CellStyle upcomingStyle = createStyle(workbook, IndexedColors.LIGHT_YELLOW);

            Map<String, List<Event>> grouped = events.stream()
                    .sorted(Comparator.comparing(Event::getTime))
                    .collect(Collectors.groupingBy(Event::getSport));

            for (String sport : grouped.keySet()) {

                Sheet sheet = workbook.createSheet(sport);
                createHeader(sheet);

                int rowNum = 1;

                for (Event e : grouped.get(sport)) {

                    Row row = sheet.createRow(rowNum++);

                    CellStyle style = switch (e.getStatus()) {
                        case "В процессе" -> liveStyle;
                        case "Закончен" -> finishedStyle;
                        default -> upcomingStyle;
                    };

                    createCell(row, 0, e.getTeam1(), style);
                    createCell(row, 1, e.getTeam2(), style);
                    createCell(row, 2, e.getTime(), style);
                    createCell(row, 3, e.getScore(), style);
                    createCell(row, 4, e.getStatus(), style);
                }

                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            createLogsSheet(workbook, logs);

            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
            }

            System.out.println("Excel обновлён: " + fileName);

        } catch (Exception e) {
            System.out.println("Ошибка Excel: " + e.getMessage());
        }
    }

    private void createLogsSheet(Workbook workbook, List<String> logs) {

        Sheet sheet = workbook.createSheet("Logs");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Time");
        header.createCell(1).setCellValue("Message");

        int rowNum = 1;

        for (String log : logs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(LocalDateTime.now().toString());
            row.createCell(1).setCellValue(log);
        }
    }

    private CellStyle createStyle(Workbook wb, IndexedColors color) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Team 1");
        header.createCell(1).setCellValue("Team 2");
        header.createCell(2).setCellValue("Time");
        header.createCell(3).setCellValue("Score");
        header.createCell(4).setCellValue("Status");
    }
}