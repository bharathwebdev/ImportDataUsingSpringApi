package com.example.bharath;
import com.example.bharath.interfaces.ReadWriteFunctions;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ExcelReadWrite implements ReadWriteFunctions {
    private static final Logger logger = Logger.getLogger(ExcelReadWrite.class.getName());
    private final DbFunctions dbFunctions;
    private final Sheet sheet;


    public ExcelReadWrite(Sheet sheet, DbFunctions dbFunctions) {
        this.sheet = sheet;
        this.dbFunctions = dbFunctions;
    }


    public void writeDataWithoutThread() {
        logger.info("Writing data without thread ...");
        long startTime = System.currentTimeMillis();
        for (Row row : sheet) {
            if (row.getRowNum() != 0) {
                dbFunctions.insertRow(row.getRowNum());

            }
        }
        long endTime = System.currentTimeMillis();
        String time = String.format("Execution time : %d ms ⏰ ",(endTime - startTime));
        logger.info(time);
    }


    public void writeDataUsingThreadPool() throws Exception {

        logger.info("Writing data with thread ...");
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_POOL_COUNT);

        final int rowCount = sheet.getLastRowNum() + 1;

        for (int i = 1; i < rowCount; i++) {
            final int rowId = i;
            executorService.submit(() ->
                    dbFunctions.insertRow(rowId));
        }

        executorService.shutdown();

        while(!executorService.isTerminated()) {
            // NO-OP
        }

        long endTime = System.currentTimeMillis();
        String time = String.format("Execution time : %d ms ⏰ ",(endTime - startTime));
        logger.info(time);
    }


}
