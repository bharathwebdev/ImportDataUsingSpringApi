package com.example.bharath;

import jakarta.persistence.EntityManager;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class DbFunctions {
    private Workbook workbook;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Sheet sheet;

    public void createTableAndFieldDynamically(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);
        Row tableHead = sheet.getRow(0);
        Row tableRowTypes = sheet.getRow(1);
        String tableName = sheet.getSheetName().replace(" ", "_");

        List<String> columns = new ArrayList<>();
        List<String> rowDataTypes = new ArrayList<>();

        if (tableHead.getCell(0) == null) {
            columns.add("undefined");
        }

        Iterator<Cell> cellIterator = tableHead.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (cell.toString().equalsIgnoreCase("id")) {
                columns.add(cell + "_" + cell.getColumnIndex());
            } else {
                columns.add(cell.toString().replaceAll(" ", "_").replaceAll("-", "_"));
            }
        }


        Iterator<Cell> cellIterator2 = tableRowTypes.cellIterator();
        while (cellIterator2.hasNext()) {
            Cell cell = cellIterator2.next();

            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                rowDataTypes.add("VARCHAR(255)");
            } else if (cell.getCellType() == CellType.NUMERIC) {
                rowDataTypes.add("INT");
            } else if (cell.getCellType() == CellType.STRING) {
                rowDataTypes.add("VARCHAR(255)");
            } else {
                rowDataTypes.add("VARCHAR(255)");
            }
        }

        StringBuilder alterQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (id SERIAL PRIMARY KEY,");

        for (int i = 0; i < tableHead.getLastCellNum(); i++) {
            alterQuery.append(columns.get(i)).append(" ").append(rowDataTypes.get(i));
            if (i != tableHead.getLastCellNum() - 1) {
                alterQuery.append(",");
            } else {
                alterQuery.append(")");
            }
        }
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(status -> {

            String createTableQuery = alterQuery.toString();
            entityManager.createNativeQuery(createTableQuery).executeUpdate();

            String addFieldQuery = String.valueOf(alterQuery);
            entityManager.createNativeQuery(addFieldQuery).executeUpdate();
            System.out.println("Table Created  ✅");
            return null;

        });
    }


    public void insertRow(int index) {

        Row tableHead = sheet.getRow(0);
        Row values = sheet.getRow(index);
        int length = tableHead.getLastCellNum();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        String tableName = sheet.getSheetName().replace(" ", "_");

        StringBuilder insertQuery = new StringBuilder(" INSERT INTO " + tableName + " (");
        for (int i = 0; i < length; i++) {
            String cell;
            if (tableHead.getCell(i) == null) {
                cell = "undefined";
            } else {
                cell = tableHead.getCell(i).toString().replaceAll(" ", "_").replaceAll("-", "_");
            }

            if (cell.equalsIgnoreCase("id")) {
                cell = cell + "_" + i;
            }
            if (cell.equals("null")) {
                cell = "undefined";
            }
            if (i == tableHead.getLastCellNum() - 1) {
                insertQuery.append(cell).append(") VALUES (");
            } else {
                insertQuery.append(cell).append(",");
            }

        }


        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        for (int i = 0; i < length; i++) {
            if (values.getCell(i) == null) {
                return;
            }
            String rowValue = values.getCell(i).toString().replace("'", "''").replaceAll("-", " ");

            String value = switch (values.getCell(i).getCellType()) {
                case _NONE -> "none";
                case NUMERIC -> rowValue;
                case STRING -> "'" + rowValue + "'";
                case FORMULA -> evaluator.evaluate(values.getCell(i)).formatAsString();
                case BLANK -> "blank";
                case BOOLEAN -> "true";
                case ERROR -> "error";
            };

            if (values.getCell(i).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(values.getCell(i))) {

                value = "'" + rowValue + "'";
            }

            if (i == length - 1) {
                insertQuery.append(value).append(")");
            } else {
                insertQuery.append(value).append(" , ");
            }
        }
        System.out.println(insertQuery);


        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery(String.valueOf(insertQuery)).executeUpdate();
            return null;
        });

    }
}
