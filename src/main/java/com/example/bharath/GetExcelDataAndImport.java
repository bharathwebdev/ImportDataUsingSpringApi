package com.example.bharath;

import com.example.bharath.response.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;


@Component
public class GetExcelDataAndImport {
    private Integer totalRow;
    private final DbFunctions dbFunctions;

    public GetExcelDataAndImport(DbFunctions dbFunctions) {
        this.dbFunctions = dbFunctions;
    }


    public ResponseEntity<Object> importData(MultipartFile file, Boolean thread, String version) {

        SuccessResponse successResponse;

        ErrorResponse errorResponse;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);
            this.totalRow = sheet.getLastRowNum();

            dbFunctions.createTableAndFieldDynamically(workbook);
            ExcelReadWrite wr = new ExcelReadWrite(sheet, dbFunctions);


            try {

                if ((Boolean.TRUE.equals(thread))) {
                    wr.writeDataUsingThreadPool();
                } else {
                    wr.writeDataWithoutThread();
                }


                successResponse = new SuccessResponse();

                if (version.equals("v2")) {
                    successResponse = createSuccessResponse(totalRow);
                    successResponse.setData(new Data(Constants.SUCCESS_IMPORT, totalRow));
                }


            } catch (Exception e) {

                e.printStackTrace();
                errorResponse = createErrorResponse(e);

                if (version.equals("v2")) {
                    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
                } else if (version.equals("v1")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.NOT_FOUND);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            errorResponse = createErrorResponse(e);
            if (version.equals("v2")) {
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            } else if (version.equals("v1")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.NOT_FOUND);
            }
        }


        if (version.equals("v2")) {
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } else if (version.equals("v1")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Constants.SUCCESS_IMPORT);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.NOT_FOUND);
        }


    }

    private SuccessResponse createSuccessResponse(Integer totalRow) {
        SuccessResponse response = new SuccessResponse();
        response.setStatus(Constants.SUCCESS);
        response.setMessage(Constants.SUCCESS_IMPORT);
        response.setTotalRecords(totalRow);
        return response;
    }


    private ErrorResponse createErrorResponse(Exception e) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(Constants.ERROR);
        response.setErrors(Collections.singletonList(e.getMessage()));
        if (e.getMessage().contains("column") && e.getMessage().contains("does not exist")) {
            response.setErrors(Collections.singletonList("Invalid column name in the Excel sheet"));
        } else {
            response.setErrors(Collections.singletonList(e.getMessage()));
        }
        return response;
    }


}

