package com.example.bharath;
import com.example.bharath.response.Data;
import com.example.bharath.response.ErrorResponse;
import com.example.bharath.response.Response;
import com.example.bharath.response.SuccessResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class BharathApplication {
    DbFunctions dbFunctions ;

    public BharathApplication(DbFunctions dbFunctions) {
        this.dbFunctions = dbFunctions;
    }

    public static void main(String[] args) {
        SpringApplication.run(BharathApplication.class, args);
    }




}



