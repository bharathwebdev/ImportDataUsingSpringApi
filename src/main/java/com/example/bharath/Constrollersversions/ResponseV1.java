package com.example.bharath.Constrollersversions;

import com.example.bharath.FetchData;
import com.example.bharath.GetExcelDataAndImport;
import com.example.bharath.dto.AuthRequest;
import com.example.bharath.functions.CommonFunctions;
import com.example.bharath.model.BankUsers;
import com.example.bharath.repository.BankuserRepository;
import com.example.bharath.responseforAuth.CommonResponse;
import com.example.bharath.service.JwtService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:3000")
@RequestMapping("/api/v1")
public class ResponseV1 {


    GetExcelDataAndImport excelDataAndImport;
    @Autowired
    FetchData s;

    @Autowired
    CommonResponse commonResponse;
    @Autowired
    JwtService jwtService;

    @Autowired
    CommonFunctions commonFunctions;

    @Autowired
    BankuserRepository bankuserRepository;

    public ResponseV1(GetExcelDataAndImport excelDataAndImport) {
        this.excelDataAndImport = excelDataAndImport;
    }

    @GetMapping("/getData")
    public ResponseEntity<Object> home(@RequestParam("tableName") String tableName, @RequestParam("page") int value,
                                       @RequestParam("pageSize") int offset) {

        return s.getAllRows(tableName, value, offset);
    }

    @GetMapping("/getallfiles")
    public ResponseEntity<Object> tableList(){
        return s.getTableList();
    }

    @PostMapping(value = "excelToPostgres", consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<Object> getFile(@RequestPart("file") @ApiParam(value = "File", required = true) MultipartFile file, @RequestParam("thread") Boolean thread) {
        return excelDataAndImport.importData(file, thread, "v1");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        ResponseEntity<String> response =null;
        Authentication authentication = commonFunctions.AuthenticateUser(authRequest);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authentication.isAuthenticated()){
            List<BankUsers> bankUsers =  bankuserRepository.findByEmail(authRequest.getEmail());
            String token = jwtService.generateToken(authRequest.getEmail(),authorities,bankUsers.get(0));
            response = ResponseEntity.status(200).body(token);
        } else {
            response = ResponseEntity.status(403).body("Invalid Credentials");
        }
        return response;
    }

}


