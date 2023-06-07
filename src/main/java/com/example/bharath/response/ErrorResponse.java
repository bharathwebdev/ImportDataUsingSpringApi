package com.example.bharath.response;


import java.util.List;

public class ErrorResponse extends Response {

    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}