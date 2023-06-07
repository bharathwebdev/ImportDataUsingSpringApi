package com.example.bharath;

import com.example.bharath.interfaces.ReadWriteFunctions;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExcelReadWrite implements ReadWriteFunctions {
    private final DbFunctions dbFunctions;
    private final Sheet sheet;
    private static final int THREAD_POOL_COUNT = Runtime.getRuntime().availableProcessors();

    public ExcelReadWrite(Sheet sheet, DbFunctions dbFunctions) {
        this.sheet = sheet;
        this.dbFunctions = dbFunctions;
    }


    public void writeDataWithoutThread() {
        System.out.println("Writing Data Without Thread ...");
        long startTime = System.currentTimeMillis();
        for (Row row : sheet) {
            if (row.getRowNum() != 0) {
                dbFunctions.insertRow(row.getRowNum());

            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms ⏰");
    }


    public void writeDataUsingThreadPool() throws Exception {

        System.out.println("Writing Data With Thread ...");
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_COUNT);
        List<Future<?>> futures = new ArrayList<>();

        final int rowCount = sheet.getLastRowNum() + 1;


        for (int i = 1; i < rowCount; i++) {
            final int rowId = i;
        Future<?> future  =   executorService.submit(() -> {
                    dbFunctions.insertRow(rowId);
            });

            futures.add(future);

        }

        for (Future<?> future : futures) {
         future.get();// Blocks until the task is complete and returns the result
        }


        executorService.shutdown();


        // Wait for all tasks to complete
        boolean tasksCompleted = executorService.awaitTermination(10, TimeUnit.MINUTES);

        if (tasksCompleted) {
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time : " + (endTime - startTime) + " ms ⏰ ");
        } else {
            System.out.println("Timeout occurred before all tasks completed.");
        }
    }


}
