package com.example.bharath;

import com.example.bharath.response.Data;
import com.example.bharath.response.ErrorResponse;
import com.example.bharath.response.Response;
import com.example.bharath.response.SuccessResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Component
public class GetExcelDataAndImport<T> {
    private Integer totalRow;
    DbFunctions dbFunctions;

    public GetExcelDataAndImport(DbFunctions dbFunctions) {
        this.dbFunctions = dbFunctions;
    }


    public ResponseEntity<Object> importData(MultipartFile file, Boolean thread, String version) {
        Response response;
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);
            this.totalRow = sheet.getLastRowNum();

            dbFunctions.createTableAndFieldDynamically(workbook);
            ExcelReadWrite wr = new ExcelReadWrite(sheet, dbFunctions);

            try {
                if (thread) {
                    wr.writeDataUsingThreadPool();
                } else {
                    wr.writeDataWithoutThread();
                }

                response = new SuccessResponse();
                if (version.equals("v2")) {
                    response.setStatus("success");
                    response.setMessage("Data imported successfully");
                    response.setTotalRecords(totalRow);
                    ((SuccessResponse) response).setData(new Data("Data imported successfully", totalRow));

                }

            } catch (Exception e) {

                e.printStackTrace();
                response = new ErrorResponse();
                if (version.equals("v2")) {
                    if (e.getMessage().contains("column") && e.getMessage().contains("does not exist")) {

                        response.setStatus("error");
                        response.setMessage("Failed to import data");
                        ((ErrorResponse) response).setErrors(Collections.singletonList("Invalid column name in the Excel sheet"));

                    } else {
                        response.setStatus("error");
                        response.setMessage("Failed to import data");
                        ((ErrorResponse) response).setErrors(Collections.singletonList(e.getMessage()));
                        response.setTotalRecords(totalRow);
                    }

                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                } else if (version.equals("v1")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("page not Found");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            response = new ErrorResponse();
            if (version.equals("v2")) {

                response.setStatus("error");
                response.setMessage("Failed to import data");
                ((ErrorResponse) response).setErrors(Collections.singletonList(e.getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (version.equals("v1")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("page not Found");
            }


        }
        if (version.equals("v2")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (version.equals("v1")) {
            return ResponseEntity.status(HttpStatus.OK).body("Data imported successfully .");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("page not Found");
        }


    }


}

