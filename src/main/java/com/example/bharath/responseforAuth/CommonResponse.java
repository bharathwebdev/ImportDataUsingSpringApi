package com.example.bharath.responseforAuth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CommonResponse<T> implements ResponseInterface {
    private String status;
    private T data;
}
