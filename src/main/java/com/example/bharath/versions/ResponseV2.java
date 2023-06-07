package com.example.bharath.versions;

import com.example.bharath.GetExcelDataAndImport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v2/api")
public class ResponseV2 {

  GetExcelDataAndImport<Object> excelDataAndImport ;

    public ResponseV2(GetExcelDataAndImport<Object> excelDataAndImport) {
        this.excelDataAndImport = excelDataAndImport;
    }

    @GetMapping("/")
    public String Home() {
        return "Home page";
    }

    @PostMapping(value = "excel-to-postgres", consumes = {
            "multipart/form-data"
    })

    @ApiOperation(value = "Upload File", consumes = "multipart/form-data")
    public ResponseEntity<Object> getFile(@RequestPart("file") @ApiParam(value = "File", required = true) MultipartFile file, @RequestParam("thread") Boolean thread) {
        return  excelDataAndImport.importData(file,thread,"v2");
    }



}


