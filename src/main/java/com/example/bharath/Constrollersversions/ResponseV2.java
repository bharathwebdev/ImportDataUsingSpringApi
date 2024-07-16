package com.example.bharath.Constrollersversions;

import com.example.bharath.FetchData;
import com.example.bharath.GetExcelDataAndImport;
import com.example.bharath.dto.AuthRequest;
import com.example.bharath.functions.CommonFunctions;
import com.example.bharath.model.BankUsers;
import com.example.bharath.repository.BankuserRepository;
import com.example.bharath.responseforAuth.CommonResponse;
import com.example.bharath.responseforAuth.ErrorResponse;
import com.example.bharath.responseforAuth.ResponseInterface;
import com.example.bharath.responseforAuth.ResponseService;
import com.example.bharath.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:3000")
@RequestMapping("/api/v2")
public class ResponseV2 {
    GetExcelDataAndImport excelDataAndImport;
    public ResponseV2(GetExcelDataAndImport excelDataAndImport) {
        this.excelDataAndImport = excelDataAndImport;
    }


    @Autowired
    FetchData s;

    @Autowired
    JwtService jwtService;

    @Autowired
    CommonFunctions commonFunctions;

    @Autowired
    ResponseService responseService;

   @Autowired
    BankuserRepository bankuserRepository;
    @Autowired
    ErrorResponse errorResponse;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("hi im running ");
    }

    @GetMapping("/getData")
    public Object home(@RequestParam("tableName") String tableName, @RequestParam("page") int value,
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
        return excelDataAndImport.importData(file, thread, "v2");
    }




    @PostMapping("/authenticate")
    public ResponseEntity<ResponseInterface> authenticateAndGetToken2(@RequestBody AuthRequest authRequest, HttpServletResponse res) {
        ResponseEntity<ResponseInterface> response =null;
        Authentication authentication = null;
        try{

            authentication = commonFunctions.AuthenticateUser(authRequest);

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if(authentication.isAuthenticated()){
                List<BankUsers> bankUsers =  bankuserRepository.findByEmail(authRequest.getEmail());
                List<String> roles = new ArrayList<>();
                authorities.forEach(e->roles.add(e.getAuthority()));
                String token = jwtService.generateToken(authRequest.getEmail(),authorities, bankUsers.get(0));

//                System.out.println("this is generated token : " + token);
                Cookie cookie = new Cookie("BearerToken", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(1800000);


//                cookie.setSecure(true);

                // Make sure the cookie is only sent over HTTPS connections
                res.addCookie(cookie);
//                res.addHeader("Authorization",token);

                Claims claims = jwtService.getClaims(token);
                responseService.setAudience(claims.getAudience());
                responseService.setIssuer(claims.getIssuer());
                responseService.setSubject(claims.getSubject());
                responseService.setJwtId(claims.getId());
                responseService.setExpirationTime(claims.getExpiration());
                responseService.setRole(claims.get("role"));
                responseService.setFirstname(claims.get("firstname"));
                responseService.setLastname(claims.get("lastname"));
                responseService.setIssuedAt(claims.getIssuedAt());
                responseService.setBearerToken(token);
                CommonResponse<ResponseService> successResponse = new CommonResponse<>();
                successResponse.setStatus("success");
                successResponse.setData(responseService);
                response = ResponseEntity.status(200).body(successResponse);
            }

        }catch (ExpiredJwtException e){
            e.printStackTrace();
            System.out.println("Token Expired : "+e);
        } catch (Exception e){

            CommonResponse<ErrorResponse> failResponse = new CommonResponse<>();
            errorResponse.setMessage(e.getMessage());
            failResponse.setData(errorResponse);
            failResponse.setStatus("error");
            response = ResponseEntity.status(403).body(failResponse);
        }

        return response;
    }


    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        // Check if the user is authenticated based on the presence of the HttpOnly cookie
        Cookie[] cookies = request.getCookies();
        boolean isAuthenticated = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("BearerToken")) {
                    isAuthenticated = true;
                    break;
                }
            }
        }

        // Return the authentication status to the frontend
        return ResponseEntity.ok(isAuthenticated);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the token (Optional: Add the token to the blacklist)
        // Here, you can implement token invalidation logic if needed.

        // Clear the HttpOnly cookie by setting its expiry time to a past date
        Cookie cookie = new Cookie("BearerToken",null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/register")
    public ResponseEntity<String> RegisterUser(@RequestBody BankUsers bankUsers){
        if(bankUsers.getPassword().isEmpty() || bankUsers.getPassword().isEmpty() ){
            return ResponseEntity.status(403).body("please Enter email and password");
        }
        if(bankUsers.getRole().isEmpty()){
            bankUsers.setRole("USER");
        }

        BankUsers savedBankUsers = null;
        ResponseEntity response = null;
        try{
            String mail = bankUsers.getEmail();
            if(bankuserRepository.findByEmail(mail).isEmpty()) {
                String hashPassword = passwordEncoder.encode(bankUsers.getPassword());
                bankUsers.setPassword(hashPassword);
                savedBankUsers = bankuserRepository.save(bankUsers);
                if (savedBankUsers.getId() > 0) {
                    response = ResponseEntity.status(HttpStatus.CREATED)
                            .body("Successfully Registered");
                }
            }else{
                response = ResponseEntity.status(403).body("User already exists !");
            }
        }catch (Exception e){
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exception occurred due to " + e.getMessage());
        }
        return response;
    }

}


