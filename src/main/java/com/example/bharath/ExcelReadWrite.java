package com.example.bharath;


import com.example.bharath.interfaces.ReadWriteFunctions;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExcelReadWrite implements ReadWriteFunctions {
    private final Connection conn;
    private final PostgresDb db;
    private final Sheet sheet;
    private static final int THREAD_POOL_COUNT = Runtime.getRuntime().availableProcessors();

    public ExcelReadWrite(PostgresDb db, Connection conn,Sheet sheet) {
        this.conn = conn;
        this.db = db;
        this.sheet = sheet;

    }


    public void writeDataWithoutThread() {
        System.out.println("Writing Data Without Thread ...");
        long startTime = System.currentTimeMillis();
        try {
            for (Row row : sheet) {
                db.insertRow(conn,row.getRowNum(), sheet);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms ⏰");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeDataUsingThreadPool() {
        System.out.println("Writing Data With Thread ...");
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_COUNT);
        final int rowCount = sheet.getLastRowNum() + 1;

        for (int i = 0; i < rowCount; i++) {
            final int rowId = i;
            executorService.submit(() -> db.insertRow(conn,rowId, sheet));
        }
        executorService.shutdown();

        try {
            // Wait for all tasks to complete
            boolean tasksCompleted = executorService.awaitTermination(10, TimeUnit.MINUTES);

            if (tasksCompleted) {
                long endTime = System.currentTimeMillis();
                System.out.println("Execution time : " + (endTime - startTime) + " ms ⏰ ");
            } else {
                System.out.println("Timeout occurred before all tasks completed.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
