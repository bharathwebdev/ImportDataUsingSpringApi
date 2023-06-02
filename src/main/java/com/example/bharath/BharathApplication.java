package com.example.bharath;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;

@SpringBootApplication
@RestController
public class BharathApplication {
	public static void main(String[] args) {
		SpringApplication.run(BharathApplication.class, args);
	}
	@GetMapping("/")
	public String Home(){
		return "Home page";
	}
	@PostMapping("/importData")
    public String getFile(@RequestParam("file") MultipartFile file) throws IOException {
        // ...
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to read the first sheet

            Config configReader = Config.INSTANCE;
            String dbName = configReader.getConfig("DB_NAME", "exceldata2");
            String userName = configReader.getConfig("USER_NAME", "postgres");
            String password = configReader.getConfig("PASSWORD", "1811786");

            // Database Connection
            PostgresDb db = new PostgresDb(dbName, userName, password);
            Connection connection = db.establishConnection();

            // Reading Excel data from the file
            ExcelReadWrite wr = new ExcelReadWrite(db, connection,sheet);
            wr.writeDataUsingThreadPool();

        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return "Error occurred while processing the file.";
        }
        return "upload done";

    }


}
