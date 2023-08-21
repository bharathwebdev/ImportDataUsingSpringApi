package com.example.bharath;

import com.example.bharath.Filter.DataTransfer;
import com.example.bharath.Filter.TransformedMap;
import com.example.bharath.response.ErrorResponse;
import com.example.bharath.response.HomeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Component
public class FetchData {

    private final JdbcTemplate jdbcTemplate;

    public FetchData(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    HomeResponse homeResponse;

    private List<Map<String, Object>>  finalData(List<Map<String, Object>> result,String... columNames){

        List<String> ignoreColumnsName = Arrays.stream(columNames).toList();

        DataTransfer transfer = new DataTransfer();

        transfer.transform(result,ignoreColumnsName);







        return   transfer.transform(result,ignoreColumnsName);
    }



    public ResponseEntity<Object> getTableList(){
        String query = "select * from information_schema.tables where table_schema='public';  ";
        List<Map<String, Object>> result =  jdbcTemplate.queryForList(query);
         return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllRows(String tableName, int page, int pageSize) {

        int offset = (page - 1) * pageSize;
        try {


            String query = "SELECT * FROM " + tableName + " ORDER BY id ASC LIMIT ? OFFSET ?";

            String query2 = "SELECT COUNT(*) FROM " + tableName;

            List<Map<String, Object>> result = jdbcTemplate.queryForList(query, pageSize, offset);


            homeResponse.setData(finalData(result,"id"));
            homeResponse.setTotalRecords(jdbcTemplate.queryForObject(query2, Integer.class));
            homeResponse.setMessage(Constants.SUCCESS_FETCH);
            homeResponse.setStatus(Constants.SUCCESS);
            homeResponse.setLimit(pageSize);
            homeResponse.setOffset(offset);

        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(Constants.ERROR);
            errorResponse.setMessage("error while fetching documents");
            errorResponse.setErrors(Collections.singletonList(e.getMessage()));
            if (e.getMessage().contains("bad SQL grammar")) {
                return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(404));
            } else if (e.getMessage().contains("OFFSET must not be negative")) {
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(homeResponse, HttpStatus.OK);
    }
}
